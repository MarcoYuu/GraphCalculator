package yuu.application.graphcalculator;

import yuu.application.graphcalculator.GraphView.GraphMode;
import yuu.application.graphcalculator.GrobalApplicationData.GraphData;
import yuu.application.graphcalculator.graph.DefaultBitmapGraph;
import yuu.application.graphcalculator.util.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GraphActivity extends Activity implements Runnable {

	private static final int ID_PROGRESS_DIALOG = 0;

	private GraphView mView = null;
	private ProgressDialog mProgressDialog = null;
	private Handler mHandler = new Handler();
	private Thread mGraphThread = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ビューの初期化
		mView = new GraphView(this);

		// タイトルバー消去・フルスクリーン・回転抑止
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 画面をアクティビティ起動時の向きで固定
		Utils.controlOrientationFix(this, true);

		showDialog(ID_PROGRESS_DIALOG);

		mGraphThread = new Thread(this);
		mGraphThread.start();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		mProgressDialog = new ProgressDialog(GraphActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("グラフデータの作成中");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				setContentView(mView);
			}
		});

		return mProgressDialog;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mGraphThread != null) {
			try {
				mGraphThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// GCのためにグラフオブジェクトの取得
		DefaultBitmapGraph graph = (DefaultBitmapGraph) mView.getGraph();
		if (mView.getGraphMode() == GraphMode.BITMAP)
			graph = (DefaultBitmapGraph) mView.getGraph();

		mView = null;
		if (graph != null)
			graph.refreshBitmap();
	}

	@Override
	public void run() {
		// 保存データの取得
		GraphData data = GrobalApplicationData.getGraphData();

		// ビューの初期化(グラフの作成)
		mView.initByDataStruct(data);

		// グラフの保存
		if (data.save_graph) {
			mHandler.post(new Runnable() {

				public void run() {
					mProgressDialog.setMessage("グラフの保存中...");
				}
			});
			Log.d("thread", "グラフの保存中");
			mView.saveGraphToSD(data.save_as_png);
			Log.d("thread", "グラフの保存の完了");
		}
		dismissDialog(ID_PROGRESS_DIALOG);
		mProgressDialog = null;
	}
}
