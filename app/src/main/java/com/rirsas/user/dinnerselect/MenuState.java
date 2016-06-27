package com.rirsas.user.dinnerselect;

/**
 * Created by USER on 2016/06/27.
 */
public class MenuState {

    private String menu_name = "";
    private boolean selected = false;

    public MenuState(String menu_name, boolean selected){
        this.menu_name = menu_name;
        this.selected = selected;
    }

    public String getMenu_name(){
        return menu_name;
    }

    public void setMenu_name(String menu_name){
        this.menu_name = menu_name;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

}
