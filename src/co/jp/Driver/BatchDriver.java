package co.jp.Driver;

import co.jp.Abstract.Main;

public class BatchDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] strParameter= {"CakesGenerater","rows:1000"};
		String[] strParameter= {"CakesChecker","PARAM1:deng chao wo"};
//		String[] strParameter= {"CakesSelecter","rows:10","totalRows:100000000"};
//		String[] strParameter = {"LotteryGenerater", "group:100000", "type:dlt"};
//		String[] strParameter = {"LotteryChecker", "param:none"};

		Main.main(strParameter);
	}

}
// CodeCheck  ver1.1.10: e3798bf70ec73da85fe9a9a2ecaded7e03a0c395809bfd848fe92488d72ea176