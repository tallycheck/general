package com.taoswork.tallybook.general.solution.conf;

import org.apache.commons.configuration2.io.*;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * Created by Gao Yuan on 2016/6/18.
 */
public class IndirectClasspathLocationStrategy extends ClasspathLocationStrategy
{
    public IndirectClasspathLocationStrategy(){

    }
    /**
     * {@inheritDoc} This implementation looks up the locator's file name as a
     * resource on the class path.
     */
    @Override
    public URL locate(FileSystem fileSystem, FileLocator locator)
    {
        URL srcUrl = locator.getSourceURL();
        if(srcUrl == null){
            return super.locate(fileSystem, locator);
        }
        return srcUrl;
    }
}