package com.taoswork.tallycheck.general.solution.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MenuTest {
    private MenuEntry makeMenuEntry(int level, String prefix, int index){
        MenuEntry menuEntry = new MenuEntry();
        menuEntry.setEntity("entity." + prefix + "." + index);
        menuEntry.setUrl(UUID.randomUUID().toString());
        menuEntry.setDescription("description " + menuEntry.getEntity());
        menuEntry.setKey("" + level + "." + prefix + "." + index);
        menuEntry.setName(prefix + "." + index);
        return menuEntry;
    }

    @Test
    public void testOutput(){
        MenuEntry menuEntry = new MenuEntry();
        List<MenuEntry> kidsAtLv1 = new ArrayList<MenuEntry>();
        for (int i = 0 ; i < 4; ++i){
            MenuEntry level1Entry = makeMenuEntry(1, "Group", i);
            List<MenuEntry> kids = new ArrayList<MenuEntry>();
            for(int j = 0 ; j <4 ; ++j){
                MenuEntry level2Entry = makeMenuEntry(2, "Item"+i, j);
                kids.add(level2Entry);
            }
            level1Entry.setEntries(kids);

            kidsAtLv1.add(level1Entry);
        }
        menuEntry.setEntries(kidsAtLv1);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String menuJson = objectMapper.writeValueAsString(menuEntry);
            MenuEntry convertBack = objectMapper.readValue(menuJson, MenuEntry.class);
            String menuJson2 = objectMapper.writeValueAsString(convertBack);

            Assert.assertEquals(menuJson, menuJson2);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoad() throws IOException {
        InputStream fileStream = this.getClass().getClassLoader().getResource("testmenu.json").openStream();
        Menu menu = new Menu(fileStream, null);
        Assert.assertEquals(menu.entryCount(true, false), 5);
        Assert.assertEquals(menu.entryCount(false, true), 12);
        Assert.assertEquals(menu.entryCount(true, true), 17);
        String keys = menu.printKeys();

        Collection<MenuPath> demoPathByKey = menu.getPathOfEntry("demo");
        Collection<MenuPath> demoPathByUrl = menu.getPathOfUrl("/demo");
        Collection<MenuPath> demoPathByEntity = menu.getPathOfEntity("com.example.entity.Demo");
        MenuPath sd = new MenuPath("study", "demo");
        ensureCollection(demoPathByKey, sd);
        ensureCollection(demoPathByUrl, sd);
        ensureCollection(demoPathByEntity, sd);

        Collection<MenuPath> materialPathByKey = menu.getPathOfEntry("material");
        Collection<MenuPath> materialPathByUrl = menu.getPathOfUrl("/material");
        Collection<MenuPath> materialPathByEntity = menu.getPathOfEntity("com.example.entity.Material");
        MenuPath epm = new MenuPath("entertainment", "picture", "material");
        MenuPath wm = new MenuPath("work", "material");
        ensureCollection(materialPathByKey, epm, wm);
        ensureCollection(materialPathByUrl,  epm, wm);
        ensureCollection(materialPathByEntity,  epm, wm);

        IMenuEntry demo = menu.getEntry(new MenuPath("study", "demo"));
        IMenuEntry material1 = menu.getEntry(new MenuPath("work", "material"));
        IMenuEntry material2 = menu.getEntry(new MenuPath("entertainment", "picture", "material"));
        IMenuEntry nullEntry = menu.getEntry(new MenuPath("entertainment", "picture", "pic"));

        Assert.assertNotNull(demo);
        Assert.assertNotNull(material1);
        Assert.assertNotNull(material2);
        Assert.assertNull(nullEntry);
    }

    private void ensureCollection(Collection<MenuPath> target, MenuPath ... refs){
        Set<MenuPath> targetInSet = new HashSet<MenuPath>();
        Set<MenuPath> refsInSet = new HashSet<MenuPath>();
        Set<MenuPath> allInSet = new HashSet<MenuPath>();

        targetInSet.addAll(target);
        for(MenuPath m : refs){
            refsInSet.add(m);
        }
        allInSet.addAll(targetInSet);
        allInSet.addAll(refsInSet);

        Assert.assertEquals(targetInSet.size(), refsInSet.size());
        Assert.assertEquals(targetInSet.size(), allInSet.size());
    }
}
