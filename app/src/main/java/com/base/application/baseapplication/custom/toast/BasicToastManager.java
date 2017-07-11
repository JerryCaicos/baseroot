package com.base.application.baseapplication.custom.toast;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class BasicToastManager extends Handler
{

	/* Potential messages for the handler to send * */
	private static final class Messages
	{

		/* Hexadecimal numbers that represent acronyms for the operation * */
		private static final int DISPLAY_BASICTOAST = 0x445354;
		private static final int ADD_BASICTOAST = 0x415354;
		private static final int REMOVE_BASICTOAST = 0x525354;
	}

	private static BasicToastManager mBasicToastManager;

	private final Queue<BasicToast> mQueue;

	/* Private method to create a new list if the manager is being initialized */
	private BasicToastManager()
	{
		mQueue = new LinkedBlockingQueue<BasicToast>();
	}

	/*
	 * Singleton method to ensure all BasicToast are passed through the same
	 * manager
	 */
	protected static synchronized BasicToastManager getInstance()
	{

		if(mBasicToastManager != null)
		{
			return mBasicToastManager;
		}
		else
		{
			mBasicToastManager = new BasicToastManager();
			return mBasicToastManager;
		}
	}

	/* Add BasicToast to queue and try to show it */
	protected void add(BasicToast basicToast)
	{
		/* Add BasicToast to queue and try to show it */
		mQueue.add(basicToast);
		this.showNextBasicToast();
	}

	/* Shows the next BasicToast in the list */
	private void showNextBasicToast()
	{

		if(mQueue.isEmpty())
		{
			/* There is no BasicToast to display next */
			return;
		}
        /* Get next BasicToast in the queue */
		final BasicToast basicToast = mQueue.peek();
        /*
         * Show basicToast if none are showing (not sure why this works but it does)
         */
		if(!basicToast.isShowing())
		{
			final Message message = obtainMessage(Messages.ADD_BASICTOAST);
			message.obj = basicToast;
			sendMessage(message);
		}
		else
		{
			sendMessageDelayed(basicToast,Messages.DISPLAY_BASICTOAST,getDuration(basicToast));
		}
	}

	/* Show/dismiss a BasicToast after a specific duration */
	private void sendMessageDelayed(BasicToast basicToast,final int messageId,final long delay)
	{

		Message message = obtainMessage(messageId);
		message.obj = basicToast;
		sendMessageDelayed(message,delay);
	}

	/* Get duration and add one second to compensate for show/hide animations */
	private long getDuration(BasicToast superToast)
	{

		long duration = superToast.getDuration();
		duration += 1000;

		return duration;
	}

	@Override
	public void handleMessage(Message message)
	{

		final BasicToast superToast = (BasicToast)message.obj;
		switch(message.what)
		{

			case Messages.DISPLAY_BASICTOAST:
				showNextBasicToast();
				break;

			case Messages.ADD_BASICTOAST:
				displayBasicToast(superToast);
				break;

			case Messages.REMOVE_BASICTOAST:
				removeBasicToast(superToast);
				break;

			default:
				super.handleMessage(message);
				break;
		}
	}

	/* Displays a basicToast */
	private void displayBasicToast(BasicToast basicToast)
	{

		if(basicToast.isShowing())
		{
            /* If the basicToast is already showing do not show again */
			return;
		}
		final WindowManager windowManager = basicToast.getWindowManager();
		final View toastView = basicToast.getView();
		final WindowManager.LayoutParams params = basicToast.getWindowManagerParams();

		if(windowManager != null)
		{
			windowManager.addView(toastView,params);
		}
		sendMessageDelayed(basicToast,Messages.REMOVE_BASICTOAST,basicToast.getDuration() + 500);
	}

	/* Hide and remove the basicToast */
	protected void removeBasicToast(BasicToast basicToast)
	{

		final WindowManager windowManager = basicToast.getWindowManager();
		final View toastView = basicToast.getView();
		if(windowManager != null)
		{
			mQueue.poll();
			windowManager.removeView(toastView);
			sendMessageDelayed(basicToast,Messages.DISPLAY_BASICTOAST,500);
		}
	}

	/* Cancels/removes all showing pending BasicToasts */
	protected void cancelAllBasicToasts()
	{

		removeMessages(Messages.ADD_BASICTOAST);
		removeMessages(Messages.DISPLAY_BASICTOAST);
		removeMessages(Messages.REMOVE_BASICTOAST);

		for(BasicToast basicToast : mQueue)
		{
			if(basicToast.isShowing())
			{
				basicToast.getWindowManager().removeView(basicToast.getView());
			}
		}
		mQueue.clear();
	}
}
