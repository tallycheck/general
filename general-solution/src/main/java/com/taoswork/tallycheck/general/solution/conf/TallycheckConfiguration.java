package com.taoswork.tallycheck.general.solution.conf;

import org.apache.commons.configuration2.Configuration;

/**
 * Created by Gao Yuan on 2016/6/23.
 */
public class TallycheckConfiguration {
    private final static String DIRECTORY = ".tallycheck/";
    private final static String FILENAME = "tallycheck.properties";
    private static Configuration sInstance;
    static {
        PreferredConfiguration preferredConfiguration = new PreferredConfiguration();
        String filename2 = DIRECTORY + FILENAME;
        String currentDir = System.getProperty("user.dir");
        String tallycheckA = "tallycheck";
        int aPos = currentDir.lastIndexOf(tallycheckA);
        String basePath = ".";
        if(aPos > 0){
            basePath = currentDir.substring(0, aPos) + tallycheckA +  "/support/conf/";
        }
        preferredConfiguration
                .pushOverrideViaClassPath(filename2)
                .pushOverrideViaClassPath(FILENAME)
                .pushOverride(filename2)
                .pushOverride(FILENAME)
                .pushOverrideViaBasePath(basePath, FILENAME)
                .pushOverrideViaUserHome(filename2)
                .pushOverrideViaUserHome(FILENAME);
        sInstance = preferredConfiguration.makeConfiguration();
    }
    public static Configuration instance(){
        return sInstance;
    }
}
