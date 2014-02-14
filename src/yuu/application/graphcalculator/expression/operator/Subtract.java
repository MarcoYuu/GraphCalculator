package yuu.application.graphcalculator.expression.operator;

import yuu.application.graphcalculator.expression.Expression;

public class Subtract extends Operator{
	public Subtract(Expression lhs, Expression rhs){
		super(lhs,rhs);
	}
	public double evaluate(double x) {
		return lhs().evaluate(x)-rhs().evaluate(x);
	}
	public static Expression create(Expression lhs, Expression rhs){
		return new Subtract(lhs,rhs);
	}
}