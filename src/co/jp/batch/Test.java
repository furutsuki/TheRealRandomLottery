package co.jp.batch;


import java.security.SecureRandom;
import java.util.Random;

/**
 * Test.
 *
 * @author Dulk
 * @version 20170612
 * @date 2017/6/12
 */
public class Test {
    public static void main(String[] args) {
        LotteryAPI lotteryAPI = LotteryAPIFactory.createLotteryAPI();
        System.out.println(lotteryAPI.getLatestLotto("dlt"));

    }
    /*
    todo
    http://www.opencai.net/apifree/
    1、大乐透的彩票生成规则需要修改，后两位可以和前面重复？
    2、兑奖方式需要根据不同奖项的兑奖规则进行更改
    3、写checker
    4、generater需要根据不同彩票修改
     */
}
