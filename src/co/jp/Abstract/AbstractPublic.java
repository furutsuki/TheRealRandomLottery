package co.jp.Abstract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import co.jp.Interface.IAbstractPublic;

public abstract class AbstractPublic implements IAbstractPublic{
	protected int absExecute(Map<String, String> argsMap) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
		String strDate = sdf.format(cal.getTime());
		System.out.println(strDate + ":処理を開始します。");
		int ret = 0;
		try {
			ret = execute(argsMap);
			cal = Calendar.getInstance();
			strDate = sdf.format(cal.getTime());
			if (ret < 10) {
				System.out.println(strDate + ":処理正常終了。");
			} else {
				System.out.println(strDate + ":処理異常終了。");
			}
		} catch (Exception e) {
			cal = Calendar.getInstance();
			strDate = sdf.format(cal.getTime());
			System.out.println(strDate + ":生成処理が失敗しました");
			e.printStackTrace();
		}
		return ret;
	}
}
// CodeCheck  ver1.1.10: c5612ccac1b64e7584a918182eb4b378d5d14f7815f4fef6b083a5e91b788491