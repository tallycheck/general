package com.taoswork.tallybook.general.solution.menu;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Gao Yuan on 2015/4/28.
 */
public class Menu implements IMenu {
    private static final Logger LOGGER = LoggerFactory.getLogger(Menu.class);

    //root wont be added to the path maps
    private MenuEntry insideRoot;

    private transient MultiValueMap<String, MenuPath> pathOfUrl;
    private transient MultiValueMap<String, MenuPath> pathOfEntity;
    private transient MultiValueMap<String, MenuPath> pathOfKey;

    private transient int leaves = 0;
    private transient int branches = 0;

    public Menu(String menuJson, IMenuEntryUpdater updater){
        this(new ByteArrayInputStream(menuJson.getBytes()), updater);
    }

    public Menu(InputStream menuJson, IMenuEntryUpdater updater){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            insideRoot = objectMapper.readValue(menuJson, MenuEntry.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        insideRoot.dropInvalidKids();
        autoFill(updater);
        ensureQuickMapping();
    }

    Menu(MenuEntryBuilder builder, IMenuEntryUpdater updater){
        insideRoot = builder.innerMenuEntry();
        insideRoot.dropInvalidKids();
        autoFill(updater);
        ensureQuickMapping();
    }

    @Override
    public IMenuEntry getEntry(MenuPath path) {
        if(null == path)
            return null;
        return insideRoot.getEntry(path);
    }

    @Override
    public IMenuEntry theFirstLeafEntry() {
        IMenuEntry entry = this.findMenuEntry(this.insideRoot, new MenuPath(), 0, new Matcher() {
            @Override
            public boolean match(TravParam param) {
                return param.hasLeave == false;
            }
        });
        return entry;
    }

    @Override
    public Collection<IMenuEntry> getEntriesOnPath(MenuPath path) {
        if(null == path)
            return null;
        return insideRoot.getEntriesOnPath(path);
    }

    @Override
    public Collection<MenuPath> getPathOfUrl(String url) {
        return (Collection<MenuPath>)pathOfUrl.get(url);
    }

    @Override
    public Collection<MenuPath> getPathOfEntity(String entity) {
        return (Collection<MenuPath>)pathOfEntity.get(entity);
    }

    @Override
    public Collection<MenuPath> getPathOfEntry(String key) {
        return (Collection<MenuPath>)pathOfKey.get(key);
    }

    @Override
    public MenuPath getSinglePathOfUrl(String url) {
        Iterator<MenuPath> iterator = pathOfUrl.iterator(url);
        if(iterator.hasNext()){
            return iterator.next();
        }
        return null;
    }

    @Override
    public MenuPath getSinglePathOfEntity(String entity) {
        Iterator<MenuPath> iterator = pathOfEntity.iterator(entity);
        if(iterator.hasNext()){
            return iterator.next();
        }
        return null;
    }

    @Override
    public MenuPath getSinglePathOfEntry(String key) {
        Iterator<MenuPath> iterator = pathOfKey.iterator(key);
        if(iterator.hasNext()){
            return iterator.next();
        }
        return null;
    }

    @Override
    public Collection<IMenuEntry> getEntries() {
        return insideRoot.subEntries();
    }

    public int entryCount(boolean branch, boolean leaf) {
        return 0 + (branch ? this.branches : 0) + (leaf ? this.leaves : 0);
    }

    //Order reversed
    public String printKeys(){
        final StringBuilder sb = new StringBuilder();
        this.traverseMenuEntry(this.insideRoot, new MenuPath(), 0, new TravCallback() {
            @Override
            public void callback(TravParam param) {
                int level = param.level;
                for(int i = 0 ; i < level ; ++i){
                    sb.append(" ");
                }
                sb.append(param.entry.getKey()).append("\n");
            }
        });
        return sb.toString();
    }

    protected void ensureQuickMapping(){
        if(pathOfUrl != null){
            return;
        }
        pathOfUrl = new MultiValueMap<String, MenuPath>();
        pathOfEntity = new MultiValueMap<String, MenuPath>();
        pathOfKey = new MultiValueMap<String, MenuPath>();

        this.traverseMenuEntry(this.insideRoot, new MenuPath(), 0, new TravCallback() {
            @Override
            public void callback(TravParam param) {
                if(param.level > 0) {
                    if(StringUtils.isEmpty(param.entry.getKey())){
                        return;
                    }
                    IMenuEntry entry = param.entry;
                    MenuPath path = param.path;

                    pathOfUrl.put(entry.getUrl(), path);
                    pathOfEntity.put(entry.getEntity(), path);
                    pathOfKey.put(entry.getKey(), path);

                    if (param.hasLeave)
                        branches++;
                    else
                        leaves++;
                }
            }
        });
    }

    public void autoFill(final IMenuEntryUpdater updater) {
        this.traverseMenuEntry(this.insideRoot, new MenuPath(), 0, new TravCallback() {
            @Override
            public void callback(TravParam param) {
                IMenuEntry entry = param.entry;
                try {
                    if (updater != null)
                        updater.update(entry);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error(e.getMessage());
                }
            }
        });
    }

    private static class TravParam{
        IMenuEntry entry;
        MenuPath path;
        int level;
        boolean hasLeave;

        public TravParam(IMenuEntry entry, MenuPath path, int level, boolean hasLeave) {
            this.entry = entry;
            this.path = path;
            this.level = level;
            this.hasLeave = hasLeave;
        }
    }

    private static interface TravCallback{
        void callback(TravParam param);
    }

    protected void traverseMenuEntry(IMenuEntry entry, MenuPath path, int level, TravCallback callback){
        boolean hasLeave = false;
        for (IMenuEntry subEntry : entry.subEntries()){
            MenuPath subPath = path.makeSubPath(subEntry.getKey());
            traverseMenuEntry(subEntry, subPath, level + 1, callback);
            hasLeave = true;
        }

        TravParam travParam = new TravParam(entry, path, level, hasLeave);
        callback.callback(travParam);
    }

    private static interface Matcher{
        boolean match(TravParam travParam);
    }

    protected IMenuEntry findMenuEntry(IMenuEntry root, MenuPath path, int level, Matcher matcher){
        boolean hasLeave = false;
        for (IMenuEntry subEntry : root.subEntries()){
            MenuPath subPath = path.makeSubPath(subEntry.getKey());
            IMenuEntry entry = findMenuEntry(subEntry, subPath, level + 1, matcher);
            if(entry != null)
                return entry;
            hasLeave = true;
        }

        TravParam travParam = new TravParam(root, path, level, hasLeave);
        if(matcher.match(travParam)){
            return root;
        }
        return null;
    }

}
