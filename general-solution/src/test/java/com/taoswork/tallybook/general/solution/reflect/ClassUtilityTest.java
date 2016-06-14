package com.taoswork.tallybook.general.solution.reflect;

import com.taoswork.tallybook.general.solution.reflect.mockup.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Gao Yuan on 2015/8/29.
 */
public class ClassUtilityTest {
    @Test
    public void testGetAllSupers(){
        {
            Collection<Class> serializableUis = ClassUtility.getAllSupers(Serializable.class, Pug.class, true, true);
            int serializableInterfacesCnt = serializableUis.size();
            Assert.assertEquals(serializableInterfacesCnt, 3);
            assertHas(serializableUis, IAnimal.class, IDog.class, IPug.class);
        }
        {
            Collection<Class> animalUis = ClassUtility.getAllSupers(IAnimal.class, Pug.class, true, true);
            int animalInterfacesCnt = animalUis.size();
            Assert.assertEquals(animalInterfacesCnt, 2);
            assertHas(animalUis, IDog.class, IPug.class);
        }
        {
            Collection<Class> furUis = ClassUtility.getAllSupers(IHasFur.class, Pug.class, true, true);
            int furInterfacesCnt = furUis.size();
            Assert.assertEquals(furInterfacesCnt, 2);
            assertHas(furUis, IDog.class, IPug.class);
        }
        {
            Collection<Class> anyUis = ClassUtility.getAllSupers(null, Pug.class, true, true);
            int anyInterfacesCnt = anyUis.size();
            Assert.assertEquals(anyInterfacesCnt, 5);
            assertHas(anyUis, Serializable.class, IHasFur.class, IAnimal.class, IDog.class, IPug.class);
        }
        {
            Collection<Class> anyUis = ClassUtility.getAllSupers(null, Monster.class, true, true);
            int anyInterfacesCnt = anyUis.size();
            Assert.assertEquals(anyInterfacesCnt, 8);
            assertHas(anyUis, Serializable.class, IHasFur.class, IAnimal.class, IDog.class, IPug.class, IFish.class, IToy.class, Pug.class);
        }
    }

    private void assertHas(Collection<Class> collection, Class ... classes){
        for (Class clz : classes){
            Assert.assertTrue(collection.contains(clz));
        }
    }
}
