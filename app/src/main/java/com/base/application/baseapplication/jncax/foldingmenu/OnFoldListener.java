package com.base.application.baseapplication.jncax.foldingmenu;

/**
 * Created by JerryCaicos on 16/1/22 10:35.
 */
public interface OnFoldListener
{
	public void onStartFold(float foldFactor);

	public void onFoldingState(float foldFactor,float foldDrawHeight);

	public void onEndFold(float foldFactor);
}
