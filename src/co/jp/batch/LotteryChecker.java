package co.jp.batch;

import co.jp.Abstract.AbstractPublic;
import co.jp.Bean.LotteryResult;
import co.jp.Enums.LotteryType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
        LotteryResult lotteryResult = new LotteryResult();
        int count = 0;
        int row = 1;
        List<String> winPart1 = (List<String>)Arrays.asList(Arrays.copyOfRange(winNum.split(","), 0, 5));
        List<String> winPart2 = (List<String>)Arrays.asList(Arrays.copyOfRange(winNum.split(","), 5, 7));
        int countPart1 = 0;
        int countPart2 = 0;
        if (file.exists()) {
            bufferedReader = new BufferedReader(new FileReader(file));
            String lottery;
            for(lottery = bufferedReader.readLine(); lottery != null; lottery = bufferedReader.readLine()) {
                /*
                if (winNum.equals(lottery)) {
                    System.out.println("[INFO] 恭喜你中奖了！意不意外？惊不惊喜？");
                    System.out.println("row:" + row);
                    count++;
                }
                */
                String[] lottPar1 = Arrays.copyOfRange(lottery.split(","), 0, 5);
                String[] lottPar2 = Arrays.copyOfRange(lottery.split(","), 5, 7);
                for(int i = 0; i < lottPar1.length; i++) {
                    if (winPart1.contains(lottPar1[i])) {
                        countPart1++;
                    }
                }
                for(int j = 0; j < lottPar2.length; j++) {
                    if (winPart2.contains(lottPar2[j])) {
                        countPart2++;
                    }
                }
                lotteryResult.savePrizeRecords(lottery, countPart1, countPart2);
                countPart1 = 0;
                countPart2 = 0;
                row++;
            }
        }
        System.out.println("总共购买注数：" + row);
        System.out.println("总共消费金额：" + row*LotteryResult.UNIT_PRIZE);
        System.out.println("总共中奖金额：" + lotteryResult.winAwards());
        System.out.println("最终盈利：" + (lotteryResult.winAwards() - row*LotteryResult.UNIT_PRIZE));

    }

    @Override
    public int execute(Map<String, String> paramMap) throws Exception {
        LotteryAPI lotteryAPI = LotteryAPIFactory.createLotteryAPI();
        String winNum = lotteryAPI.getLatestLotto(LotteryType.BIG_LOTTERY);
        checkLottery(winNum, "c://csv//lotteries.csv");

        return 0;
    }
}
