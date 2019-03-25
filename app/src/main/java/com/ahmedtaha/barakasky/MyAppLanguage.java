package com.ahmedtaha.barakasky;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class MyAppLanguage {
    public MyAppLanguage() {
    }

    /*public void IsArabic(Context context, String la){
        if (la.equals("Arabic")){
            setMyAppLanguage(context,"العربية");
        } else {
            setMyAppLanguage(context, "Englis");
        }
    }
    public void setMyAppLanguage(Context context, String lang){
        String myLan = "";
        if (lang.equals("Arabic")){
            myLan = "ar";
        } else {
            myLan = "en";
        }
        Locale locale = new Locale(myLan);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);*/

        /*Locale locale = new Locale(myLan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());*/


    //}
}
