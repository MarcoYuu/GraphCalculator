package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class LogE extends Function{
	public LogE(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.log(argument().evaluate(x));
	}
	public static  Expression create(Expression argument){
		return new LogE(argument);
	}
}