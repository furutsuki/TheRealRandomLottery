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

    public static LotteryAPI createLotteryAPI() {
        return createLotteryAPI(net);
    }

    public static LotteryAPI createLotteryAPI(String net) {
        switch (net) {
            case "opencai":
                return new OpenCaiLottery();
            default:
                System.err.println("无效的彩票API网站");
                return null;
        }
    }


}
