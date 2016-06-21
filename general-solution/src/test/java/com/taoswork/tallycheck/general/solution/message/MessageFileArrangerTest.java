package com.taoswork.tallycheck.general.solution.message;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Gao Yuan on 2015/6/3.
 */
public class MessageFileArrangerTest {
    @Test
    public void testArranger1(){
        i18nMessageFileArranger arranger = new i18nMessageFileArranger();
        arranger.add("XXX_zh_CN");
        arranger.add("XXX_zh_TW");
        arranger.add("XXX_zh_CN");

        String [] xxx = arranger.fileNamesWithoutLocalization();
        verifyContent(xxx, "XXX");
    }

    @Test
    public void testArranger2(){
        i18nMessageFileArranger arranger = new i18nMessageFileArranger();
        arranger.add("XXX_zh_CN1");
        arranger.add("XXX_zh_TW");
        arranger.add("XXX_zh_CN");

        String [] xxx = arranger.fileNamesWithoutLocalization();
        verifyContent(xxx, "XXX", "XXX_zh_CN1");
    }

    @Test
    public void testArranger3(){
        i18nMessageFileArranger arranger = new i18nMessageFileArranger();
        arranger.add("XXX_en");
        arranger.add("XXX_zh");
        arranger.add("XXX_zh_CN");

        String [] xxx = arranger.fileNamesWithoutLocalization();
        verifyContent(xxx, "XXX");
    }

    @Test
    public void testArranger4(){
        i18nMessageFileArranger arranger = new i18nMessageFileArranger();
        arranger.add("XXX_");
        arranger.add("XXX_enx");
        arranger.add("XXX_zh_TW");
        arranger.add("XXX_zh_CN");

        String [] xxx = arranger.fileNamesWithoutLocalization();
        verifyContent(xxx, "XXX", "XXX_enx");
    }

    @Test
    public void testArranger5(){
        i18nMessageFileArranger arranger = new i18nMessageFileArranger();
        arranger.add("YYY");
        arranger.add("XXX_zh_CN");
        arranger.add("XXX_zh_TW");
        arranger.add("XXX_zh_CN");

        String [] xxx = arranger.fileNamesWithoutLocalization();
        verifyContent(xxx, "XXX", "YYY");
    }


    private void verifyContent(String[] output, String... checking){
        Assert.assertEquals(output.length, checking.length);
        for (String check : checking){
            Assert.assertTrue(Arrays.asList(output).contains(check));
        }
    }
}
