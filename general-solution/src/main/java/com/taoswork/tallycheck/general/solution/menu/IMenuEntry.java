package com.taoswork.tallycheck.general.solution.menu;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IMenuEntry extends Serializable {

    String getName();

    IMenuEntry setName(String name);

    String getKey();

    IMenuEntry setKey(String key);

    String getDescription();

    IMenuEntry setDescription(String description);

    String getIcon();

    IMenuEntry setIcon(String icon);

    String getUrl();

    IMenuEntry setUrl(String url);

    String getEntity();

    IMenuEntry setEntity(String entity);

    String getSecurityGuard();

    IMenuEntry setSecurityGuard(String securityGuard);

    IMenuEntry getEntry(String path);

    IMenuEntry getEntry(MenuPath path);

    List<IMenuEntry> getEntriesOnPath(MenuPath path);

    Collection<IMenuEntry> subEntries();

}
