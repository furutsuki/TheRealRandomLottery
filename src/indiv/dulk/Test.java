package indiv.dulk;

import java.util.List;

/**
 * Test.
 *
 * @author Dulk
 * @version 20170606
 * @date 17-6-6
 */
public class Test {
    public static void main(String[] args) {

        List<int[]> lotteries = Lottery.getRandomNumber(7, 4, 35);
        for (int[] lottery : lotteries) {
            for (int number : lottery) {
                System.out.print(number + " ");
            }
            System.out.println();
        }

    }
}
