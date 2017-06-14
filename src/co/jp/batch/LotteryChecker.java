package co.jp.batch;

import co.jp.Abstract.AbstractPublic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * LotteryChecker.
 *
 * @author Dulk
 * @version 20170614
 * @date 2017/6/14
 */
public class LotteryChecker extends AbstractPublic {

    /*
    todo
    1、先做一个完整匹配的
    2、再做一个范围匹配的，根据中奖号码的数量不同中奖等级不同，可以考虑正则表达式
    3、计算出中奖金额和消耗金额，得出利润
     */

    private void checkLottery(String winNum, String filepath) throws IOException {
        File file = new File(filepath);
        BufferedReader bufferedReader = null;
        int count = 0;
        int row = 0;
        if (file.exists()) {
            bufferedReader = new BufferedReader(new FileReader(file));
            String lottery;
            for(lottery = bufferedReader.readLine(); lottery != null; lottery = bufferedReader.readLine()) {
                if (winNum.equals(lottery)) {
                    System.out.println("[INFO] 恭喜你中奖了！意不意外？惊不惊喜？");
                    System.out.println("row:" + (row + 1));
                    count++;
                }
                row++;
            }
            if (count == 0) {
                System.out.println("[INFO] 你上缴了你的智商税... So Sad...");
            }
        }
        System.out.println("acmount:" + row);

    }

    @Override
    public int execute(Map<String, String> paramMap) throws Exception {
        LotteryAPI lotteryAPI = LotteryAPIFactory.createLotteryAPI();
        String winNum = lotteryAPI.getLatestLotto("dlt");
        checkLottery(winNum, "c://csv//lotteries.csv");

        return 0;
    }
}
