package ru.galuzin.mocksample;

public class SettingsHelper {

    public static final String SOME_SETTING = "SomeSetting";

    private static SettingsCache settingsCache;


    public SettingsHelper(){
        System.out.println("setting helper constructor");
    }

    public void postConstruct() {
        settingsCache = new SettingsCache();
    }

    public String getSomeSetting(){
        if( settingsCache==null ) {
            throw new IllegalStateException("cache not intialized");
        }
        return settingsCache.getString(SOME_SETTING, "");
    }
}
