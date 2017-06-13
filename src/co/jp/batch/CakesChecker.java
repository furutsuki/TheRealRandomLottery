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
import co.jp.Bean.PeoniesBean;
import co.jp.Common.CommonUtil;
import co.jp.Common.Constants;

public class CakesChecker extends AbstractPublic {
	private int counter = 0;
	@Override
	public int execute(Map<String, String> paramMap) throws Exception {
		// 未完成
		String filepath = "c:\\csv\\cakes.csv";
		resultPrintOut(checkByRow(filepath));
		return 0;
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
	private int buttByRow(List<String> cake, List<String> flower) {
		int count = 0;
		// 戻す型としてjavaBean追加
		if (null == flower || null == cake ) {
			System.out.println("format error.");
			return 0;
		}
		for (String piece : cake) {
			if (flower.contains(piece)) {
				count++;
			}
		}
		return count;
		
	}
	
	private PeoniesBean checkByRow(String filepath) throws IOException {
		String flowerFilePath = "c:\\csv\\flower.csv";
		// 牡丹BEAN
		PeoniesBean pb = new PeoniesBean();
		int rowLen = 7;
		List<String> flower = new ArrayList<String>();
		List<List<String>> sunfloweres_1 = new ArrayList<List<String>>();
		List<List<String>> sunfloweres_2 = new ArrayList<List<String>>();
		List<List<String>> sunfloweres_3 = new ArrayList<List<String>>();
		pb.setFirstClass(sunfloweres_1);
		pb.setSecondClass(sunfloweres_2);
		pb.setThirdClass(sunfloweres_3);
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
						counter++;
						List<String> row = new ArrayList<String>();
						int endCode = 0;
						String[] rowTmp = strline.split(",");
						if (rowTmp.length != rowLen) {
							System.out.println("CSVファイルの項目数が合わない。");
							break;
						}
						for (String element : rowTmp) {
							row.add(CommonUtil.lpad(element, 2, '0'));
						}
						endCode = buttByRow(row, flower);

						setPeoniesBean(pb,row,endCode,counter);
						
						if (counter % Constants.MSG_COUNT == 0) {
							System.out.println(counter + " records checked.");
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
		System.out.println("totally " + counter + " records checked.");
		return pb;
	}
	
	private void setPeoniesBean(PeoniesBean pb, List<String> row, int level, int count) {
		
		if (Constants.LEVEL_1 == level) {
			row.add(0,String.valueOf(count));
			pb.getFirstClass().add(row);
		} else if (Constants.LEVEL_2 == level) {
			row.add(0,String.valueOf(count));
			pb.getSecondClass().add(row);
		} else if (Constants.LEVEL_3 == level) {
			row.add(0,String.valueOf(count));
			pb.getThirdClass().add(row);
		}
	}
	
	private void resultPrintOut(PeoniesBean pb) {
		List<List<String>> sunfloweres_1 = pb.getFirstClass();
		List<List<String>> sunfloweres_2 = pb.getSecondClass();
		List<List<String>> sunfloweres_3 = pb.getThirdClass();
		int count1 = sunfloweres_1.size();
		int count2 = sunfloweres_2.size();
		int count3 = sunfloweres_3.size();
		int count = count1 + count2 + count3;
		long cost = Constants.PRICE*counter;
		long award = Constants.AWARD_1 * count1 + Constants.AWARD_2 * count2 + Constants.AWARD_3 * count3;
		if (count > 0) {
			System.out.println("Congratulations!");
			System.out.println("LEVEL1 : " + count1);
			System.out.println("LEVEL2 : " + count2);
			System.out.println("LEVEL3 : " + count3);
			for (List<String> sunflower : sunfloweres_1) {
				System.out.println("LEVEL1 IN " + sunflower.get(0) + " row:");
				sunflower.remove(0);
				System.out.println(sunflower.toString());
			}

		} else {
			System.out.println("its regret that you can't hit this shit.");
		}
		System.out.println("total cost: " + cost);
		System.out.println("total earned: " + award);
		
	}
	
}
// CodeCheck  ver1.1.10: 7c911b42180831a6c7bbadeac0879af13d0c9a819972cb3c91cfdd546ad831e4