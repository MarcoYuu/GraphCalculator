package yuu.application.graphcalculator.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;

/**
 * キーリピート機能付きボタン.
 * 
 * c.f http://d.hatena.ne.jp/tomorrowkey/20110211/1297385760
 */
public class RepeatButton extends Button implements OnLongClickListener {

	private static int REPEAT_INTERVAL = 50;
	private boolean isContinue = true;

	private Handler handler;

	public RepeatButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnLongClickListener(this);
		handler = new Handler();
	}
	
	public static void setRepeatInterval(int milisec){
		REPEAT_INTERVAL =milisec;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		// キーから指が離されたら連打をオフにする
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isContinue = false;
		}
		return true;
	}

	@Override
	public boolean onLongClick(View v) {
		// 長押しをきっかけに連打を開始する
		isContinue = true;
		handler.post(repeatRunnable);
		return true;
	}

	Runnable repeatRunnable = new Runnable() {
		@Override
		public void run() {
			// 連打フラグをみて処理を続けるか判断する
			if (!isContinue) {
				return;
			}
			// クリック処理を実行する
			performClick();
			// 連打間隔を過ぎた後に、再び自分を呼び出す
			handler.postDelayed(this, REPEAT_INTERVAL);
		}
	};
}