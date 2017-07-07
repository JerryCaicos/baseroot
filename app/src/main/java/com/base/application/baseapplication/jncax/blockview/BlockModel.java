package com.base.application.baseapplication.jncax.blockview;

/**
 * Created by adminchen on 16/1/20 16:19.
 */
public class BlockModel
{
	private String blockname;

	public void setIsChecked()
	{
		this.isChecked = !isChecked;
	}

	private boolean isChecked;

	public boolean isChecked()
	{
		return isChecked;
	}

	public String getBlockname()
	{
		return blockname;
	}


	public BlockModel(String name,boolean check)
	{
		blockname = name;
		isChecked = check;
	}
}
