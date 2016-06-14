package com.taoswork.tallybook.general.solution.menu;

import com.taoswork.tallybook.general.extension.collections.CollectionUtility;
import com.taoswork.tallybook.general.extension.utils.TPredicate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuEntry implements IMenuEntry {

    private String key;
    private String name;
    private String description;
    private String icon;
    private String url;
    private String entity;

    /**
     * Defines a permission entry with this value, the tallyuser owns the permission entry can access this menu entry
     * If the permissionKey is empty, tallyuser has any access (any except none) to this.entity can access this menu entry.
     */
    private String securityGuard;

    private List<MenuEntry> entries = new ArrayList<MenuEntry>();

    public MenuEntry() {
        this("", "", "", "");
    }

    public MenuEntry(String icon, String key, String url, Class entity){
        this(icon, key, url,entity.getName());
    }

    public MenuEntry(String icon, String key, String url, String entity){
        if(!url.startsWith("/")){
            url = "/" + url;
        }
        this.icon = icon;
        this.key = key;
        this.url = url;
        this.entity = entity;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public IMenuEntry setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IMenuEntry setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public IMenuEntry setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public IMenuEntry setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public IMenuEntry setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public IMenuEntry setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public String getSecurityGuard() {
        return securityGuard;
    }

    @Override
    public IMenuEntry setSecurityGuard(String securityGuard) {
        this.securityGuard = securityGuard;
        return this;
    }

    @Override
    public IMenuEntry getEntry(final String path) {
        return CollectionUtility.find(this.entries, new TPredicate<MenuEntry>() {
            @Override
            public boolean evaluate(MenuEntry notNullObj) {
                return notNullObj.getKey().equals(path);
            }
        });
    }

    @Override
    public IMenuEntry getEntry(MenuPath path) {
        IMenuEntry current = this;
        for (String pathNode : path.getPath()){
            current = current.getEntry(pathNode);
            if(current == null)
                return null;
        }
        return current;
    }

    @Override
    public List<IMenuEntry> getEntriesOnPath(MenuPath path) {
        List<IMenuEntry> result = new ArrayList<IMenuEntry>();
        IMenuEntry current = this;
        for (String pathNode : path.getPath()) {
            current = current.getEntry(pathNode);
            if (current == null) {
                return null;
            } else {
                result.add(current);
            }
        }
        return result;
    }

    @Override
    public Collection<IMenuEntry> subEntries() {
        return (Collection)this.entries;
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }

    public IMenuEntry setEntries(List<MenuEntry> entries) {
        this.entries = entries;
        return this;
    }

    private boolean _isValid(){
        if(StringUtils.isEmpty(this.getKey())){
            return false;
        }
        return true;
    }

    private boolean hasKid(){
        return (entries != null && entries.size() > 0);
    }

    public void dropInvalidKids(){
        List<MenuEntry> delList = new ArrayList<MenuEntry>();
        for(MenuEntry entry : entries){
            entry.dropInvalidKids();
            if(!entry._isValid()){
                delList.add(entry);
            }
        }
        for(MenuEntry delItem : delList){
            entries.remove(delItem);
        }
    }
}
