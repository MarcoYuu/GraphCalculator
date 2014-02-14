package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Power extends Function{
	public Power(Expression base,  Expression exponent){
		super(base,exponent);
	}
	public double evaluate(double x) {
		return Math.pow(argument().evaluate(x),argument2().evaluate(x));
	}
	public static Expression create(Expression base, Expression exponent){
		return new Power(base,exponent);
	}
}