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

    public FlowModel(String name,boolean checked)
    {
        itemName = name;
        isChecked = checked;
    }


    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked()
    {
        isChecked = !isChecked;
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
