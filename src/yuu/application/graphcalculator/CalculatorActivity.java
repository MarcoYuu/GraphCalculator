package yuu.application.graphcalculator;

import yuu.application.graphcalculator.expression.Expression;
import yuu.application.graphcalculator.expression.Expression.ExpressionException;
import yuu.application.graphcalculator.expression.ExpressionParser;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * グラフ描画機能付き関数電卓アクティビティ.
 * 
 * <pre>
 * メインとなるアクティビティ。
 * </pre>
 * 
 * @author Yuu
 * @version 0.1
 * @since 0.1
 */
public class CalculatorActivity extends Activity {

	private static final int ID_SETTING_GRAPH_DIALOG = 0;
	private static final int ID_HELP_DIALOG = 1;
	private static final int MENU_ID_HELP = (Menu.FIRST + 1);

	private enum ExpressionEditTarget {
		PRIMARY_EXPRESSION,
		SECONDARY_EXPRESSION,
		ASSIGNMENT,
	}

	private ExpressionEditTarget mExpressionEditMode = ExpressionEditTarget.PRIMARY_EXPRESSION;

	private Expression mSavedExpression = null;

	private EditText mPrimaryExpression = null;
	private EditText mSecondaryExpression = null;
	private EditText mAnswer = null;
	private EditText mAssign = null;

	private ExpressionParser mExpressionParser = new ExpressionParser();

	/**
	 * アクティビティ作成時の動作.
	 * 
	 * @param savedInstanceState
	 *            Bundle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// IMEを基本的には起動しない
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// ビューの指定
		setContentView(R.layout.main);

		// ビューの取得
		mPrimaryExpression = (EditText) findViewById(R.id.InputExpr);
		mSecondaryExpression = (EditText) findViewById(R.id.InputExpr2);
		mAnswer = (EditText) findViewById(R.id.Answer);
		mAssign = (EditText) findViewById(R.id.assign);

		// ViewPagerアダプタへ各Viewの
		CalculatorAreaAdapter adapter = new CalculatorAreaAdapter(this);
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(adapter);

		mSavedExpression = GrobalApplicationData.getExpression();

		// 計算開始ボタン等の初期化
		initEditableArea();
		initCalcButton();
	}

	// オプションメニューが最初に呼び出される時に1度だけ呼び出されます
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// メニューアイテムを追加します
		menu.add(Menu.NONE, MENU_ID_HELP, Menu.NONE, "Help")
			.setIcon(android.R.drawable.ic_menu_help);
		return super.onCreateOptionsMenu(menu);
	}

	// オプションメニューアイテムが選択された時に呼び出されます
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ID_HELP:
			showDialog(ID_HELP_DIALOG);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// このメソッド内で、ダイアログの生成するコードを記述する
		switch (id) {
		case ID_SETTING_GRAPH_DIALOG:
			return new SettingDialog(this);
		case ID_HELP_DIALOG:
			Dialog help = new Dialog(this);
			help.setTitle(R.string.how_to_use);
			help.setContentView(R.layout.help);
			return help;
		default:
			return null;
		}
	}

	/**
	 * @return
	 */
	private EditText getCurrentEditTarget() {
		switch (mExpressionEditMode) {
		case PRIMARY_EXPRESSION:
			return mPrimaryExpression;
		case SECONDARY_EXPRESSION:
			return mSecondaryExpression;
		case ASSIGNMENT:
			return mAssign;
		default:
			return null;
		}
	}

	/**
	 * 入力領域の初期化.
	 * 
	 * <pre>
	 * おもに、IMEが呼び出されないようにするためのイベントハンドラの登録を行っている。
	 * </pre>
	 */
	private void initEditableArea() {
		// EditTextへのフォーカス時にIMEを無効化する
		/*----------------ここから----------------- */
		final TextView textXEqual = (TextView) findViewById(R.id.x_equal);
		final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// IME非表示
				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		};
		View.OnTouchListener primary_expr_touch_listener = new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				mExpressionEditMode = ExpressionEditTarget.PRIMARY_EXPRESSION;

				mAssign.setTextColor(Color.GRAY);
				textXEqual.setTextColor(Color.GRAY);

				mPrimaryExpression.setTextColor(Color.BLACK);
				mPrimaryExpression.setHintTextColor(Color.BLACK);

				mSecondaryExpression.setTextColor(Color.GRAY);
				mSecondaryExpression.setHintTextColor(Color.GRAY);

				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				v.requestFocus();
				return true;
			}
		};
		View.OnTouchListener secondary_expr_touch_listener = new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				mExpressionEditMode = ExpressionEditTarget.SECONDARY_EXPRESSION;

				mAssign.setTextColor(Color.GRAY);
				textXEqual.setTextColor(Color.GRAY);

				mPrimaryExpression.setTextColor(Color.GRAY);
				mPrimaryExpression.setHintTextColor(Color.GRAY);

				mSecondaryExpression.setTextColor(Color.BLACK);
				mSecondaryExpression.setHintTextColor(Color.BLACK);

				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				v.requestFocus();
				return true;
			}
		};
		View.OnTouchListener assignment_touch_listener = new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				mExpressionEditMode = ExpressionEditTarget.ASSIGNMENT;

				mAssign.setTextColor(Color.BLACK);
				textXEqual.setTextColor(Color.BLACK);

				mPrimaryExpression.setTextColor(Color.GRAY);
				mPrimaryExpression.setHintTextColor(Color.GRAY);

				mSecondaryExpression.setTextColor(Color.GRAY);
				mSecondaryExpression.setHintTextColor(Color.GRAY);

				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				v.requestFocus();
				return true;
			}
		};
		mAnswer.setOnFocusChangeListener(listener);
		mAnswer.setFocusable(false);

		mAssign.setOnFocusChangeListener(listener);
		mAssign.setOnTouchListener(assignment_touch_listener);

		mPrimaryExpression.setOnFocusChangeListener(listener);
		mPrimaryExpression.setOnTouchListener(primary_expr_touch_listener);
		primary_expr_touch_listener.onTouch(mPrimaryExpression, null);

		mSecondaryExpression.setOnFocusChangeListener(listener);
		mSecondaryExpression.setOnTouchListener(secondary_expr_touch_listener);

		inputMethodManager.showSoftInput(mPrimaryExpression, InputMethodManager.SHOW_IMPLICIT);
		/*----------------ここまで----------------- */
	}

	/**
	 * 計算を行うボタンの初期化.
	 * 
	 * <pre>
	 * 式解析機による文字列の解析と、得られた式をアプリケーションに登録するハンドラを実装している。
	 * </pre>
	 */
	private void initCalcButton() {
		// =ボタン
		Button button_equal = (Button) findViewById(R.id.button_equal);
		button_equal.setOnClickListener(new CalcButtonListener());

		// カーソル移動ボタン
		Button button = (Button) findViewById(R.id.button_left);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText edit = getCurrentEditTarget();

				int loc = edit.getSelectionStart();
				if (loc > 0)
					edit.setSelection(loc - 1);
			}
		});
		button = (Button) findViewById(R.id.button_right);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText edit = getCurrentEditTarget();

				int loc = edit.getSelectionStart();
				String prev_text = edit.getText().toString();
				if (loc < prev_text.length())
					edit.setSelection(loc + 1);
			}
		});

		// バックスペース
		button = (Button) findViewById(R.id.button_back);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText edit = getCurrentEditTarget();

				// カーソル位置の取得
				int loc = edit.getSelectionStart();
				String prev_text = edit.getText().toString();

				if (prev_text.length() == 0)
					return;

				// 文字を削除
				String prefix = loc > 1 ? prev_text.substring(0, loc - 1) : "";
				String suffix = loc < prev_text.length() ? prev_text.substring(loc,
					prev_text.length()) : "";
				edit.setText(prefix + suffix);
				if (loc > 0)
					edit.setSelection(loc - 1);
			}
		});
	}

	/**
	 * 計算ボタンの実装.
	 * 
	 * <pre>
	 * 式解析を開始する
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class CalcButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (mExpressionEditMode == ExpressionEditTarget.SECONDARY_EXPRESSION)
				parseExpression(mSecondaryExpression, true);
			else
				parseExpression(mPrimaryExpression, true);
		}

		protected Boolean parseExpression(EditText expression_text, boolean toast) {
			// パーサに式をセット
			mExpressionParser.setExpression(expression_text.getText().toString());

			// 解析を開始
			Expression parsed_expr = null;
			try {
				parsed_expr = mExpressionParser.parse();
			} catch (ExpressionException e) {
				if (e.getMessage() != null)
					Log.d("parseExpression", e.getMessage());
				else
					Log.d("parseExpression", e.toString());

				if (toast)
					Toast.makeText(CalculatorActivity.this, "式が間違っています", Toast.LENGTH_SHORT).show();
				mSavedExpression = null;
				return false;
			}

			// 解析できていたら代入
			if (parsed_expr != null) {
				double ans;
				try {
					ans = parsed_expr.evaluate(Double.valueOf(mAssign.getText().toString()));
				} catch (Exception e) {
					if (e.getMessage() != null)
						Log.d("parseExpression", e.getMessage());
					else
						Log.d("parseExpression", e.toString());
					if (toast)
						Toast
							.makeText(CalculatorActivity.this, "式、または代入値が無効です", Toast.LENGTH_SHORT)
							.show();
					mSavedExpression = null;
					return false;
				}
				mAnswer.setText(String.valueOf(ans));
			} else {
				if (toast)
					Toast.makeText(CalculatorActivity.this, "式が無効です", Toast.LENGTH_SHORT).show();
				mSavedExpression = null;
				return false;
			}

			// 解析完了かつ、有効な値が代入出来たらtrue
			mSavedExpression = parsed_expr;
			return true;
		}
	}

	/**
	 * グラフ描画ボタンの実装.
	 * 
	 * <pre>
	 * グラフ画面を開く
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class GraphButtonListener extends CalcButtonListener {

		@Override
		public void onClick(View v) {
			// 一つ目の式が解析可能なら
			if (super.parseExpression(mPrimaryExpression, true)) {
				// 解析した式を保存
				GrobalApplicationData.setExpression(mSavedExpression);

				// 二つ目は出来ようができまいが保存
				if (!super.parseExpression(mSecondaryExpression, false)
					&& mSecondaryExpression.getText().toString().length() != 0)
					Toast.makeText(CalculatorActivity.this, "二つ目の式は解析できませんでした\n一変数グラフになります",
						Toast.LENGTH_SHORT).show();
				GrobalApplicationData.setSecondExpression(mSavedExpression);

				showDialog(ID_SETTING_GRAPH_DIALOG);
			}
		}
	}

	/**
	 * ACの実装.
	 * 
	 * <pre>
	 * 文字列消去
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class AllCrearButtonListener implements View.OnClickListener {

		public void onClick(View v) {
			EditText edit = getCurrentEditTarget();
			edit.setText("");
		}
	}

	/**
	 * EditTextへの文字列の追加.
	 * 
	 * <pre>
	 * 指定した文字列を編集領域に指定されたViewの現在のカーソル位置へ挿入する
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class InsertButtonListener implements View.OnClickListener {

		private String str_;

		/**
		 * コンストラクタ.
		 * 
		 * @param <T>
		 *            数値型
		 * @param num
		 *            挿入する数値
		 */
		public <T> InsertButtonListener(T num) {
			str_ = String.valueOf(num);
		}

		/**
		 * コンストラクタ.
		 * 
		 * @param str
		 *            挿入する文字列
		 */
		public InsertButtonListener(String str) {
			str_ = str;
		}

		/**
		 * 文字列の設定.
		 * 
		 * @param str
		 *            挿入する文字列
		 */
		public void setString(String str) {
			str_ = str;
		}

		/**
		 * クリック時の挙動.
		 * 
		 * <pre>
		 * カーソル位置へ文字列を挿入する
		 * </pre>
		 * 
		 * @param v
		 *            コールバック元のView
		 */
		public void onClick(View v) {
			EditText edit = getCurrentEditTarget();

			// カーソル位置の取得
			int loc = edit.getSelectionStart();
			String prev_text = edit.getText().toString();

			// 文字列の挿入
			String prefix = prev_text.substring(0, loc);
			String suffix = loc < prev_text.length() ? prev_text.substring(loc, prev_text.length())
				: "";
			edit.setText(prefix + str_ + suffix);
			edit.setSelection(loc + str_.length());
		}
	}

	/**
	 * Ansの実装.
	 * 
	 * <pre>
	 * 解の引用
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class QuoteAnswerButtonListener extends InsertButtonListener {

		private QuoteAnswerButtonListener(String str) {
			super(str);
		}

		@Override
		public void onClick(View v) {
			super.setString(mAnswer.getText().toString());
			super.onClick(v);
		}
	}

	/**
	 * EditTextへの文字列と()の追加.
	 * 
	 * <pre>
	 * 指定した文字列を編集領域に指定されたViewの現在のカーソル位置へ挿入する
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class BracketInsertButtonListener extends InsertButtonListener {

		int n = 1;;

		public BracketInsertButtonListener(String str) {
			super(str + "()");
		}

		public BracketInsertButtonListener(String str, String bracket) {
			super(str + bracket);
		}

		public BracketInsertButtonListener(String str, String bracket, int n) {
			super(str + bracket);
			this.n = n;
		}

		@Override
		public void onClick(View v) {
			super.onClick(v);
			EditText edit = getCurrentEditTarget();
			edit.setSelection(edit.getSelectionStart() - n);
		}
	}

	/**
	 * ViewPagerへのアダプタ.
	 * 
	 * <pre>
	 * Pagerに表示するビューを管理する。
	 * </pre>
	 * 
	 * @author Yuu
	 * @version 0.1
	 * @since 0.1
	 */
	private class CalculatorAreaAdapter extends PagerAdapter {

		private static final int NUM_VIEW = 3;

		private LayoutInflater mInflater;
		private View mViewOfPage[] = new View[3];

		/**
		 * コンストラクタ.
		 * 
		 * @param context
		 *            Context
		 */
		public CalculatorAreaAdapter(final Context context) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * ビューの生成.
		 * 
		 * <pre>
		 * 現在のページのビューを生成する
		 * </pre>
		 * 
		 * @param collection
		 *            View
		 * @param position
		 *            現在のページ番号
		 * @return 現在のビュー
		 */
		@Override
		public Object instantiateItem(final View collection, final int position) {
			ViewPager pager = (ViewPager) collection;

			if (mViewOfPage[position] == null) {
				switch (position) {
				case 0:
					mViewOfPage[position] = mInflater.inflate(R.layout.page1, pager, false);
					initView1();
					break;
				case 1:
					mViewOfPage[position] = mInflater.inflate(R.layout.page2, pager, false);
					initView2();
					break;
				case 2:
					mViewOfPage[position] = mInflater.inflate(R.layout.page3, pager, false);
					initView3();
					break;
				}
			}
			pager.addView(mViewOfPage[position], 0);
			return mViewOfPage[position];
		}

		@Override
		public void destroyItem(final View collection, final int position, final Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public void finishUpdate(final View collection) {
		}

		@Override
		public int getCount() {
			return NUM_VIEW;
		}

		@Override
		public boolean isViewFromObject(final View view, final Object object) {
			return view == (View) object;
		}

		@Override
		public void restoreState(final Parcelable parcel, final ClassLoader classLoader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(final View collection) {
		}

		/**
		 * ページ1の初期化.
		 */
		private void initView1() {
			View currentView = mViewOfPage[0];
			Button button = (Button) currentView.findViewById(R.id.button_equal);
			button.setOnClickListener(new CalcButtonListener());

			// 数字ボタンの登録
			Resources res = getResources();
			String[] itemList = res.getStringArray(R.array.num_button);
			for (int i = 0; i < itemList.length; i++) {
				button = (Button) currentView.findViewById(res.getIdentifier(itemList[i], "id",
					getPackageName()));
				button.setOnClickListener(new InsertButtonListener(i));
			}

			button = (Button) currentView.findViewById(R.id.button_add);
			button.setOnClickListener(new InsertButtonListener("+"));
			button = (Button) currentView.findViewById(R.id.button_sub);
			button.setOnClickListener(new InsertButtonListener("-"));
			button = (Button) currentView.findViewById(R.id.button_mult);
			button.setOnClickListener(new InsertButtonListener("*"));
			button = (Button) currentView.findViewById(R.id.button_div);
			button.setOnClickListener(new InsertButtonListener("/"));

			button = (Button) currentView.findViewById(R.id.button_dot);
			button.setOnClickListener(new InsertButtonListener("."));
			button = (Button) currentView.findViewById(R.id.button_ac);
			button.setOnClickListener(new AllCrearButtonListener());
			button = (Button) currentView.findViewById(R.id.button_bracket);
			button.setOnClickListener(new BracketInsertButtonListener(""));
		}

		/**
		 * ページ2の初期化.
		 */
		private void initView2() {
			View currentView = mViewOfPage[1];

			Button button = (Button) currentView.findViewById(R.id.button_sin);
			button.setOnClickListener(new BracketInsertButtonListener("sin"));
			button = (Button) currentView.findViewById(R.id.button_cos);
			button.setOnClickListener(new BracketInsertButtonListener("cos"));
			button = (Button) currentView.findViewById(R.id.button_tan);
			button.setOnClickListener(new BracketInsertButtonListener("tan"));

			button = (Button) currentView.findViewById(R.id.button_pow);
			button.setOnClickListener(new InsertButtonListener("^"));
			button = (Button) currentView.findViewById(R.id.button_exp);
			button.setOnClickListener(new BracketInsertButtonListener("exp"));
			button = (Button) currentView.findViewById(R.id.button_root);
			button.setOnClickListener(new BracketInsertButtonListener("√"));

			button = (Button) currentView.findViewById(R.id.button_log);
			button.setOnClickListener(new BracketInsertButtonListener("log"));
			button = (Button) currentView.findViewById(R.id.button_ln);
			button.setOnClickListener(new BracketInsertButtonListener("ln"));
			button = (Button) currentView.findViewById(R.id.button_factorial);
			button.setOnClickListener(new InsertButtonListener("!"));

			button = (Button) currentView.findViewById(R.id.button_pi);
			button.setOnClickListener(new InsertButtonListener("π"));
			button = (Button) currentView.findViewById(R.id.button_E);
			button.setOnClickListener(new InsertButtonListener("E"));
			button = (Button) currentView.findViewById(R.id.button_x);
			button.setOnClickListener(new InsertButtonListener("x"));

			button = (Button) currentView.findViewById(R.id.button_ac);
			button.setOnClickListener(new AllCrearButtonListener());
			button = (Button) currentView.findViewById(R.id.button_useAns);
			button.setOnClickListener(new QuoteAnswerButtonListener(""));

			button = (Button) currentView.findViewById(R.id.button_graph);
			button.setOnClickListener(new GraphButtonListener());
		}

		/**
		 * ページ3の初期化.
		 */
		private void initView3() {
			View currentView = mViewOfPage[2];

			Button button = (Button) currentView.findViewById(R.id.button_sinh);
			button.setOnClickListener(new BracketInsertButtonListener("sinh"));
			button = (Button) currentView.findViewById(R.id.button_cosh);
			button.setOnClickListener(new BracketInsertButtonListener("cosh"));
			button = (Button) currentView.findViewById(R.id.button_tanh);
			button.setOnClickListener(new BracketInsertButtonListener("tanh"));

			button = (Button) currentView.findViewById(R.id.button_arcsin);
			button.setOnClickListener(new BracketInsertButtonListener("asin"));
			button = (Button) currentView.findViewById(R.id.button_arccos);
			button.setOnClickListener(new BracketInsertButtonListener("acos"));
			button = (Button) currentView.findViewById(R.id.button_arctan);
			button.setOnClickListener(new BracketInsertButtonListener("atan"));

			button = (Button) currentView.findViewById(R.id.button_abs);
			button.setOnClickListener(new BracketInsertButtonListener("", "||"));
			button = (Button) currentView.findViewById(R.id.button_max);
			button.setOnClickListener(new BracketInsertButtonListener("max", "(,)", 2));
			button = (Button) currentView.findViewById(R.id.button_min);
			button.setOnClickListener(new BracketInsertButtonListener("min", "(,)", 2));

			button = (Button) currentView.findViewById(R.id.button_rad);
			button.setOnClickListener(new BracketInsertButtonListener("Radians"));
			button = (Button) currentView.findViewById(R.id.button_degree);
			button.setOnClickListener(new BracketInsertButtonListener("Degrees"));
			button = (Button) currentView.findViewById(R.id.button_x);
			button.setOnClickListener(new InsertButtonListener("x"));

			button = (Button) currentView.findViewById(R.id.button_ac);
			button.setOnClickListener(new AllCrearButtonListener());
			button = (Button) currentView.findViewById(R.id.button_useAns);
			button.setOnClickListener(new QuoteAnswerButtonListener(""));

			button = (Button) currentView.findViewById(R.id.button_graph);
			button.setOnClickListener(new GraphButtonListener());
		}
	}
}