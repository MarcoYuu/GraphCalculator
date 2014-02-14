package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Log10 extends Function{
	public Log10(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.log10(argument().evaluate(x));
	}
	public static  Expression create(Expression argument){
		return new Log10(argument);
	}
}