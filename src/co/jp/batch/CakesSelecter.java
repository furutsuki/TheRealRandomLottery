package co.jp.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.input.ReversedLinesFileReader;
import co.jp.Abstract.AbstractPublic;
import co.jp.Common.CommonUtil;
import co.jp.Common.Constants;
import co.jp.Enums.SaveMode;

public class CakesSelecter extends AbstractPublic{
	private int total = 0;
	@Override
	public int execute(Map<String, String> paramMap) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		int rows = Integer.valueOf(paramMap.get("rows"));
		total = Integer.valueOf(paramMap.get("totalRows"));
		String filepath = "c:\\csv\\selected.csv";
		String cakesPath = "c:\\csv\\cakes.csv";
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		System.out.println("start to write Csv file.");
		CommonUtil.writeCsv(filepath, select(cakesPath,rows), SaveMode.NEWFILE);
		System.out.println("end to write Csv file.");
		return 0;
	}
	
	private List<List<String>> select(String cakesPath,int rows) throws IOException {
		SecureRandom random = new SecureRandom();
		List<List<String>> selectedList = new ArrayList<List<String>>();
		int counter = 0;
		for (int i=0; i < rows; i++) {
			// シードを生成する
			byte[] seeds = SecureRandom.getSeed(37);
			while(counter < Constants.CAKES_INTERVAL) {
				random.setSeed(seeds);
				random.nextBytes(seeds);
				counter++;
			}
			counter = 0;
			int position = random.nextInt(total) + 1;
			if (position*2 > total) {
				selectedList.add(specifyCakeReverse(position, cakesPath));
			} else {
				selectedList.add(specifyCake(position, cakesPath));
			}
			System.out.println((i+1) + " rows selected.");
		}
		return selectedList;
	}
	
	private List<String> specifyCakeReverse (int position, String cakesPath) throws IOException {
		List<String> selectedCake = new ArrayList<String>();
		File dataFile = new File(cakesPath);
		
		int posRev= 0;
		if (!dataFile.exists()) {
			System.out.println("ファイルが存在しません:" + cakesPath);
		}
		try (ReversedLinesFileReader r = new ReversedLinesFileReader(new File(cakesPath),Charset.defaultCharset());) {
				for (;posRev < total - position;posRev++) {
					r.readLine();
				}
				String cake = r.readLine();
				String[] rowTmp = cake.split(",");
				for (String element : rowTmp) {
					selectedCake.add(CommonUtil.lpad(element, 2, '0'));
				}
				selectedCake.add(0, String.valueOf(position));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return selectedCake;
	}
	
	private List<String> specifyCake (int position, String cakesPath) throws IOException {
		List<String> selectedCake = new ArrayList<String>();
		File dataFile = new File(cakesPath);
		int posNow = 0;
		if (!dataFile.exists()) {
			System.out.println("ファイルが存在しません:" + cakesPath);
		}
		try (FileReader fr = new FileReader(cakesPath);
				 BufferedReader br = new BufferedReader(fr);) {
				for (;posNow < position-1;posNow++) {
					br.readLine();
				}
				String cake = br.readLine();
				String[] rowTmp = cake.split(",");
				for (String element : rowTmp) {
					selectedCake.add(CommonUtil.lpad(element, 2, '0'));
				}
				selectedCake.add(0, String.valueOf(position));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return selectedCake;
	}
}
// CodeCheck  ver1.1.10: af117c6e0d2ed05fb2a447ef984e81619523fa66189a6ae2903dea5864a72e6d