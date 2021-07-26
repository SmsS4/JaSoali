package com.example.jasoali.models;

import android.view.View;
import android.widget.PopupWindow;

public class WindowAndView {
    private PopupWindow popupWindow;
    private View view;
    public WindowAndView(PopupWindow popupWindow, View view){
        this.popupWindow = popupWindow;
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }
}
