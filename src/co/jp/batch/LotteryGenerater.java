package co.jp.batch;


import co.jp.Abstract.AbstractPublic;
import co.jp.Enums.LotteryType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * LotteryGenerater.
 *
 * @author Dulk
 * @date 17-6-6
 * @version 20170608
 */
public class LotteryGenerater extends AbstractPublic{


    /**
     * 彩票号码随机生成
     *
     * @param groups 需要生成的组数
     * @param type 彩票类型
     * @return
     */
    private static List<List<Integer>> getRandomLotteries(int groups, LotteryType type) {
        List<List<Integer>> lotteries = new ArrayList<List<Integer>>();
        SecureRandom secureRandom = new SecureRandom();

        //超级大乐透
        if (type == LotteryType.BIG_LOTTERY) {
            for (int i = 0; i < groups; i++) {
                List<Integer> lottery = new ArrayList<Integer>();
                while (lottery.size() != 5) {
                    int number = secureRandom.nextInt(35) + 1;
                    if (lottery.contains(number)) {
                        continue;
                    } else {
                        lottery.add(number);
                    }
                }
                Collections.sort(lottery);
                int bean1 = secureRandom.nextInt(12) + 1;
                int bean2 = secureRandom.nextInt(12) + 1;
                while (bean1 == bean2) {
                    bean2 = secureRandom.nextInt(12) + 1;
                }
                if (bean1 < bean2) {
                    lottery.add(bean1);
                    lottery.add(bean2);
                } else {
                    lottery.add(bean2);
                    lottery.add(bean1);
                }
                lotteries.add(lottery);
            }
        }



        return lotteries;
    }

    /**
     * List<List<Integer>>格式输出为csv文件
     *
     * @param lists
     * @param filePath
     * @throws IOException
     */
    private static void listsToCSV(List<List<Integer>> lists, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        String content = "";
        for (List<Integer> list : lists) {
            content = list.toString().substring(1, list.toString().length() - 1);
            content = content.replace(" ", "");
            bw.write(content);
            bw.newLine();
        }

        bw.flush();
        bw.close();
    }

    @Override
    public int execute(Map<String, String> paramMap) throws Exception {
        int group = Integer.parseInt(paramMap.get("group"));
        LotteryType type = null;
        String filePath = "c:/csv/lotteries.csv";
        if ("dlt".equals(paramMap.get("type"))) {
            type = LotteryType.BIG_LOTTERY;
        }

        listsToCSV(getRandomLotteries(group, type), filePath);

        return 0;
    }
}
