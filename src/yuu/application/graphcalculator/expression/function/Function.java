package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public abstract class Function implements Expression {

	private Expression mArgument1 =null;
	private Expression mArgument2 =null;

	public Function(Expression argument1){
		if(argument1 == null)
			throw new FunctionArgmentException("Argument is null.");
		mArgument1 =argument1;
	}

	public Function(Expression argument1, Expression argument2){
		if(argument1 == null || argument2 == null)
			throw new FunctionArgmentException("Argument of lhs or rhs is null.");

		mArgument1 =argument1;
		mArgument2 =argument2;
	}

	protected Expression argument(){
		return mArgument1;
	}

	protected Expression argument2(){
		return mArgument2;
	}

	static public class FunctionArgmentException extends ExpressionException{
		private static final long serialVersionUID = -4966559621099438184L;
		FunctionArgmentException(String str) {
			super(str+"Please set valid value.");
		}
	}
}
