package com.taoswork.tallybook.general.solution.message;

import java.util.*;

/**
 * Created by Gao Yuan on 2015/6/3.
 */
public class i18nMessageFileArranger {
    private List<String> inputs = new ArrayList<String>();

    public void add(Collection<String> fileNames){
        for(String filename : fileNames){
            add(filename);
        }
    }

    public void add(String... fileNames){
        for(String filename : fileNames){
            add(filename);
        }
    }

    public void add(String fileName){
        int posfix = fileName.lastIndexOf(".properties");
        if(posfix > 0){
            fileName = fileName.substring(0, posfix);
        }

        inputs.add(fileName);
    }

    public String[] fileNamesWithoutLocalization(){
        Set<String> simpleFileNames = new HashSet<String>();
        for(String fileName_zh_CN : inputs){
            int filenameLen = fileName_zh_CN.length();
            int last_ = fileName_zh_CN.lastIndexOf("_");

            if (last_ >= (filenameLen - 3)){ // drop loc
                String fileName_zh = fileName_zh_CN.substring(0, last_);
                int fileName_zh_Len = fileName_zh.length();
                int locLast_ = fileName_zh.lastIndexOf("_");
                if(locLast_ >= (fileName_zh_Len - 3)){ //drop language
                    String fileName = fileName_zh.substring(0, locLast_);
                    simpleFileNames.add(fileName);
                } else {
                    simpleFileNames.add(fileName_zh);
                }
            } else {
                simpleFileNames.add(fileName_zh_CN);
            }
        }
        return simpleFileNames.toArray(new String[]{});
    }
}
