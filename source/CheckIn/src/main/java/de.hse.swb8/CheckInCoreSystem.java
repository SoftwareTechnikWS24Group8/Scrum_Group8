package de.hse.swb8;

public class CheckInCoreSystem {

    public CheckInCoreSystem()
    {
        //TEST

        DataBaseLogin dblogin = new DataBaseLogin();

        Callback callback = (message) -> StartUp(message);

        dblogin.LoginIntoDataBase(callback);
    }

    private void StartUp(DataBaseInfo info)
    {
        System.out.println("CheckInCoreSystem: Starting CheckIn Core " + info.url());
    }
}
