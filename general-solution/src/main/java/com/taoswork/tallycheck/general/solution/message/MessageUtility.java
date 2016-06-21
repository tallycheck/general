package com.taoswork.tallycheck.general.solution.message;

import com.taoswork.tallycheck.general.extension.utils.CloneUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessageUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtility.class);

    public static List<String> getMessageBasenames(ResourcePatternResolver resolver, String[] messageDirs, List<String> result) {
        List<String> basenameList = result;
        if(basenameList == null)
            basenameList = new ArrayList<String>();
        i18nMessageFileArranger arranger = new i18nMessageFileArranger();
        for (String messageDir : messageDirs) {
            final String matchPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                messageDir + "*.properties";

            try {
                Resource[] resources = resolver.getResources(
                    //ResourceUtils.CLASSPATH_URL_PREFIX +
                    matchPattern);

                for (Resource res : resources) {
                    try {
                        String respath = res.getFilename();
                        respath = messageDir + respath;
                        arranger.add(respath);
                    } catch (Exception e) {
                        LOGGER.error("Resource '{}' failed to return path.", res.getURI());
                    }
                }
                for (String simplefilename : arranger.fileNamesWithoutLocalization()) {
                    basenameList.add(ResourceUtils.CLASSPATH_URL_PREFIX + simplefilename);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return basenameList;
    }

    public static List<String> getMessageBasenamesUnderDirectory(ResourcePatternResolver resolver, String directory, List<String> basenameList) {
        try {
            directory = "/" + directory + "/";
            directory = directory.replace("//", "/");
            Resource[] resources = resolver.getResources(
                //ResourceUtils.CLASSPATH_URL_PREFIX +
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    directory + "**/*.properties");
            i18nMessageFileArranger arranger = new i18nMessageFileArranger();
            for (Resource res : resources) {
                try {
                    String respath = res.getURI().getPath();
                    int offset = respath.indexOf(directory);
                    String workoutPath = respath.substring(offset);
                    arranger.add(workoutPath);
                } catch (Exception e) {
                    LOGGER.error("Resource '{}' failed to return path.", res.getURI());
                }
            }
            for (String simplefilename : arranger.fileNamesWithoutLocalization()) {
                basenameList.add(ResourceUtils.CLASSPATH_URL_PREFIX + simplefilename);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basenameList;
    }

    public static Map<String, String> translateMessageDictionary(Map<String, String> orig, MessageSource ms, Locale locale) {
        Map<String, String> target = CloneUtility.makeClone(orig);
        for (Map.Entry<String, String> entry : target.entrySet()) {
            String oldVal = entry.getValue();
            String newVal = ms.getMessage(oldVal, null, oldVal, locale);
            entry.setValue(newVal);
        }
        return target;
    }
}
