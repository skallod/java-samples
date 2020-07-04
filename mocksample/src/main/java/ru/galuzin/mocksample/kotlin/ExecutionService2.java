package ru.galuzin.mocksample.kotlin;

public class ExecutionService2 {

    public SettingsHelper2 settingsHelper;

    public ExecutionService2() {
        System.out.println("exec2 service constructor");
    }

    public void execute() {
        String setting  = settingsHelper.getSomeSetting();
        if(!setting.isEmpty()){
            System.out.println("exec2 service success " + setting);
        }
        System.out.println("exec2 service finish");
    }
}
