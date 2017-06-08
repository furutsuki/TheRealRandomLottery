package co.jp.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.jp.Abstract.AbstractPublic;

public class CakesChecker extends AbstractPublic {
	/**
	 * CAKESとFLOWERの取得および突合せ処理
	 *
	 * @param rowLen
	 *            行ごとに項目の個数
	 * @param cakesPath
	 *            cakesのパス
	 */
	private void checkCakes(int rowLen, String cakesPath) {
		String flowerFilePath = "d:\\flower.csv";
		List<List<String>> flowerList = new ArrayList<List<String>>();
		List<List<String>> cakesList = new ArrayList<List<String>>();
		flowerList = getDataList(flowerFilePath, rowLen);
		cakesList = getDataList(cakesPath, rowLen);
		butt(cakesList, flowerList);
	}

	/**
	 * CSVファイルを読み込んで、データを取得する処理
	 *
	 * @param filePath
	 *            CSVファイルのパス
	 * @param rowLen
	 *            行ごとに項目の個数
	 * @return
	 */
	private List<List<String>> getDataList(String filePath, int rowLen) {
		List<List<String>> dataList = new ArrayList<List<String>>();
		File dataFile = new File(filePath);
		if (!dataFile.exists()) {
			System.out.println("ファイルが存在しません:" + filePath);
		}
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			while (true) {
				try {
					String strline = br.readLine();
					if (strline != null) {
						List<String> row = new ArrayList<String>();
						String[] rowTmp = strline.split(",");
						if (rowTmp.length != rowLen) {
							System.out.println("CSVファイルの項目数が合わない。");
							return null;
						}
						for (String element : rowTmp) {
							row.add(element);
						}
						dataList.add(row);
					} else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 突合せ処理
	 *
	 * @param cakesList
	 *            cakesのリスト
	 * @param flowerList
	 *            flowerのリスト
	 * @return
	 */
	private void butt(List<List<String>> cakesList, List<List<String>> flowerList) {
		// 戻す型としてjavaBean追加
		if (null == flowerList || null == cakesList ) {
			System.out.println("format error.");
			return;
		}
		List<String> flower = flowerList.get(0);
		List<List<String>> sunfloweres = new ArrayList<List<String>>();
		int cnt = 0;
		for (List<String> cake:cakesList) {
			cnt++;
			if(cake.containsAll(flower)) {
				cake.add(0, String.valueOf(cnt));
				sunfloweres.add(cake);
			}
			if (cnt % 100000 == 0) {
				System.out.println(cnt + " records checked.");
			}
		}
		System.out.println("totally " + cnt + " records checked.");
		if (sunfloweres.size() > 0) {
			System.out.println("Congratulations!");
			for (List<String> sunflower : sunfloweres) {
				System.out.println(sunflower.get(0) + " row:");
				sunflower.remove(0);
				System.out.println(sunflower.toString());
			}
		} else {
			System.out.println("its regret that you can't hit this shit.");
		}
	}
	


	@Override
	public int execute(Map<String, String> paramMap) throws Exception {
		// 未完成
		String filepath = "d:\\cakes.csv";
		checkCakes(7,filepath);
		return 0;
	}
}
// CodeCheck  ver1.1.10: 38f5ce63bf6bf482a48fbb231f27517c620ad09c419e2dcc08fd8d057153dbf7