package yuu.application.graphcalculator.graph;

import yuu.application.graphcalculator.expression.Expression;
import android.graphics.Matrix;
import android.graphics.Path;

public abstract class GraphBuilder {

	//描画領域の高さと幅(px)
	protected int mWidth =480;
	protected int mHeight =800;
	
	//解析範囲と描画領域の比
	protected float mRatio =240.0f;
	
	//解析範囲と分割幅
	protected float mStart =-10;
	protected float mEnd =10;
	protected float mStride =0.00001f;
	
	//軸の範囲
	protected float mAxisXMin =-10;
	protected float mAxisXMax =10;
	protected float mAxisYMin =-10;
	protected float mAxisYMax =10;
	
	//無限とみなす値
	protected double mInfinity =10e16;
	//y軸拡大率
	protected double mScaleAxisY =1.0f;
	
	//描画対象グラフの式
	protected Expression mExpression = null;
	protected Expression mParameterX;
	protected Expression mParameterY;

	public abstract Graph createGraph();
	public abstract Graph createGraph(int type);
	
	public GraphBuilder setScaleAxisY(double scale){
		mScaleAxisY =scale;
		return this;
	}
	
	public GraphBuilder setAnalyzeRange(float start, float end, float stride){
		mStart =start;
		mEnd =end;
		mStride =stride;
		
		updateRatio();
		return this;
	}
	public GraphBuilder setAxisRange(float min, float max){
		mAxisXMin =min;
		mAxisXMax =max;
		
		mAxisYMax =(mAxisXMax-mAxisXMin)*getAspectRatio()/2.0f;
		mAxisYMin =-mAxisYMax;
		
		updateRatio();
		return this;
	}
	
	public GraphBuilder setScreenSize(int width, int height){
		mWidth =width;
		mHeight =height;
		
		updateRatio();
		return this;
	}
	
	public GraphBuilder setAnalyzeRangeAndScreenSize(
			int start, int end, float stride, int width, int height)
	{
		setScreenSize(width, height);
		setAnalyzeRange(start, end, stride);
		return this;
	}
	
	public GraphBuilder setValueAsPositiveInfinity(double value){
		mInfinity =value;
		return this;
	}

	public GraphBuilder setExpression(Expression expression) {
		mExpression =expression;
		return this;
	}
	
	public GraphBuilder setParametricExpression(Expression x, Expression y) {
		mParameterX =x;
		mParameterY =y;
		return this;
	}
	
	public float getAspectRatio() {
		if(mHeight>mWidth)
			return (float)mHeight/(float)mWidth;
		else
			return (float)mWidth/(float)mHeight;
	}
	
	protected float[] makeCoordinateArray() {
		float[] points =new float[(int) ((mEnd-mStart)/mStride)*2+2];
		for(int i=0;i<points.length/2;++i){
			points[i*2] =mStart +mStride*i;
			points[i*2+1] =-(float)(mExpression.evaluate(points[i*2])*mScaleAxisY);
		}
		return points;
	}
	
	protected float[] makeParametricCoordinateArray() {
		float[] points =new float[(int) ((mEnd-mStart)/mStride)*2];
		for(int i=0;i<points.length/2;++i){
			double t =mStart +mStride*i;
			points[i*2] =(float)(mParameterX.evaluate(t));
			points[i*2+1] =-(float)(mParameterY.evaluate(t)*mScaleAxisY);
		}
		return points;
	}
	
	protected Path makeGraphPath(float[] coordArray) {
		int i;
		Path path =new Path();		
		for(i=0;i<coordArray.length/2;){	
			//始点の決定
			for(;i<coordArray.length/2;++i){
				if(!Float.isNaN(coordArray[i*2+1])){
					if(isPositiveInfinity(coordArray[i*2+1])){
						path.moveTo(coordArray[i*2], (float) mInfinity);
					}else if(isNegativeInfinity(coordArray[i*2+1])){
						path.moveTo(coordArray[i*2], (float) -mInfinity);
					}else{
						path.moveTo(coordArray[i*2], coordArray[i*2+1]);
					}
					++i;
					break;
				}
			}
			//無限大か非数ではパスを切る
			for(;i<coordArray.length/2;++i){
				if(Float.isNaN(coordArray[i*2+1])){
					break;
				}else if(isPositiveInfinity(coordArray[i*2+1])){
					if(!isNegativeInfinity(coordArray[(i-1)*2+1]))
						path.lineTo(coordArray[i*2],(float) mInfinity);
					break;
				}else if(isNegativeInfinity(coordArray[i*2+1])){
					if(!isPositiveInfinity(coordArray[(i-1)*2+1]))
						path.lineTo(coordArray[i*2],(float) -mInfinity);
					break;
				}else{
					path.lineTo(coordArray[i*2], coordArray[i*2+1]);
				}
			}
		}
		
		fitPathToScreen(path);
	
		return path;
	}
	protected Path makeAxisPath() {
		Path Axis =new Path();
		
		Axis.moveTo(mAxisXMin, 0);
		Axis.lineTo(mAxisXMax, 0);
		Axis.moveTo(0, mAxisYMin);
		Axis.lineTo(0, mAxisYMax);
		
		fitPathToScreen(Axis);
		
		return Axis;
	}
	
	protected Path makeGridPath(float stride) {
		Path Grid =new Path();

		int elementNum =(int) (mAxisXMax/stride)+1;
		for(int i =0;i<elementNum;++i){
			float cur_pos =stride*i;
			if(cur_pos<mAxisXMin)
				continue;

			Grid.moveTo(cur_pos, mAxisYMin);
			Grid.lineTo(cur_pos, mAxisYMax);
		}
		elementNum =(int) (mAxisXMin/stride)-1;
		for(int i =0;i>elementNum;--i){
			float cur_pos =stride*i;
			if(cur_pos>mAxisXMax)
				continue;
			
			Grid.moveTo(cur_pos, mAxisYMin);
			Grid.lineTo(cur_pos, mAxisYMax);
		}
		
		elementNum =(int) (mAxisYMax/(stride*mScaleAxisY))+1;
		for(int i =0;i<elementNum;++i){
			float cur_pos =(float) (stride*i*mScaleAxisY);
			if(cur_pos<mAxisYMin)
				continue;

			Grid.moveTo(mAxisXMin, cur_pos);
			Grid.lineTo(mAxisXMax, cur_pos);
		}
		elementNum =(int) (mAxisYMin/(stride*mScaleAxisY))-1;
		for(int i =0;i>elementNum;--i){
			float cur_pos =(float) (stride*i*mScaleAxisY);
			if(cur_pos>mAxisYMax)
				continue;
			
			Grid.moveTo(mAxisXMin, cur_pos);
			Grid.lineTo(mAxisXMax, cur_pos);
		}
			
		fitPathToScreen(Grid);
		
		return Grid;
	}
	
	/**
	 * 
	 */
	private void updateRatio() {
		mRatio =(float)mWidth/(mAxisXMax-mAxisXMin);
	}
	/**
	 * @param path
	 */
	private void fitPathToScreen(Path path) {
		Matrix mat =new Matrix();
		mat.setScale(mRatio, mRatio, mWidth/2.0f, mHeight/2.0f);
		mat.postTranslate(mRatio*((mAxisXMax-mAxisXMin)/2.0f-mAxisXMax),0);
		path.offset(mWidth/2.0f, mHeight/2.0f);
		path.transform(mat);
	}
	
	private boolean isPositiveInfinity(float value){
		return value > mInfinity;
	}
	
	private boolean isNegativeInfinity(float value){
		return value < -mInfinity;
	}
}