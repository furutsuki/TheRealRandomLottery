package co.jp;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CakesGenerater {

	/**
	 * CAKESの組み合わせを生成する
	 *
	 * @param num
	 *            生成する組数
	 * @param seedLength
	 *            シードの長
	 * @param rowLen
	 *            行ごとに項目の個数
	 */
	public void generateCakes(int num, int seedLength, int rowLen) {
		SecureRandom random = new SecureRandom();
		// シードを生成する
		byte[] seeds = SecureRandom.getSeed(20);
		// 項目数を制御するためのカウンター
		int counter = 0;
		// 実際ループの回数を記録
		int realCount = 0;
		// 生成する組数を制御するためのカウンター
		int pairs = 0;

		while (num > pairs) {
			List<Integer> list = new ArrayList<Integer>();
			while (counter < rowLen) {
				random.setSeed(seeds);
				int cake = random.nextInt(36);
				if (!list.contains(cake) && 0 != cake) {
					list.add(cake);
					counter++;
				}
				random.nextBytes(seeds);
				realCount++;
			}
			Collections.sort(list);
			pairs++;
			counter = 0;
			System.out.println(list);
		}

		System.out.println(realCount);
	}

}