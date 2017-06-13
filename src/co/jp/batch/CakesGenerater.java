package co.jp.batch;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import co.jp.Abstract.AbstractPublic;
import co.jp.Common.CommonUtil;
import co.jp.Common.Constants;
import co.jp.Enums.SaveMode;

public class CakesGenerater extends AbstractPublic {
	// 生成する組数を制御するためのカウンター
	private int pairs = 0;
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
	private List<List<Integer>> generateCakes(int num, int seedLength, int rowLen) {
		SecureRandom random = new SecureRandom();
		// シードを生成する
		byte[] seeds = SecureRandom.getSeed(seedLength);
		// 項目数を制御するためのカウンター
		int counter = 0;
		// 実際ループの回数を記録
		int realCount = 0;
		// 生成する組数を制御するためのカウンター
		int tmprows = 0;
		// 返すためのList
		List<List<Integer>> CakesList = new ArrayList<List<Integer>>();

		while (num > tmprows) {
			List<Integer> list = new ArrayList<Integer>();
			while (counter < rowLen) {
				random.setSeed(seeds);
				int cake = random.nextInt(38);
				if (!list.contains(cake) && 0 != cake) {
					list.add(cake);
					counter++;
				}
				random.nextBytes(seeds);
				realCount++;
			}
			Collections.sort(list);
			pairs++;
			tmprows++;
			counter = 0;
			CakesList.add(list);
			if (pairs % Constants.MSG_COUNT == 0) {
				System.out.println(pairs + " cakes generated.");
			}
		}

		System.out.println("乱数取得回数：" + realCount);
		return CakesList;
	}

	@Override
	public int execute(Map<String, String> paramMap) throws Exception {
		System.out.println("start to write Csv file.");
		String filepath = "c:\\csv\\cakes.csv";
		int rows = Integer.valueOf(paramMap.get("rows"));
		File file = new File(filepath);
		while (rows > 0) {
			int tmp = 0;
			if (rows >= Constants.LIMIT_COUNT) {
				tmp = Constants.LIMIT_COUNT;
				rows -= Constants.LIMIT_COUNT;
			} else {
				tmp = rows;
				rows = 0;
			}
			if (file.exists()) {
				CommonUtil.writeCsv(filepath, generateCakes(tmp, 40, 7), SaveMode.ADD);
			} else {
				CommonUtil.writeCsv(filepath, generateCakes(tmp, 40, 7), SaveMode.NEWFILE);
			}
		}
		
		System.out.println("totally " + pairs + " cakes generated.");

		System.out.println("Csv file 出力処理完了。");
		return 0;
	}

}
// CodeCheck  ver1.1.10: c6465e9daec5a7affc737d5430cfbdbc3bac71b6eb50c1c1acb26099f31652a9