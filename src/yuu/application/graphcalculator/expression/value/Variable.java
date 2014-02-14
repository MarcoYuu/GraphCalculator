package yuu.application.graphcalculator.expression.value;

import yuu.application.graphcalculator.expression.Expression;

public class Variable extends Value{
	public double evaluate(double x) {
		return x;
	}
	public static Expression create(){
		return new Variable();
	}
}