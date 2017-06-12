package co.jp.Driver;

import co.jp.Abstract.Main;

public class BatchDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] strParameter= {"CakesGenerater","PARAM1:deng chao wo"};
//		String[] strParameter= {"CakesChecker","PARAM1:deng chao wo"};
		String[] strParameter = {"LotteryGenerater", "size:7", "group:1000000", "scope:37"};
		Main.main(strParameter);
	}

}
// CodeCheck  ver1.1.10: 6928f02c380b8a0fdefdb309c27d8674a7ac19306d6d9f01fec522dea197ddb8