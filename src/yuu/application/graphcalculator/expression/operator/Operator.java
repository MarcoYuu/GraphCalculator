package yuu.application.graphcalculator.expression.operator;

import yuu.application.graphcalculator.expression.Expression;

public abstract class Operator implements Expression {

	private Expression lhs =null;
	private Expression rhs =null;

	public Operator(Expression lhs, Expression rhs){
		if(lhs == null || rhs == null)
			throw new OperatorArgmentException();
		this.lhs =lhs;
		this.rhs =rhs;
	}
	public abstract double evaluate(double x);

	protected Expression lhs(){
		return lhs;
	}
	protected Expression rhs(){
		return rhs;
	}

	static public class OperatorArgmentException extends ExpressionException{
		private static final long serialVersionUID = -7814919260174797739L;
		OperatorArgmentException() {
			super("Argument of lhs or rhs is null.Please set valid value.");
		}
	}
}
