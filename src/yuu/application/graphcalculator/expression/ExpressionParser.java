package yuu.application.graphcalculator.expression;

import java.util.StringTokenizer;

import yuu.application.graphcalculator.expression.function.Absolute;
import yuu.application.graphcalculator.expression.function.ArcCos;
import yuu.application.graphcalculator.expression.function.ArcSin;
import yuu.application.graphcalculator.expression.function.ArcTan;
import yuu.application.graphcalculator.expression.function.Cos;
import yuu.application.graphcalculator.expression.function.Cosh;
import yuu.application.graphcalculator.expression.function.Exp;
import yuu.application.graphcalculator.expression.function.Factorial;
import yuu.application.graphcalculator.expression.function.Log;
import yuu.application.graphcalculator.expression.function.Log10;
import yuu.application.graphcalculator.expression.function.LogE;
import yuu.application.graphcalculator.expression.function.Max;
import yuu.application.graphcalculator.expression.function.Min;
import yuu.application.graphcalculator.expression.function.Negater;
import yuu.application.graphcalculator.expression.function.Power;
import yuu.application.graphcalculator.expression.function.Root;
import yuu.application.graphcalculator.expression.function.Sin;
import yuu.application.graphcalculator.expression.function.Sinh;
import yuu.application.graphcalculator.expression.function.Tan;
import yuu.application.graphcalculator.expression.function.Tanh;
import yuu.application.graphcalculator.expression.function.ToDegrees;
import yuu.application.graphcalculator.expression.function.ToRadians;
import yuu.application.graphcalculator.expression.operator.Add;
import yuu.application.graphcalculator.expression.operator.Divide;
import yuu.application.graphcalculator.expression.operator.Multiply;
import yuu.application.graphcalculator.expression.operator.Subtract;
import yuu.application.graphcalculator.expression.value.Constant;
import yuu.application.graphcalculator.expression.value.Variable;

//c.f http://www.tuat.ac.jp/~tuatmcc/contents/monthly/200206/nuki.xml

/**
 * 式構文解析機.
 *
 * <pre>
 * 与えられた文字列を数式として解析し、評価可能な式を作り出せる。</br>
 * コンストラクタかメソッドで解析する文字列を指定したのち、parse()で式を取り出す。</br>
 * </br>
 * 使える記号は</br>
 * 「*,/,+,/,-,^,.,(,),|,!」</br>
 * 関数として一引数のものが、</br>
 * 「sin,cos,tan,√,exp,ln,log,sinh,cosh,tanh,asin,acos,atan,Radians,Degrees」</br>
 * 二引数のものが</br>
 * 「max,min」</br>
 * 定数として</br>
 * 「π,E」</br>
 * 変数として</br>
 * 「x」</br>
 * がある。関数は必ず()ないし||で引数が指定されている必要がある。</br>
 * そのほか、階乗などは整数値しか取れないなどの制約がいくつかある。</br>
 * </pre>
 *
 * @author Yuu
 * @version 0.1
 * @since 0.1
 */
public class ExpressionParser {
	private String mExpression ="";
	private int mIndex =0;

	/**
	 * コンストラクタ.
	 */
	public ExpressionParser(){
	}

	/**
	 * コンストラクタ.
	 * @param expression 解析する式
	 */
	public ExpressionParser(String expression){
		mExpression =expression;
	}


	/**
	 * 式の登録.
	 * @param expression 解析する式
	 */
	public void setExpression(String expression){
		mExpression =expression;
	}

	/**
	 * 式の解析.
	 * @return 解析された、評価可能な式
	 * @exception 構文にあっていない文字列を解析した時例外を送出する
	 */
	public Expression parse(){
		mIndex =0;
		return analyzeAddition();
	}

	private Expression analyzeAddition(){
		Expression expr =analyzeMultiplication();
		if(mIndex >=mExpression.length())
			return expr;
		for(;;){
			if(mExpression.charAt(mIndex) == '+'){
				++mIndex;
				expr =Add.create(expr,analyzeMultiplication());
			}else if(mExpression.charAt(mIndex) == '-') {
				++mIndex;
				expr =Subtract.create(expr,analyzeMultiplication());
			}else{
				break;
			}
			if(mIndex>=mExpression.length())
				return expr;
		}
	    return expr;
	}

	private Expression analyzeMultiplication(){
		Expression expr = analyzeBracket();
		if(mIndex>=mExpression.length())
			return expr;
		for(;;){
			if(mExpression.charAt(mIndex) == '*') {
				++mIndex;
				expr =Multiply.create(expr,analyzeBracket());
			}else if(mExpression.charAt(mIndex) == '/') {
				++mIndex;
				expr =Divide.create(expr,analyzeBracket());
			}else{
				break;
			}
			if(mIndex>=mExpression.length())
				return expr;
		}
		return expr;
	}

	private Expression analyzeBracket(){
		if(mIndex>=mExpression.length())
			return null;
		if(mExpression.charAt(mIndex) == '('){
			++mIndex;

			Expression expr =analyzeAddition();
			if(mIndex>=mExpression.length())
				return expr;
			if(mExpression.charAt(mIndex) != ')'){
				return null;
			}

			++mIndex;

			if(mIndex>=mExpression.length())
				return expr;

			if(mExpression.charAt(mIndex) == '^'){
				++mIndex;
				Expression expr2 =analyzeBracket();
				return Power.create(expr,expr2);
			}else if(mExpression.charAt(mIndex) == '!'){
				++mIndex;
				return Factorial.create(expr);
			}else{
				return expr;
			}
		}else if(mExpression.charAt(mIndex) == '|'){
			++mIndex;

			Expression expr =analyzeAddition();
			if(mIndex>=mExpression.length())
				return Absolute.create(expr);
			if(mExpression.charAt(mIndex) != '|'){
				return null;
			}

			++mIndex;

			if(mIndex>=mExpression.length())
				return Absolute.create(expr);

			if(mExpression.charAt(mIndex) == '^'){
				++mIndex;
				Expression expr2 =analyzeBracket();
				return Power.create(Absolute.create(expr),expr2);
			}else if(mExpression.charAt(mIndex) == '!'){
				++mIndex;
				return Factorial.create(Absolute.create(expr));
			}else{
				return Absolute.create(expr);
			}
		}else{
			Expression expr =analyzeFunction();

			if(mIndex>=mExpression.length())
				return expr;

			if(mExpression.charAt(mIndex) == '^'){
				++mIndex;
				Expression expr2 =analyzeBracket();
				return Power.create(expr,expr2);
			}else if(mExpression.charAt(mIndex) == '!'){
				++mIndex;
				return Factorial.create(expr);
			}else{
				return expr;
			}
		}
	}

	private Expression analyzeFunction(){
		if(Character.isDigit((mExpression.charAt(mIndex)))){
			StringTokenizer sub_expr =new StringTokenizer(mExpression.substring(mIndex));
			String base =sub_expr.nextToken("()+-*/^!|,");
			double constant =Double.parseDouble(base);
			mIndex +=base.length();

			return Constant.create(constant);
		}else{
			switch(mExpression.charAt(mIndex))
			{
			case 'π':{++mIndex;return Constant.PI;}
			case 'E':{++mIndex;return Constant.E;}
			case 'x':{++mIndex;return Variable.create();}//variable
			case 's':{return createTrigFunc("sin");}//sin,sinh
			case 'c':{return createTrigFunc("cos");}//cos,cosh
			case 't':{return createTrigFunc("tan");}//tan,tanh
			case 'a':{return createArcTrigFunc();}//asin,atan,acos
			case '√':{return createRootFunc();}//root
			case 'l':{return createLogFunc();}//ln,log10
			case 'e':{return createExponentialFunc();}//exp
			case 'm':{return createMaxMinFunc();}//max,min
			case '-':{return createNegater();}//exp
			case 'R':{mIndex+=7;return ToRadians.create(analyzeBracket());}//Radians
			case 'D':{mIndex+=7;return ToDegrees.create(analyzeBracket());}//Degrees
			default:{return null;}
			}
		}
	}

	private Expression createMaxMinFunc() {
		++mIndex;
		char second =mExpression.charAt(mIndex);

		mIndex+=2;
		if(mExpression.charAt(mIndex) != '(')
			return null;
		++mIndex;
		Expression expr =analyzeAddition();

		if(mExpression.charAt(mIndex) != ',')
			return null;
		++mIndex;
		Expression expr2 =analyzeAddition();

		if(mExpression.charAt(mIndex) != ')')
			return null;
		if(mIndex<mExpression.length())
			++mIndex;

		if(second == 'a')
			return Max.create(expr,expr2);
		else
			return Min.create(expr,expr2);
	}

	private Expression createNegater() {
		++mIndex;
		Expression expr =analyzeBracket();
		return Negater.create(expr);
	}

	private Expression createExponentialFunc() {
		mIndex+=3;
		Expression expr =analyzeBracket();
		return Exp.create(expr);
	}

	private Expression createRootFunc() {
		++mIndex;
		Expression expr =analyzeBracket();
		return Root.create(expr);
	}

	private Expression createLogFunc(){
		++mIndex;
		if(mExpression.charAt(mIndex) == 'n'){
			++mIndex;
			Expression expr =analyzeBracket();
			return LogE.create(expr);

		}else if(mExpression.charAt(mIndex) == 'o'){//logN(
			mIndex+=2;
			if(Character.isDigit((mExpression.charAt(mIndex)))){
				StringTokenizer sub_expr =new StringTokenizer(mExpression.substring(mIndex));
				String base =sub_expr.nextToken("(|");
				double constant =Double.parseDouble(base);
				mIndex +=base.length();

				Expression expr =analyzeBracket();

				if(constant == 10.0){
					return Log10.create(expr);
				}else{
					return Log.create(Constant.create(constant), expr);
				}
			}if(mExpression.charAt(mIndex) =='(' || mExpression.charAt(mIndex) =='|'){
				Expression expr =analyzeBracket();
				return Log10.create(expr);
			}if(mExpression.charAt(mIndex) =='E'){
				Expression expr =analyzeBracket();
				return LogE.create(expr);
			}if(mExpression.charAt(mIndex) =='π'){
				Expression expr =analyzeBracket();
				return Log.create(Constant.PI, expr);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	private Expression createTrigFunc(String type){
		mIndex+=3;
		if(mExpression.charAt(mIndex) == 'h'){
			++mIndex;
			if(mExpression.charAt(mIndex) == '^'){
				++mIndex;
				Expression expr2 =analyzeBracket();
				Expression expr =analyzeBracket();

				if(type.equals("sin")){
					return Power.create(Sinh.create(expr), expr2);
				}else if(type.equals("cos")){
					return Power.create(Cosh.create(expr), expr2);
				}else if(type.equals("tan")){
					return Power.create(Tanh.create(expr), expr2);
				}
			}else{
				Expression expr =analyzeBracket();

				if(type.equals("sin")){
					return Sinh.create(expr);
				}else if(type.equals("cos")){
					return Cosh.create(expr);
				}else if(type.equals("tan")){
					return Tanh.create(expr);
				}
			}
		}else if(mExpression.charAt(mIndex) == '^'){
			++mIndex;
			Expression expr2 =analyzeBracket();
			Expression expr =analyzeBracket();

			if(type.equals("sin")){
				return Power.create(Sin.create(expr), expr2);
			}else if(type.equals("cos")){
				return Power.create(Cos.create(expr), expr2);
			}else if(type.equals("tan")){
				return Power.create(Tan.create(expr), expr2);
			}
		}else{
			Expression expr =analyzeBracket();

			if(type.equals("sin")){
				return Sin.create(expr);
			}else if(type.equals("cos")){
				return Cos.create(expr);
			}else if(type.equals("tan")){
				return Tan.create(expr);
			}
		}
		return null;
	}

	private Expression createArcTrigFunc(){
		++mIndex;
		String type;
		switch(mExpression.charAt(mIndex))
		{
		case 's':{type ="sin"; break;}//sin
		case 'c':{type ="cos"; break;}//cos
		case 't':{type ="tan"; break;}//tan
		default:{return null;}
		}

		mIndex +=3;
		if(mExpression.charAt(mIndex) == '^'){
			++mIndex;
			Expression expr2 =analyzeBracket();
			Expression expr =analyzeBracket();

			if(type.equals("sin")){
				return Power.create(ArcSin.create(expr), expr2);
			}else if(type.equals("cos")){
				return Power.create(ArcCos.create(expr), expr2);
			}else if(type.equals("tan")){
				return Power.create(ArcTan.create(expr), expr2);
			}
		}else{
			Expression expr =analyzeBracket();

			if(type.equals("sin")){
				return ArcSin.create(expr);
			}else if(type.equals("cos")){
				return ArcCos.create(expr);
			}else if(type.equals("tan")){
				return ArcTan.create(expr);
			}
		}
		return null;
	}
}
