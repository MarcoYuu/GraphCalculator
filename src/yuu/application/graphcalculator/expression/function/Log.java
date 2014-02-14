package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Log extends Function{
	public Log(Expression base, Expression antilogarithm){
		super(base,antilogarithm);
	}
	public double evaluate(double x) {
		return Math.log(argument2().evaluate(x))/Math.log(argument().evaluate(x));
	}
	public static  Expression create(Expression base,  Expression antilogarithm){
		return new Log(base,antilogarithm);
	}
}