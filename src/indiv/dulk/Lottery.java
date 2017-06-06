package indiv.dulk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    public static List<int[]> getRandomNumber(int size, int groups, int scope) {
        long seed = new Date().getTime();
        List<int[]> lotteries = new ArrayList<int[]>();

        for (int i = 0; i < groups; i++) {
            Random random = new Random(seed);
            int[] lottery = new int[size];
            for (int j = 0; j < size; j++) {
                lottery[j] = random.nextInt(scope) + 1;
            }
            lotteries.add(CommonUtil.sort(lottery));
            seed++;
        }

        return lotteries;
    }



}
