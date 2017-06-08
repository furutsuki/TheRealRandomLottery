package co.jp.applet;

import java.applet.Applet;
import java.awt.Graphics;
import java.security.SecureRandom;

public class EvaluateAlgorithmSecureRandom extends Applet{
	private static final long serialVersionUID = 2L;

	public void paint(Graphics g) {

		int NUMBER = 10000; /* 発生させる乱数の数 */
		int j; /* forのカウンタ */
		int i; /* 0.1ごとのキザミの番号 */
		int f[] = new int[36]; /* キザミiに落ちる乱数の数 */
		double f0; /* 理論度数 */
		double xs = 0.0; /* カイ自乗の値 */

		f0 = NUMBER / 10.0;
		SecureRandom random = new SecureRandom();
		// シードを生成する
		byte[] seeds = SecureRandom.getSeed(40);
		for (j = 1; j <= NUMBER; j++) {
			random.setSeed(seeds);
			i = random.nextInt(36);
			f[i] = f[i] + 1;
			random.nextBytes(seeds);
		}
		g.drawString("     i       f[i]", 10, 10);

		/* 頻度の検定 */
		for (i = 1; i <= 35; i++) {
			g.drawString("     " + i, 10, 20 + 10 * i);
			g.drawString("     " + f[i], 30, 20 + 10 * i);
			xs = xs + (f[i] - f0) * (f[i] - f0) / f0;
		}
		g.drawString("xs = " + xs, 10, 30 + 10 * i);
	}
}
// CodeCheck ver1.1.10:
// 75f3c37a8f889ed674f254b5b2539844994a0d41db4dbe9ebfc53ee397951801
// CodeCheck  ver1.1.10: 47f145b4ded889744d8b33f4c06a3542f43affee516acebdfaa5b4e5ed87eb25