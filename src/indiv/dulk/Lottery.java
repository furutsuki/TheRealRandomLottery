package indiv.dulk;

import java.util.*;

/**
 * Lottery.
 *
 * @author Dulk
 * @date 17-6-6
 * @version 20170606
 */
public class Lottery {


    /**
     * 彩票号码随机生成
     *
     * @param size 每组的号码数量
     * @param groups 需要生成的组数
     * @param scope 彩票号码的最大值
     * @return
     */
    public static List<List<Integer>> getRandomNumber(int size, int groups, int scope) {
        long seed = new Date().getTime();
        List<List<Integer>> lotteries = new ArrayList<List<Integer>>();

        for (int i = 0; i < groups; i++) {
            TreeSet lottery = new TreeSet();
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



}
