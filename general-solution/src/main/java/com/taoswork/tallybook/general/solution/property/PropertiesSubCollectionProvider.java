package com.taoswork.tallybook.general.solution.property;

import java.util.Properties;

/**
 * Created by Gao Yuan on 2015/5/12.
 */
public interface PropertiesSubCollectionProvider {
    Properties getSubProperties(String prefix);
}
