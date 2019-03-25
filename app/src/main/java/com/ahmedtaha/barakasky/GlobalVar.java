package com.ahmedtaha.barakasky;

import android.app.Application;

public class GlobalVar extends Application {
    public String first_open = "true";

    public String getFirst_open() {
        return first_open;
    }

    public void setFirst_open(String first_open) {
        this.first_open = first_open;
    }
}
