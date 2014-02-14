package yuu.application.graphcalculator.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.Log;

public class Utils {
	
	public static class FpsCounter{
		private int INTERVAL = 500;
		private int LIST_SIZE = 4;
		
	    private long mTime = 0;
	    private int mCount = 0;
	    private LinkedList<Float> mFpsList = new LinkedList<Float>();
	    
	    public FpsCounter(){
	    }
	    
	    public FpsCounter(int interval, int average_size){
	    	INTERVAL =interval;
	    	LIST_SIZE =average_size;
	    }
	
	    public void countFPS() {
	        long time = System.currentTimeMillis();
	        if(time - mTime >= INTERVAL) {
	            final float fps = mCount * 1000 / (float)(time - mTime);
	            mFpsList.offer(fps);
	            while(mFpsList.size() > LIST_SIZE) {
	                mFpsList.remove();
	            }
	            mTime = time;
	            mCount = 0;
	            
	            Float[] fpss = mFpsList.toArray(new Float[0]);
	            Arrays.sort(fpss);
	            Log.d("FPS:", String.format("Max:%4.1f, Min:%4.1f", fpss[fpss.length-1], fpss[0]));
	            
	        } else {
	            ++mCount;
	        }
	    }
	}

	/**
	 * スタックトレース記録.
	 * <pre>ログにスタックトレースを出力する</pre>
	 * @param e 例外
	 * @param toast_comment　トーストコメント
	 * @param Tag ログのタグ
	 */
	public static void logStackTrace(Exception e, String Tag){
		StringWriter sw = null;
		PrintWriter  pw = null;
	
		sw = new StringWriter();
		pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String trace = sw.toString();
	
		Log.d(Tag, trace);
	}

	public static void controlOrientationFix(Activity activity, boolean fixOrient) {
		if (fixOrient) {
			int orientation = activity.getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		} else {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED );
		}
	}
}
