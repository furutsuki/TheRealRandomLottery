package co.jp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class checkCakes {
	/**
	 * CAKESとFLOWERの取得および突合せ処理
	 *
	 * @param rowLen
	 *            行ごとに項目の個数
	 * @param cakesPath
	 *            cakesのパス
	 */
	public void checkCakes(int rowLen, String cakesPath) {
		String flowerFilePath = "d:\\flower.csv";
		List<List<String>> flowerList = new ArrayList<List<String>>();
		List<List<String>> cakesList = new ArrayList<List<String>>();
		flowerList = getDataList(flowerFilePath, rowLen);
		cakesList = getDataList(cakesPath, rowLen);
		String result = butt(cakesList, flowerList, "TOP");
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
	 * @param mode
	 *            突合せモード
	 * @return
	 */
	private String butt(List<List<String>> cakesList, List<List<String>> flowerList, String mode) {
		// 戻す型としてjavaBean追加
		String dummy = "";
		if ("ALL".equals(mode)) {
			return dummy;
		} else {
			return dummy;
		}
	}
}
