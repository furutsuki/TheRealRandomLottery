package co.jp.Common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import co.jp.Enums.SaveMode;

public class CommonUtil {

	/**
	 * <p>
	 * List<List<String>>形式のデータをCSVファイルに出力する。<BR>
	 * 文字コードは「UTF-8」とする。<BR>
	 * </p>
	 * 
	 * @param path CSVファイルの出力先パス
	 * @param lst CSV出力するデータ
	 * @param save ファイル保存モード（NEW,ADD）
	 */
	public static void writeCsv(String path, List<? extends List<? extends Object>> lst, SaveMode savemode) {

		String charSet = "UTF-8";

		//パラメータチェック

		File fl = new File(path);

		// ファイル存在チェック
		if (fl.exists()) {
			if (savemode.equals(SaveMode.NEWFILE)) {
				fl = null;
				System.out.println("already existed file.");
			}

			// 書込チェック
			if (!fl.canWrite()) {
				fl = null;
				System.out.println("Can't write file.");
			}
		}
		else {
			if (savemode.equals(SaveMode.ADD)) {
				fl = null;
				System.out.println("file not exist.");
			}
		}

		boolean writeMode = (savemode.equals(SaveMode.ADD));
		StringBuilder sb = new StringBuilder();

		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fl, writeMode),
				charSet)));) {
			for (List<?> lstVal : lst) {
				// 行文字列の初期化
				sb = new StringBuilder();
				int index = 0;
				
				// カンマ区切り文字列の作成
				for (Object val : lstVal) {
					if (index > 0) {
						sb.append(",");
					}
					sb.append(cnvObjToString(val));
					index++;
				}
				// 行書き込み
				pw.println(sb.toString());
			}

			pw.close();
		} catch (IOException e) {
			System.out.println("error occured in CsvOutputing.");
			e.printStackTrace();
		} finally {
			fl = null;
			sb = null;
		}
	}
	
	public static String cnvObjToString(Object arg) {
		if (arg != null) {
			return arg.toString();
		}
		return "";
	}
	
	public static String rpad(String target, int len, char paddingChar)
	  {
	    if (target == null) {
	      target = "";
	    }
	    if ((target != null) && (target.length() >= len)) {
	      return target;
	    }
	    StringBuilder buffer = new StringBuilder(target);
	    for (int i = 0; i < len - target.length(); i++) {
	      buffer.append(paddingChar);
	    }
	    return buffer.toString();
	  }
	
	  public static String lpad(String target, int len, char paddingChar)
	  {
	    if (target == null) {
	      target = "";
	    }
	    if ((target != null) && (target.length() >= len)) {
	      return target;
	    }
	    StringBuilder buffer = new StringBuilder();
	    for (int i = 0; i < len - target.length(); i++) {
	      buffer.append(paddingChar);
	    }
	    buffer.append(target);
	    return buffer.toString();
	  }

}
// CodeCheck  ver1.1.10: b672aa1293b99e20dcd38d462eedef2db3e191cf4300ea637dfae8176462c3d3