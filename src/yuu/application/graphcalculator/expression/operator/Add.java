package yuu.application.graphcalculator.expression.operator;

import yuu.application.graphcalculator.expression.Expression;

public class Add extends Operator{
	public Add(Expression lhs, Expression rhs){
		super(lhs,rhs);
	}
	public double evaluate(double x) {
		return lhs().evaluate(x)+rhs().evaluate(x);
	}
	public static Expression create(Expression lhs, Expression rhs){
		return new Add(lhs,rhs);
	}
}