package ru.galuzin.mocksample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SettingsCache {

    Map<String,String> map = new ConcurrentHashMap<String, String>();

    public SettingsCache() {
        System.out.println("setting cache constructor");
    }

    public String getString(String someSetting, String default_some) {
        System.out.println("cache get string");
        final String value = map.get(someSetting);
        System.out.println("cache value = "+value);
        if(value == null) {
            System.out.println("cache load from db");
            return default_some;
        }
        return value;
    }
}
