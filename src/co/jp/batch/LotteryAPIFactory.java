package co.jp.batch;

/**
 * LotteryAPIFactory.
 *
 * @author Dulk
 * @date 2017/6/12
 * @version 20170612
 */
public class LotteryAPIFactory {

    private static String net = "opencai";

    private static LotteryAPI createLotteryAPI(String net) {
        if ("opencai".equals(net)) {
            return new OpenCaiLottery();
        } else {
            System.err.println("无效的彩票API网站");
            return null;
        }

    }

    public static LotteryAPI createLotteryAPI() {
        return createLotteryAPI(net);
    }

}
