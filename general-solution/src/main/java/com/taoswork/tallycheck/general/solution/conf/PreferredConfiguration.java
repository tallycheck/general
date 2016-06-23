package com.taoswork.tallycheck.general.solution.conf;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedBuilderProperties;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.ProvidedURLLocationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gao Yuan on 2016/6/15.
 */
public class PreferredConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreferredConfiguration.class);

    //default at position 0, overrides follow
    private final List<Configuration> configFiles = new ArrayList<Configuration>();

    public PreferredConfiguration(){

    }
    public PreferredConfiguration pushOverrideViaBasePath(String basePath, String filename){
        File file = new File(basePath, filename);
        return pushOverride(file);
    }

    public PreferredConfiguration pushOverrideViaUserHome(String filename){
        final String PROP_HOME = "user.home";
        return pushOverrideViaBasePath(System.getProperty(PROP_HOME), filename);
    }

    public PreferredConfiguration pushOverrideViaClassPath(String filename) {
        Parameters parameters = new Parameters();
        FileBasedBuilderParameters fbParameters = parameters.fileBased();

        final FileLocationStrategy strategy;
        if (filename.contains("/")) {
            strategy = new IndirectClasspathLocationStrategy();
        } else {
            strategy = new ClasspathLocationStrategy();
        }
        FileBasedBuilderProperties builderProps = fbParameters.setLocationStrategy(strategy).setFileName(filename);
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
        builder.configure(fbParameters);

        try {
            Configuration conf = builder.getConfiguration();
            this.pushOverride(conf);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PreferredConfiguration pushOverride(String path) {
        File file = new File(path);
        return pushOverride(file);
    }

    public PreferredConfiguration pushOverride(File file) {
        try {
            URL url = file.toURI().toURL();

            Parameters parameters = new Parameters();
            FileBasedBuilderParameters fbParameters = parameters.fileBased();

            final FileLocationStrategy strategy = new ProvidedURLLocationStrategy();
            FileBasedBuilderProperties builderProps = fbParameters.setLocationStrategy(strategy).setURL(url);
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
            builder.configure(fbParameters);

            Configuration conf = builder.getConfiguration();
            this.pushOverride(conf);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return this;

    }

    public PreferredConfiguration pushOverride(URL url){
        Parameters params = new Parameters();

        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties().setURL(url));
        Configuration cf = null;
        try {
            cf = builder.getConfiguration();
        } catch (ConfigurationException e) {
            LOGGER.info("SKIP: " + url);
        }finally {
            pushOverride(cf);
        }
        return this;
    }

    public PreferredConfiguration pushOverride(Configuration conf){
        if(conf != null){
            configFiles.add(conf);
        }
        return this;
    }

    public Configuration makeConfiguration(){
        CompositeConfiguration cc = new CompositeConfiguration();
        int sz = configFiles.size();
        for(int i = sz -1 ; i >= 0 ; i --){
            Configuration conf = configFiles.get(i);
            if(conf != null)
                cc.addConfiguration(conf);
        }

        return cc;
    }

}
