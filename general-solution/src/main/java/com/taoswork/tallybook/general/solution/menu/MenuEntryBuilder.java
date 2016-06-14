package com.taoswork.tallybook.general.solution.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gao Yuan on 2016/3/15.
 */
public class MenuEntryBuilder {
    private final MenuEntryBuilder parent;
    private final List<MenuEntryBuilder> kids = new ArrayList<MenuEntryBuilder>();
    private final MenuEntry entry;

    private MenuEntryBuilder(MenuEntry entry) {
        this.parent = null;
        this.entry = entry;
    }

    private MenuEntryBuilder(MenuEntryBuilder parent) {
        this.parent = parent;
        this.entry = new MenuEntry();
        this.entry.setUrl("");
    }

    MenuEntry innerMenuEntry(){
        return entry;
    }

    public static MenuEntryBuilder createRootNode(){
        return new MenuEntryBuilder(new MenuEntry());
    }

    public MenuEntryBuilder name(String name) {
        entry.setName(name);
        return this;
    }

    public MenuEntryBuilder key(String key) {
        entry.setKey(key);
        return this;
    }

    public MenuEntryBuilder description(String description) {
        entry.setDescription(description);
        return this;
    }

    public MenuEntryBuilder icon(String icon) {
        entry.setIcon(icon);
        return this;
    }

    public MenuEntryBuilder url(String url) {
        entry.setUrl(url);
        return this;
    }

    public MenuEntryBuilder entity(Class entity) {
        return entity(entity.getName());
    }

    public MenuEntryBuilder entity(String entity) {
        entry.setEntity(entity);
        return this;
    }

    public MenuEntryBuilder securityGuard(String securityGuard) {
        entry.setSecurityGuard(securityGuard);
        return this;
    }

    public MenuEntryBuilder securityGuard(Class securityGuard) {
        return securityGuard(securityGuard.getName());
    }

    public MenuEntryBuilder beginSiblingEntry(){
        return endEntry().beginEntry();
    }

    public MenuEntryBuilder beginEntry(){
        MenuEntryBuilder subNode = new MenuEntryBuilder(this);
        kids.add(subNode);
        return subNode;
    }

    public MenuEntryBuilder endEntry(){
        return parent;
    }

    private void gatherKids(){
        List<MenuEntry> e = new ArrayList<MenuEntry>();
        for(MenuEntryBuilder kid : kids){
            kid.gatherKids();
           e.add(kid.entry);
        }
        entry.setEntries(e);
    }

    public Menu makeMenu(IMenuEntryUpdater updater){
        this.gatherKids();
        return new Menu(this, updater);
    }

}
