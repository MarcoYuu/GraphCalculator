package yuu.application.graphcalculator.expression;

/**
 * 式インタフェース.
 *
 * <pre>評価可能な式を表す</pre>
 *
 * @author Yuu
 * @version 0.1
 * @since 0.1
 */
public interface Expression {
	/**
	 * @param x 変数への代入値
	 * @return 評価値
	 */
	public double evaluate(double x);

	static public class ExpressionException extends RuntimeException{
		private static final long serialVersionUID = -291110325683539869L;

		protected ExpressionException(String str){
			super(str);
		}
	}
}
