package com.taoswork.tallycheck.general.solution.proppath;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by Gao Yuan on 2016/3/22.
 */
public class PathBuilder {
    private StringJoiner sj = new StringJoiner(".", "", "");

    public static String buildPath(String ... paths){
        return new PathBuilder(paths).result();
    }

    public PathBuilder(String ... paths) {
        appendAll(paths);
    }

    public PathBuilder append(String path) {
        String[] pieces = path.split("\\.");
        for (String p : pieces) {
            if (StringUtils.isNotEmpty(p)) {
                sj.add(p);
            }
        }
        return this;
    }

    public PathBuilder appendAll(String... paths) {
        for (String path : paths) {
            append(path);
        }
        return this;
    }

    public PathBuilder appendIndex(int index) {
        return append("" + index);
    }

    public String result() {
        return sj.toString();
    }
}