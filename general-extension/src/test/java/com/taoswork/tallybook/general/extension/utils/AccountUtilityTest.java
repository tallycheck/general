package com.taoswork.tallybook.general.extension.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gao Yuan on 2015/10/3.
 */
public class AccountUtilityTest {
    @Test
    public void testPhoneNumber_WithCountryCode(){
        String phone1 = "+86 123 4567890";
        String phone2 = "(86) 123-456-7890";
        String phone3 = "(+86) 123-456-7890";
        String phone4 = "0086 (123)4567890";
        String phone5 = "(0086) (123)4567890";
        String phone6 = "0086-1234567890";
        String standard = "0086-1234567890";

        String standard1 = AccountUtility.makeStandardPhoneNumber(phone1);
        String standard2 = AccountUtility.makeStandardPhoneNumber(phone2);
        String standard3 = AccountUtility.makeStandardPhoneNumber(phone3);
        String standard4 = AccountUtility.makeStandardPhoneNumber(phone4);
        String standard5 = AccountUtility.makeStandardPhoneNumber(phone5);
        String standard6 = AccountUtility.makeStandardPhoneNumber(phone6);

        Assert.assertEquals(standard, standard1);
        Assert.assertEquals(standard, standard2);
        Assert.assertEquals(standard, standard3);
        Assert.assertEquals(standard, standard4);
        Assert.assertEquals(standard, standard5);
        Assert.assertEquals(standard, standard6);

        Assert.assertTrue(AccountUtility.isPhone(standard));
        Assert.assertTrue(AccountUtility.isPhone(phone1));
        Assert.assertTrue(AccountUtility.isPhone(phone2));
        Assert.assertTrue(AccountUtility.isPhone(phone3));
        Assert.assertTrue(AccountUtility.isPhone(phone4));
        Assert.assertTrue(AccountUtility.isPhone(phone5));
        Assert.assertTrue(AccountUtility.isPhone(phone6));
    }

    @Test
    public void testFakePhoneNumber_WithCountryCode(){
        String phone1 = "+86 123s4567890";
        String phone2 = "(86) 1234+567890";
        String phone3 = "0086 12345(67890";
        String phone4 = "$0086-1234567890";

        Assert.assertFalse(AccountUtility.isPhone(phone1));
        Assert.assertFalse(AccountUtility.isPhone(phone2));
        Assert.assertFalse(AccountUtility.isPhone(phone3));
        Assert.assertFalse(AccountUtility.isPhone(phone4));
    }

    @Test
    public void testPhoneNumber_WithNoCountryCode(){
        String phone1 = "123 4567 890122";
        String phone2 = "1234 5678 901 22";
        String phone3 = "12 345-6789-0122";
        String phone4 = "123-456-7890(122)";
        String phone5 = "123  45 6789  0122";
        String standard = "1234567890122";
        String standard1 = AccountUtility.makeStandardPhoneNumber(phone1);
        String standard2 = AccountUtility.makeStandardPhoneNumber(phone2);
        String standard3 = AccountUtility.makeStandardPhoneNumber(phone3);
        String standard4 = AccountUtility.makeStandardPhoneNumber(phone4);
        String standard5 = AccountUtility.makeStandardPhoneNumber(phone5);

        Assert.assertEquals(standard, standard1);
        Assert.assertEquals(standard, standard2);
        Assert.assertEquals(standard, standard3);
        Assert.assertEquals(standard, standard4);
        Assert.assertEquals(standard, standard5);

        Assert.assertTrue(AccountUtility.isPhone(standard));
        Assert.assertTrue(AccountUtility.isPhone(phone1));
        Assert.assertTrue(AccountUtility.isPhone(phone2));
        Assert.assertTrue(AccountUtility.isPhone(phone3));
        Assert.assertTrue(AccountUtility.isPhone(phone4));
        Assert.assertTrue(AccountUtility.isPhone(phone5));
    }
}
