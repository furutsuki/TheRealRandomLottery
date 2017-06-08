package co.jp.applet;

import java.applet.Applet;
import java.awt.Graphics;
import java.util.Date;
import java.util.Random;

public class EvaluateAlgorithmRandom extends Applet {

	private static final long serialVersionUID = 1L;

	public  void paint(Graphics g){
		
		int NUMBER = 10000;         /* 発生させる乱数の数 */
		int j;                      /* forのカウンタ */ 
		int i;                      /* 0.1ごとのキザミの番号 */
		int f[] = new int [36];    /* キザミiに落ちる乱数の数 */
		double f0;                 /* 理論度数 */
		double xs = 0.0;           /* カイ自乗の値 */
		  
		f0 = NUMBER/10.0;
		long seed = new Date().getTime();
		Random random = new Random(seed);
		for (j = 1; j <= NUMBER; j ++){
			i = random.nextInt(35) + 1;
			f[i] = f[i] + 1;
		}
		g.drawString("     i       f[i]", 10, 10);
  
		/*頻度の検定*/
		for (i = 1; i <= 35; i ++){
			g.drawString("     " + i , 10 , 20 + 10 * i);
			g.drawString("     " + f[i] , 30 , 20 + 10 * i);
			xs = xs + (f[i] - f0) * (f[i] - f0) / f0;
		}
		g.drawString("xs = " +  xs , 10, 30 + 10 * i);
	}

}
// CodeCheck  ver1.1.10: 23519a6f7561c83dbf3214e23cebf4734ed3002086ca733a45ba7a201b67f68f