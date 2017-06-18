package co.jp.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * LotteryResult.
 *
 * @author Dulk
 * @version 20170618
 * @date 2017/6/18
 */
public class LotteryResult {
    /** 每注单价 */
    public static final int UNIT_PRIZE = 2;

    //todo 因为前三等奖的奖金是浮动的，此处暂定为0，后续需要根据额外的API获取来设定
    /** 一等奖 */
    private static final int AWARD_1ST = 0;
    /** 二等奖 */
    private static final int AWARD_2ND = 0;
    /** 三等奖 */
    private static final int AWARD_3RD = 0;
    /** 四等奖 */
    private static final int AWARD_4TH = 200;
    /** 五等奖 */
    private static final int AWARD_5TH = 10;
    /** 六等奖 */
    private static final int AWARD_6TH = 5;

    private List<String> prize_1st = new ArrayList<String>();
    private List<String> prize_2nd = new ArrayList<String>();
    private List<String> prize_3rd = new ArrayList<String>();
    private List<String> prize_4th = new ArrayList<String>();
    private List<String> prize_5th = new ArrayList<String>();
    private List<String> prize_6th = new ArrayList<String>();

    public void savePrizeRecords(String lottery, int amountPart1, int amountPart2) {
        if (amountPart1 == 5) {
            if (amountPart2 == 2) {
                this.getPrize_1st().add(lottery);
            } else if (amountPart2 == 1) {
                this.getPrize_2nd().add(lottery);
            } else if (amountPart2 == 0) {
                this.getPrize_3rd().add(lottery);
            }
        }
        if (amountPart1 == 4) {
            if (amountPart2 == 2) {
                this.getPrize_3rd().add(lottery);
            } else if (amountPart2 == 1) {
                this.getPrize_4th().add(lottery);
            } else if (amountPart2 == 0) {
                this.getPrize_5th().add(lottery);
            }
        }
        if (amountPart1 == 3) {
            if (amountPart2 == 2) {
                this.getPrize_4th().add(lottery);
            } else if (amountPart2 == 1) {
                this.getPrize_5th().add(lottery);
            } else if (amountPart2 == 0) {
                this.getPrize_6th().add(lottery);
            }
        }
        if (amountPart1 == 2) {
            if (amountPart2 == 2) {
                this.getPrize_5th().add(lottery);
            } else if (amountPart2 == 1) {
                this.getPrize_6th().add(lottery);
            }
        }
        if (amountPart1 == 1) {
            if (amountPart2 == 2) {
                this.getPrize_6th().add(lottery);
            }
        }
        if (amountPart1 == 0) {
            if (amountPart2 == 2) {
                this.getPrize_6th().add(lottery);
            }
        }
    }

    public long winAwards() {
        long award = 0;
        //todo 打印中奖row/号码/中奖等级
        if (this.getPrize_1st().size() != 0) {
            award += this.getPrize_1st().size()*AWARD_1ST;
        }
        if (this.getPrize_2nd().size() != 0) {
            award += this.getPrize_2nd().size()*AWARD_2ND;
        }
        if (this.getPrize_3rd().size() != 0) {
            award += this.getPrize_3rd().size()*AWARD_3RD;
        }
        if (this.getPrize_4th().size() != 0) {
            award += this.getPrize_4th().size()*AWARD_4TH;
        }
        if (this.getPrize_5th().size() != 0) {
            award += this.getPrize_5th().size()*AWARD_5TH;
        }
        if (this.getPrize_6th().size() != 0) {
            award += this.getPrize_6th().size()*AWARD_6TH;
        }
        return award;
    }



    public List<String> getPrize_1st() {
        return prize_1st;
    }

    public void setPrize_1st(List<String> prize_1st) {
        this.prize_1st = prize_1st;
    }

    public List<String> getPrize_2nd() {
        return prize_2nd;
    }

    public void setPrize_2nd(List<String> prize_2nd) {
        this.prize_2nd = prize_2nd;
    }

    public List<String> getPrize_3rd() {
        return prize_3rd;
    }

    public void setPrize_3rd(List<String> prize_3rd) {
        this.prize_3rd = prize_3rd;
    }

    public List<String> getPrize_4th() {
        return prize_4th;
    }

    public void setPrize_4th(List<String> prize_4th) {
        this.prize_4th = prize_4th;
    }

    public List<String> getPrize_5th() {
        return prize_5th;
    }

    public void setPrize_5th(List<String> prize_5th) {
        this.prize_5th = prize_5th;
    }

    public List<String> getPrize_6th() {
        return prize_6th;
    }

    public void setPrize_6th(List<String> prize_6th) {
        this.prize_6th = prize_6th;
    }
}
