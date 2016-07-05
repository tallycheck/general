package com.taoswork.tallycheck.general.solution.spring;

import com.taoswork.tallycheck.general.extension.collections.CollectionUtility;
import com.taoswork.tallycheck.general.extension.utils.TPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import java.util.*;

/**
 * Created by Gao Yuan on 2015/5/18.
 */
public class BeanCreationMonitor implements
        BeanPostProcessor,
        ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanCreationMonitor.class);
    private static final boolean SHOW_BEAN_BY_CREATION = false;
    private static final boolean SHOW_BEAN_BY_NAME = true;

    private static final class BeanProcessInfo {
        BeanProcessInfo(String beanName, Object bean) {
            this.beanName = beanName;
            this.beanHash = bean.hashCode();
            this.time = new Date().getTime();
        }

        final String beanName;
        final int beanHash;
        final long time;

        @Override
        public String toString() {
            return beanName +
                    ", t:" + time +
                    ", hash:" + beanHash;
        }
    }

    private static int activeMonitor = 0;
    private int currentMonitorId = 0;
    private final String name;

    private final List<BeanProcessInfo> beanProcessInfos = new ArrayList<BeanProcessInfo>();
    private final Map<String, Object> beanNameObjects = new HashMap<String, Object>();

    private int started = 0;
    private int stopped = 0;
    private int closed = 0;
    private int refreshed = 0;

    public BeanCreationMonitor(String name) {
        this.name = name;
        currentMonitorId = activeMonitor + 1;
        activeMonitor++;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, final String beanName) throws BeansException {
        BeanProcessInfo fit = CollectionUtility.find(beanProcessInfos, new TPredicate<BeanProcessInfo>() {
            @Override
            public boolean evaluate(BeanProcessInfo notNullObj) {
                return notNullObj.beanName.equals(beanName);
            }
        });

//          //comment following, because of too much warning for beans with 'scope = prototype'
//        if (fit != null) {
//            LOGGER.warn("Bean [" + beanName + "] @ " + name + " already initialized: " + fit + ".");
//        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        beanProcessInfos.add(new BeanProcessInfo(beanName, bean));
        beanNameObjects.put(beanName, bean);
        return bean;
    }

    public String getName() {
        return name;
    }

    private String contextName(ApplicationEvent event) {
        ApplicationContext applicationContext = (ApplicationContext) event.getSource();
        return "[" + this.getName() + "] "+
                "["+applicationContext.getDisplayName()  +"] " +
                "AppHash(" + applicationContext.hashCode() + ") " +
                "MonitorHash(" + this.hashCode() + ")";
    }

    private String buildContextStatusString(ApplicationEvent event) {
        return contextName(event) + " (start,refresh,stop,close}: " +
                started + "," + refreshed + "," + stopped + "," + closed + "\n" +
                " { " + currentMonitorId + " of " + activeMonitor + " }";
    }

    private String getLogPrefix(ApplicationEvent event) {
        return "Context \n" + contextName(event);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent) {
            onApplicationStartedEvent((ContextStartedEvent) event);
        } else if (event instanceof ContextRefreshedEvent) {
            onApplicationRefreshedEvent((ContextRefreshedEvent) event);
        } else if (event instanceof ContextStoppedEvent) {
            onApplicationStoppedEvent((ContextStoppedEvent) event);
        } else if (event instanceof ContextClosedEvent) {
            onApplicationClosedEvent((ContextClosedEvent) event);
        }
    }

    private void onApplicationStartedEvent(ContextStartedEvent event) {
        started++;
        StringBuilder sb = new StringBuilder(getLogPrefix(event) + " OnStart(" + started + "): \n");
        sb.append(buildContextStatusString(event));
        LOGGER.debug(sb.toString());
    }

    private void onApplicationRefreshedEvent(ContextRefreshedEvent event) {
        refreshed++;
        ApplicationContext applicationContext = (ApplicationContext) event.getSource();
        StringBuilder sb = new StringBuilder(getLogPrefix(event) + " OnRefresh(" + refreshed + "): \n");
        int i = 0;
        Map<String, BeanProcessInfo> orderedBeanInfos = new TreeMap<String, BeanProcessInfo>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                }
        );
        for (BeanProcessInfo beanProInfo : beanProcessInfos) {
            String idx = "_";
            int procBeanHash = beanProInfo.beanHash;
            int realBeanHash = 0;
            try {
                realBeanHash = applicationContext.getBean(beanProInfo.beanName).hashCode();
            } catch (Exception e) {
//                LOGGER.warn("Bean [" + beanProInfo.beanName + "]@" + name + " failed to load.");
                idx = "*";
            }
            boolean goodbean = (procBeanHash == realBeanHash);
            if (goodbean) {
                i++;
                idx = "" + i;
                orderedBeanInfos.put(beanProInfo.beanName, beanProInfo);
            }
            if (SHOW_BEAN_BY_CREATION) {
                sb.append("\t" + idx + ".\t" + beanProInfo + ", rhash:" + realBeanHash + "\n");
            }
        }
        if (SHOW_BEAN_BY_CREATION) {
            sb.append("\t" + "---------------------------------------------------------\n");
        }
        if (SHOW_BEAN_BY_NAME) {
            int beanIdx = 1;
            for (Map.Entry<String, BeanProcessInfo> beanProInfoEntry : orderedBeanInfos.entrySet()) {
                sb.append("[+]" + beanIdx + ".\t" + beanProInfoEntry.getValue() + "\n");
                beanIdx++;
            }
        }
        sb.append(buildContextStatusString(event));
        LOGGER.debug(sb.toString());
    }

    private void onApplicationStoppedEvent(ContextStoppedEvent event) {
        stopped++;
        StringBuilder sb = new StringBuilder(getLogPrefix(event) + " OnStop(" + stopped + "): \n");
        sb.append(buildContextStatusString(event));
        LOGGER.debug(sb.toString());
    }

    private void onApplicationClosedEvent(ContextClosedEvent event) {
        closed++;
        StringBuilder sb = new StringBuilder(getLogPrefix(event) + " OnClose(" + closed + "): \n");
        sb.append(buildContextStatusString(event));
        LOGGER.debug(sb.toString());
    }
}

