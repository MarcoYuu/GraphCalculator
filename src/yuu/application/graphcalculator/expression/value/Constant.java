package yuu.application.graphcalculator.expression.value;

import yuu.application.graphcalculator.expression.Expression;

public class Constant extends Value{
	final double mValue;

	public Constant(double value){
		mValue =value;
	}
	public double evaluate(double x) {
		return mValue;
	}
	public static Expression create(double x){
		return new Constant(x);
	}
	
	public static final Expression E =new Constant(Math.E);
	public static final Expression PI =new Constant(Math.PI);
}