package com.taoswork.tallybook.general.extension.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class UriUtility {
    public static String findParent(
            String uri) {
        try {
            URI u = new URI(uri);
            URI parent = u.getPath().endsWith("/") ? u.resolve("..") : u.resolve(".");
            return parent.toString();
        } catch (URISyntaxException exp) {
            return null;
        }
    }
}
