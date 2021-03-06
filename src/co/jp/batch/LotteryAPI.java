package co.jp.batch;


import co.jp.Enums.LotteryType;

/**
 * LotteryAPI.
 *
 * @author Dulk
 * @version 20170612
 * @date 2017/6/12
 */
public interface LotteryAPI {

    /**
     * 获取最新一期的中奖彩票的号码
     * @param lottoType
     * @return
     */
    String getLatestLotto(LotteryType lottoType);

}
