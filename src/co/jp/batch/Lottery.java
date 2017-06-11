package co.jp.batch;


import co.jp.Abstract.AbstractPublic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

/**
 * Lottery.
 *
 * @author Dulk
 * @date 17-6-6
 * @version 20170608
 */
public class Lottery extends AbstractPublic{


    /**
     * 彩票号码随机生成
     *
     * @param size 每组的号码数量
     * @param groups 需要生成的组数
     * @param scope 彩票号码的最大值
     * @return
     */
    private static List<List<Integer>> getRandomNumber(int size, int groups, int scope) {
        long seed = new Date().getTime();
        List<List<Integer>> lotteries = new ArrayList<List<Integer>>();

        for (int i = 0; i < groups; i++) {
            TreeSet<Integer> lottery = new TreeSet<Integer>();
            Random random = new Random(seed);
            while (lottery.size() != size) {
                lottery.add(random.nextInt(scope) + 1);
            }
            seed++;
            List<Integer> bean = new ArrayList<Integer>();
            bean.addAll(lottery);
            lotteries.add(bean);
        }

        return lotteries;
    }

    private static void listsToCSV(List<List<Integer>> lists, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        int size = Integer.parseInt(paramMap.get("size"));
        int group = Integer.parseInt(paramMap.get("group"));
        int scope = Integer.parseInt(paramMap.get("scope"));
        String filePath = "c:/csv/lotteries.csv";

        listsToCSV(getRandomNumber(size, group, scope), filePath);

        return 0;
    }
}
