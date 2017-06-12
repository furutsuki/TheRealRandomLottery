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
import co.jp.Common.CommonUtil;
import co.jp.Common.Constants;

public class CakesChecker extends AbstractPublic {
	/**
	 * CAKESとFLOWERの取得および突合せ処理
	 *
	 * @param rowLen
	 *            行ごとに項目の個数
	 * @param cakesPath
	 *            cakesのパス
	 * @throws IOException
	 */
	private void checkCakes(int rowLen, String cakesPath) throws IOException {
		String flowerFilePath = "c:\\csv\\flower.csv";
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
	 * @throws IOException
	 */
	private List<List<String>> getDataList(String filePath, int rowLen) throws IOException {
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
							row.add(CommonUtil.lpad(element, 2, '0'));
						}
						dataList.add(row);
					} else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			br.close();

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
	
	/**
	 * 突合せ処理
	 *
	 * @param cakesList
	 *            cakesのリスト
	 * @param flowerList
	 *            flowerのリスト
	 * @return
	 */
	private List<String> buttByRow(List<String> cake, List<String> flower) {
		// 戻す型としてjavaBean追加
		if (null == flower || null == cake ) {
			System.out.println("format error.");
			return null;
		}
		if(cake.containsAll(flower)) {
			return cake;
		} else {
			return null;
		}
		
	}
	
	private void checkByRow(String filepath) throws IOException {
		String flowerFilePath = "c:\\csv\\flower.csv";
		int rowLen = 7;
		int count = 0;
		List<String> flower = new ArrayList<String>();
		List<List<String>> sunfloweres = new ArrayList<List<String>>();
		flower = getDataList(flowerFilePath, rowLen).get(0);
		File dataFile = new File(filepath);
		if (!dataFile.exists()) {
			System.out.println("ファイルが存在しません:" + filepath);
		}
		try (FileReader fr = new FileReader(filepath);
			 BufferedReader br = new BufferedReader(fr);) {
			while (true) {
				try {
					String strline = br.readLine();
					if (strline != null) {
						count++;
						List<String> row = new ArrayList<String>();
						List<String> result = new ArrayList<String>();
						String[] rowTmp = strline.split(",");
						if (rowTmp.length != rowLen) {
							System.out.println("CSVファイルの項目数が合わない。");
							break;
						}
						for (String element : rowTmp) {
							row.add(CommonUtil.lpad(element, 2, '0'));
						}
						result = buttByRow(row, flower);
						if (null != result) {
							result.add(0, String.valueOf(count));
							sunfloweres.add(result);
						}
						if (count % Constants.MSG_COUNT == 0) {
							System.out.println(count + " records checked.");
						}
						
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
		System.out.println("totally " + count + " records checked.");
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
		String filepath = "c:\\csv\\cakes.csv";
		//checkCakes(7,filepath);
		checkByRow(filepath);
		return 0;
	}
}
