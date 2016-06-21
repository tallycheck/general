package com.taoswork.tallycheck.general.web.view.thymeleaf;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gao Yuan on 2015/4/27.
 */
public class TallyBookDialect extends AbstractDialect {
    private Set<IProcessor> processors = null;

    @Override
    public String getPrefix() {
        return "tb";
    }

    private void initProcessors() {
        if (null == processors) {
            processors = new HashSet<IProcessor>();
            processors.add(new CsrfProcessor());
        }
    }

    @Override
    public Set<IProcessor> getProcessors() {
        initProcessors();
        return processors;
    }

    public void setProcessors(Set<IProcessor> processors) {
        this.processors = processors;
    }
}
