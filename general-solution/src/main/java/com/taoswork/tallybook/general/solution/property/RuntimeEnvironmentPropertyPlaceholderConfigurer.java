package com.taoswork.tallybook.general.solution.property;

import com.taoswork.tallybook.general.extension.collections.PropertiesUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.*;

/**
 * Created by Gao Yuan on 2015/4/23.
 */
public class RuntimeEnvironmentPropertyPlaceholderConfigurer extends
        PropertyPlaceholderConfigurer
        implements PropertiesSubCollectionProvider,
        InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeEnvironmentPropertyPlaceholderConfigurer.class);

    public static final String KEY_NAME_4_RUNTIME_ENVIRONMENT = "runtime.environment";

    public static final String ENVIRONMENT_NAME_DEVELOPMENT = "development";

    protected static final String ENVIRONMENT_NAME_DEFAULT = ENVIRONMENT_NAME_DEVELOPMENT;

    protected static Set<Resource> insidePathResource = new LinkedHashSet<Resource>();
    protected static Set<Resource> defaultPathResource = new LinkedHashSet<Resource>();

    static {
        insidePathResource.add(new ClassPathResource("/"));

        defaultPathResource.add(new ClassPathResource("runtime-properties/tally-default-properties/"));
    }

    protected final String determinedEnvironment;
    protected Set<Resource> propertyPathResources;
    protected Set<Resource> overridablePropertyPathResources;
    protected StringValueResolver stringValueResolver;

    protected Resource[] overrideFileResources;

    protected Properties properties;

    protected boolean publisherVisible = false;

    public RuntimeEnvironmentPropertyPlaceholderConfigurer(){
        super();
        setIgnoreUnresolvablePlaceholders(true); // This default will get overriden by tallyuser options if present
        setNullValue("@null");
        determinedEnvironment = System.getProperty(KEY_NAME_4_RUNTIME_ENVIRONMENT, ENVIRONMENT_NAME_DEFAULT).toLowerCase();
    }

    public boolean isPublisherVisible() {
        return publisherVisible;
    }

    public void setPublisherVisible(boolean publisherVisible) {
        this.publisherVisible = publisherVisible;
    }

    /**
     * Create shared properties resource
     * @param propertiesFileName
     * @param pathResources
     * @return
     * 		Example: (using propertiesFileName = product.properties, location={ResA, ResB})
     * 			fileName: product.properties
     * 		Returns: {ResA/product.properties, ResB/product.properties}
     * @throws IOException
     */
    private static Resource[] createPropertiesFileResource(String propertiesFileName, Set<Resource> pathResources) throws IOException {
        String fileName = propertiesFileName.toLowerCase();
        Resource[] resources = new Resource[pathResources.size()];
        int index = 0;
        for (Resource resource : pathResources) {
            resources[index] = resource.createRelative(fileName);
            index++;
        }
        return resources;
    }

    private static void addExistResourceTo(Collection<Resource> to, Resource ... from){
        for (Resource resource : from) {
            if (resource.exists()) {
                to.add(resource);
            }
        }
    }

    /**
     * Sets the directory from which to read environment-specific properties
     * files; note that it must end with a '/'
     */
    public void setPropertyPathResources(Set<Resource> propertyPathResources) {
        this.propertyPathResources = propertyPathResources;
    }

    /**
     * Sets the directory from which to read environment-specific properties
     * files; note that it must end with a '/'. Note, these properties may be
     * overridden by those defined in propertyPathResources and any "runtime-properties" directories
     *
     * @param overridablePropertyPathResources location containing overridable environment properties
     */
    public void setOverridablePropertyPathResources(Set<Resource> overridablePropertyPathResources) {
        this.overridablePropertyPathResources = overridablePropertyPathResources;
    }

    public Resource[] getOverrideFileResources() {
        return overrideFileResources;
    }

    public void setOverrideFileResources(Resource[] overrideFileResources) {
        this.overrideFileResources = overrideFileResources;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private class PropertyPlaceholderConfigurerResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
            public String resolvePlaceholder(String placeholderName) {
                return RuntimeEnvironmentPropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName,
                        RuntimeEnvironmentPropertyPlaceholderConfigurer.this.properties,
                        PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK);
            }
        }

        private final PropertyPlaceholderHelper helper;
        private final PropertyPlaceholderHelper.PlaceholderResolver resolver;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper("${", "}", ":", true);
            this.resolver = new PropertyPlaceholderConfigurerResolver();
        }

        public String resolveStringValue(String strVal) throws BeansException {
            String value = this.helper.replacePlaceholders(strVal, this.resolver);
            return (value.equals("") ? null : value);
        }
    }

    public StringValueResolver getStringValueResolver() {
        return stringValueResolver;
    }

    @Override //method from InitializingBean
    public void afterPropertiesSet() throws Exception {

        // Prepend the default property locations to the specified property locations (if any)
        Set<Resource> combinedPathResources = new LinkedHashSet<Resource>();
        if (!CollectionUtils.isEmpty(overridablePropertyPathResources)) {
            combinedPathResources.addAll(overridablePropertyPathResources);
        }

        if (!CollectionUtils.isEmpty(propertyPathResources)) {
            combinedPathResources.addAll(propertyPathResources);
        }
        propertyPathResources = combinedPathResources;


        /* Process configuration in the following order (later files override earlier files
         * common-shared.properties
         * [environment]-shared.properties
         * common.properties
         * [environment].properties
         * -Dproperty-override-shared specified value, if any
         * -Dproperty-override specified value, if any  */
        Set<Set<Resource>> allPathResources = new LinkedHashSet<Set<Resource>>();
        allPathResources.add(defaultPathResource);
        allPathResources.add(propertyPathResources);

        ArrayList<Resource> allFileResources = new ArrayList<Resource>();
        String environment = this.determinedEnvironment;
        addExistResourceTo (allFileResources, createPropertiesFileResource("common.properties", insidePathResource));

        for (Set<Resource> locations : allPathResources) {
            addExistResourceTo (allFileResources, createPropertiesFileResource("common-shared.properties", locations));
            addExistResourceTo (allFileResources, createPropertiesFileResource(environment + "-shared.properties", locations));
            addExistResourceTo (allFileResources, createPropertiesFileResource("common.properties", locations));
            addExistResourceTo (allFileResources, createPropertiesFileResource(environment + ".properties", locations));
        }

        if(overrideFileResources != null){
            addExistResourceTo(allFileResources, overrideFileResources);
        }

        setLocations(allFileResources.toArray(new Resource[]{}));
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        stringValueResolver = new PlaceholderResolvingStringValueResolver(props);
        properties = props;
        if(publisherVisible){
            RuntimePropertiesPublisher.instance().add(props);
        }
    }

    @Override
    public Properties getSubProperties(String prefix) {
        return PropertiesUtility.getSubProperties(properties, prefix);
    }
}

