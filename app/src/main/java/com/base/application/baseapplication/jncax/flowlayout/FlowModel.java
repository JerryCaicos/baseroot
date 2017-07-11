package com.base.application.baseapplication.jncax.flowlayout;

/**
 * Created by JerryCaicos on 2017/7/11.
 */

public class FlowModel
{
    private boolean isChecked;

    private String itemName;

    public FlowModel(){

    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        isChecked = checked;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
}
