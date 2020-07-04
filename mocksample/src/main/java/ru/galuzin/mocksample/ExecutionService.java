package ru.galuzin.mocksample;

public class ExecutionService {

    SettingsHelper settingsHelper;

    public ExecutionService() {
        System.out.println("exec service constructor");
    }

    public void execute() {
        String setting  = settingsHelper.getSomeSetting();
        if(!setting.isEmpty()){
            System.out.println("exec service success " + setting);
        }
        System.out.println("exec service finish");
    }
}
