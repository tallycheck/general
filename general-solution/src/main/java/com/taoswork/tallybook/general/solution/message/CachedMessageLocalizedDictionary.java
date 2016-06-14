package com.taoswork.tallybook.general.solution.message;

import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Gao Yuan on 2015/9/12.
 */
public class CachedMessageLocalizedDictionary {
    private final Map<String, String> original;
    private final MessageSource messageSource;
    private Map<Locale, Map> translated = new HashMap<Locale, Map>();

    public CachedMessageLocalizedDictionary(Map<String, String> original, MessageSource ms){
        this.original = original;
        this.messageSource = ms;
    }

    public Map<String, String> getTranslated(Locale locale){
        Map<String, String> readable = translated.get(locale);
        if(readable == null){
            readable = MessageUtility.translateMessageDictionary(original, messageSource, locale);
            translated.put(locale, readable);
        }
        return readable;
    }
}
