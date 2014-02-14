package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Factorial extends Function{
	public Factorial(Expression argument) {
		super(argument);		
	}
	public double evaluate(double x) {
		double value =argument().evaluate(x);
		
		//引数が正か0のとき
		if(value >= 0.0){
			int floored_value =(int) Math.floor(value);
			//整数のとき
			if(value-floored_value == 0){
				return factorial(floored_value);
			}
			//0.5刻みの時
			else if(value-floored_value == 0.5){
				floored_value +=1;
				return factorial(2*floored_value)*Math.sqrt(Math.PI)
						/ (Math.pow(2.0, 2.0*floored_value)*factorial(floored_value));
			}
			//それ以外(スターリングの計算器向け近似
			value +=1;
			return Math.sqrt( (2*Math.PI)/value )
					* Math.pow( (value/Math.E) * Math.sqrt( (value*Math.sinh(1/value)) + (1/(810*Math.pow(value,6))) ), value );
		}
		else{
			value +=1;
			return gamma(value);
		}
	}
	public static Expression create(Expression argument){
		return new Factorial(argument);
	}
	
	public static double factorial(int x){
		double ans =1;
		for(int i=1;i<=x;++i)
			ans *=i;
		return ans;
	}
	
	public static double gamma(double x){
		if(x < 0)
			return Math.PI/(Math.sin(Math.PI*x)*Math.exp(log_gamma(1-x)));
		return Math.exp(log_gamma(x));
	}
	
	@SuppressWarnings("unused")
	private static double log_gamma(double x){
		//c.f http://d.hatena.ne.jp/gerumanium/20110618/1308370946
		final int N =8;
		final double B0 = 1;
		final double B1 = (-1.0 / 2.0);
		final double B2 = (1.0 / 6.0);
		final double B4 = (1.0 / 30.0);
		final double B6 = (1.0 / 42.0);
		final double B8 = (-1.0 / 30.0);
		final double B10 = (5.0 / 66.0);
		final double B12 = (-691.0 / 2730.0);
		final double B14 = (7.0 / 6.0);
		final double B16 = (-3617.0 / 510.0);
		final double B18 = (43867.0 / 798.0);
		final double B20 = (-174611.0 / 330.0);
		
		double v = 1;
		while (x < N) {
			v *= x;
			x += 1;
		}
		double w = 1 / (x*x);
		return 0.5 *Math.log(2*Math.PI)-Math.log(v)-x+(x - 0.5)*Math.log(x)
				+(((((((((B16/(16*15))*w + (B14/(14*13)))*w + (B12/(12*11)))*w + (B10*9)))*w + (B8/(8*7)))*w + (B6/(6*5)))*w + (B4/(4*3)))*w + (B2/(2*1)))/x;
	}
}