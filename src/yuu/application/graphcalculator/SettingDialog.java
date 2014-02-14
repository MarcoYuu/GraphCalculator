package yuu.application.graphcalculator;

import yuu.application.graphcalculator.GraphView.GraphMode;
import yuu.application.graphcalculator.GrobalApplicationData.GraphData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingDialog extends Dialog {

	private Activity mCallerActivity;

	private Point mWindowSize = new Point();
	private EditText mMinLimit;
	private EditText mMaxLimit;
	private EditText mMinLimitAxis;
	private EditText mMaxLimitAxis;
	private EditText mScaleY;
	private EditText mInfinity;
	private Spinner mLineWidth;
	private Spinner mStride;
	private CheckBox mAntiAlias;

	private static int mLineWidthNum;
	private static int mStrideNum;

	public SettingDialog(Activity context) {
		super(context);

		mCallerActivity = context;
		Display disp = ((WindowManager) mCallerActivity.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		mWindowSize.set(disp.getWidth(), disp.getHeight());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graph_setting);

		initWidget();

		importGraphData();
		exportGraphData();

		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				exportGraphData();
				mCallerActivity = null;
			}
		});

		Button button = (Button) findViewById(R.id.draw_graph);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ダイアログ終了
				dismiss();

				// 設定の登録
				try {
					exportGraphData();
				} catch (Exception e) {
					Toast.makeText(mCallerActivity, "無効な設定があります", Toast.LENGTH_SHORT).show();
					return;
				}

				// インテントの作成
				Intent intent = new Intent(mCallerActivity, GraphActivity.class);

				// 画面遷移
				mCallerActivity.startActivity(intent);
				mCallerActivity = null;
			}
		});
	}

	private void initWidget() {
		mMinLimit = (EditText) findViewById(R.id.min_limit);
		mMaxLimit = (EditText) findViewById(R.id.max_limit);
		mMinLimitAxis = (EditText) findViewById(R.id.min_limit_axis);
		mMaxLimitAxis = (EditText) findViewById(R.id.max_limit_axis);
		mInfinity = (EditText) findViewById(R.id.infinity);
		mScaleY = (EditText) findViewById(R.id.scale_y);

		mMaxLimit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (GrobalApplicationData.getSecondExpression() == null)
					mMaxLimitAxis.setText(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mMinLimit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (GrobalApplicationData.getSecondExpression() == null)
					mMinLimitAxis.setText(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mStride = (Spinner) findViewById(R.id.stride);
		mLineWidth = (Spinner) findViewById(R.id.line_width);
		mAntiAlias = (CheckBox) findViewById(R.id.antialias);
	}

	/**
	 * 設定を書き戻す
	 */
	private void importGraphData() {
		GraphData data = GrobalApplicationData.getGraphData();
		if (data == null)
			return;

		mMinLimit.setText(String.valueOf(data.start));
		mMaxLimit.setText(String.valueOf(data.end));
		mMinLimitAxis.setText(String.valueOf(data.min_axis));
		mMaxLimitAxis.setText(String.valueOf(data.max_axis));
		mInfinity.setText(String.valueOf(data.infinity));
		mScaleY.setText(String.valueOf(data.scale_y));

		mStride.setSelection(mStrideNum);
		mLineWidth.setSelection(mLineWidthNum);

		mAntiAlias.setChecked(data.anti_alias);
	}

	/**
	 * データを登録する
	 */
	private void exportGraphData() {
		GraphData data = GrobalApplicationData.getGraphData();
		if (data == null)
			data = new GraphData();

		setGraphData(data);
		GrobalApplicationData.setGraphData(data);
	}

	/**
	 * データ内容の読み取り
	 *
	 * @param data
	 */
	private void setGraphData(GraphData data) {
		data.mode = GraphMode.BITMAP;
		data.window_width = mWindowSize.x;
		data.window_height = mWindowSize.y;

		data.min_axis = Float.valueOf(mMinLimitAxis.getText().toString());
		data.max_axis = Float.valueOf(mMaxLimitAxis.getText().toString());
		data.start = Float.valueOf(mMinLimit.getText().toString());
		data.end = Float.valueOf(mMaxLimit.getText().toString());
		data.infinity = Float.valueOf(mInfinity.getText().toString());
		data.scale_y = Float.valueOf(mScaleY.getText().toString());

		data.stride = Float.valueOf((String) mStride.getSelectedItem());
		data.line_width = Float.valueOf((String) mLineWidth.getSelectedItem());
		mStrideNum = mStride.getSelectedItemPosition();
		mLineWidthNum = mLineWidth.getSelectedItemPosition();

		data.anti_alias = mAntiAlias.isChecked();

		RadioButton radio = (RadioButton) findViewById(R.id.is_save);
		data.save_graph = radio.isChecked();
		radio = (RadioButton) findViewById(R.id.save_png);
		data.save_as_png = radio.isChecked();
	}
}
