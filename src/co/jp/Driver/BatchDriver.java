package co.jp.Driver;

import co.jp.Abstract.Main;

public class BatchDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] strParameter= {"CakesGenerater","rows:100"};
//		String[] strParameter= {"CakesChecker","PARAM1:deng chao wo"};
//		String[] strParameter = {"LotteryGenerater", "group:10000000", "type:dlt"};
		String[] strParameter = {"LotteryChecker", "param:none"};

		Main.main(strParameter);
	}

}
// CodeCheck  ver1.1.10: 9df931eed055fb37e090ba866d8a6a1c0b65aa0988a65af233d57b3292a00499