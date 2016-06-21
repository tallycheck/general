package com.taoswork.tallybook.general.solution.conf;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Gao Yuan on 2016/6/18.
 */
public class PreferredConfigurationTest {
    private static final String RES_FN_PREF = "pc/";

    private static final String FN_4_ABSOLUTE_PATH = RES_FN_PREF + "xxx-absolute-filepath.properties";
    private static final String FN_4_CLASSPATH = "xxx-classpath.properties";
    private static final String FN_4_CLASSPATH2 = RES_FN_PREF + "xxx-classpath-2.properties";
    private static final String FN_4_HOMEPATH = "xxx-homepath.properties";
    private static final String FN_4_HOMEPATH2 = RES_FN_PREF + "xxx-homepath-2.properties";
    private static final String FN_4_RELATIVE_PATH = "xxx-relative-filepath.properties";
    private static final String FN_4_RELATIVE_PATH2 = RES_FN_PREF + "xxx-relative-filepath-2.properties";

    private static final String ABC_KEY = "abc";

    private static final String ABC_ABSOLUTE_PATH = "ABC @ absolute filepath";
    private static final String ABC_CLASSPATH = "ABC @ classpath";
    private static final String ABC_CLASSPATH2 = "ABC @ classpath 2";
    private static final String ABC_HOMEPATH = "ABC @ homepath";
    private static final String ABC_HOMEPATH2 = "ABC @ homepath 2";
    private static final String ABC_RELATIVE_PATH = "ABC @ relative filepath";
    private static final String ABC_RELATIVE_PATH2 = "ABC @ relative filepath 2";

    private static File tempAbsoluteFile;
    private static File homePathFile;
    private static File homePathFile2;
    private static File relativePathFile;
    private static File relativePathFile2;

    protected static void copyResourceFileToFileSystem(String resourceFilePath, File target) throws IOException {
        // attempt to load from the context classpath
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(resourceFilePath);
        InputStream inputStream = url.openStream();

        OutputStream outputStream = new FileOutputStream(target);

        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    private void ensureAbsoluteProperties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverride(tempAbsoluteFile.getAbsolutePath());
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_ABSOLUTE_PATH.equals(abc));
    }

    private void ensureClasspath2Properties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverrideViaClasspath(FN_4_CLASSPATH2);
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_CLASSPATH2.equals(abc));
    }

    private void ensureClasspathProperties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverrideViaClasspath(FN_4_CLASSPATH);
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_CLASSPATH.equals(abc));
    }

    private void ensureRelativePath2Properties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverride(FN_4_RELATIVE_PATH2);
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_RELATIVE_PATH2.equals(abc));
    }

    private void ensureRelativePathProperties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverride(FN_4_RELATIVE_PATH);
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_RELATIVE_PATH.equals(abc));
    }

    private void ensureHomePath2Properties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverrideUnderHomePath(FN_4_HOMEPATH2);
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_HOMEPATH2.equals(abc));
    }

    private void ensureHomePathProperties(PreferredConfiguration prefConfiguration) {
        prefConfiguration.pushOverrideUnderHomePath(FN_4_HOMEPATH);
        Configuration conf = prefConfiguration.makeConfiguration();
        String abc = conf.getString(ABC_KEY);
        Assert.assertTrue(ABC_HOMEPATH.equals(abc));
    }

    @BeforeClass
    public static void prepareFiles() throws IOException {
        //Absolute file
        {
            File tempFile = File.createTempFile("pref", null);
            tempFile.deleteOnExit();
            copyResourceFileToFileSystem(FN_4_ABSOLUTE_PATH, tempFile);
            tempAbsoluteFile = tempFile;
        }

        //Relative path file
        {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();

            String userDir = System.getProperty("user.dir");
            {
                File fRp = new File(userDir, FN_4_RELATIVE_PATH);
                if (fRp.exists()) {
                    Assert.fail();
                }
                try {
                    boolean create = fRp.createNewFile();
                    if (!create)
                        Assert.fail();
                    fRp.deleteOnExit();
                    copyResourceFileToFileSystem(RES_FN_PREF + FN_4_RELATIVE_PATH, fRp);
                    relativePathFile = fRp;
                } catch (IOException e) {
                    Assert.fail();
                }
            }
        }

        //Relative path file2
        {
            String userDir = System.getProperty("user.dir");
            File fRp = new File(userDir, FN_4_RELATIVE_PATH2);
            if (fRp.exists()) {
                Assert.fail();
            }
            try {
                File parentFile = fRp.getParentFile();
                parentFile.mkdirs();
                parentFile.deleteOnExit();

                boolean create = fRp.createNewFile();
                if (!create)
                    Assert.fail();
                fRp.deleteOnExit();
                copyResourceFileToFileSystem(FN_4_RELATIVE_PATH2, fRp);

                relativePathFile2 = fRp;
            } catch (IOException e) {
                Assert.fail();
            }
        }

        String homeDir = System.getProperty("user.home");
        //home path file
        {
            File fRp = new File(homeDir, FN_4_HOMEPATH);
            if (fRp.exists()) {
                Assert.fail();
            }
            try {
                boolean create = fRp.createNewFile();
                if (!create)
                    Assert.fail();
                fRp.deleteOnExit();
                copyResourceFileToFileSystem(RES_FN_PREF + FN_4_HOMEPATH, fRp);
                homePathFile = fRp;
            } catch (IOException e) {
                Assert.fail();
            }
        }
        //home path file2
        {
            File fRp = new File(homeDir, FN_4_HOMEPATH2);
            if (fRp.exists()) {
                Assert.fail();
            }
            try {
                File parentFile = fRp.getParentFile();
                parentFile.mkdirs();
                parentFile.deleteOnExit();

                boolean create = fRp.createNewFile();
                if (!create)
                    Assert.fail();
                fRp.deleteOnExit();
                copyResourceFileToFileSystem(FN_4_HOMEPATH2, fRp);
                homePathFile2 = fRp;
            } catch (IOException e) {
                Assert.fail();
            }
        }

    }

    @AfterClass
    public static void destroyFiles() {
        File[] files = new File[]{tempAbsoluteFile, relativePathFile, relativePathFile2, homePathFile, homePathFile2};
        for (File f : files) {
            if (f.exists()) {
                boolean deleteOk = f.delete();
                if (!deleteOk)
                    Assert.fail();
            }
        }
    }

    @Test
    public void testAbsolutePathProperties() throws IOException {
        PreferredConfiguration prefConfiguration = new PreferredConfiguration();
        ensureAbsoluteProperties(prefConfiguration);
    }

    @Test
    public void testClassPathProperties() {
        {
            PreferredConfiguration prefConfiguration = new PreferredConfiguration();
            ensureClasspathProperties(prefConfiguration);
        }
        {
            PreferredConfiguration prefConfiguration = new PreferredConfiguration();
            ensureClasspath2Properties(prefConfiguration);
        }
    }

    @Test
    public void testRelativePathProperties() {
        {
            PreferredConfiguration prefConfiguration = new PreferredConfiguration();
            ensureRelativePathProperties(prefConfiguration);
        }
        {
            PreferredConfiguration prefConfiguration = new PreferredConfiguration();
            ensureRelativePath2Properties(prefConfiguration);
        }
    }

    @Test
    public void testHomePathProperties() {
        {
            PreferredConfiguration prefConfiguration = new PreferredConfiguration();
            ensureHomePathProperties(prefConfiguration);
        }
        {
            PreferredConfiguration prefConfiguration = new PreferredConfiguration();
            ensureHomePath2Properties(prefConfiguration);
        }
    }

    @Test
    public void testOverrides() {
        final PreferredConfiguration prefConfiguration = new PreferredConfiguration();
        {
            ensureAbsoluteProperties(prefConfiguration);
            ensureClasspath2Properties(prefConfiguration);
            ensureClasspath2Properties(prefConfiguration);
            ensureHomePathProperties(prefConfiguration);
            ensureHomePath2Properties(prefConfiguration);
            ensureRelativePathProperties(prefConfiguration);
            ensureRelativePath2Properties(prefConfiguration);
        }
        {
            ensureRelativePath2Properties(prefConfiguration);
            ensureRelativePathProperties(prefConfiguration);
            ensureHomePath2Properties(prefConfiguration);
            ensureHomePathProperties(prefConfiguration);
            ensureClasspath2Properties(prefConfiguration);
            ensureClasspath2Properties(prefConfiguration);
            ensureAbsoluteProperties(prefConfiguration);
        }
    }
}
