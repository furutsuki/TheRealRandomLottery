package co.jp.batch;

import co.jp.Common.NetUtil;
import co.jp.Enums.LotteryType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * OpenCaiLottery.
 *
 * @author Dulk
 * @version 20170612
 * @date 2017/6/12
 */
public class OpenCaiLottery implements LotteryAPI {

    private static String freeAPI = "http://f.apiplus.net/%s-%d.%s";

    public static String getAPI(LotteryType lottoType, int row, String callbackType) {
        String type = "";
        if (lottoType == LotteryType.BIG_LOTTERY) {
            type = "dlt";
        }
        return String.format(freeAPI, type, row, callbackType);
    }


    public String getLatestLotto(LotteryType lottoType) {
        String url = getAPI(lottoType, 1, "json");
        String jsonStr = null;
        String lottery = null;
        String openTime = null;

        try {
            jsonStr = NetUtil.sendHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonStr != null) {
            JSONObject jsonObj = JSON.parseObject(jsonStr);
            JSONArray jsonArr = jsonObj.getJSONArray("data");
            lottery = JSON.parseObject(jsonArr.getString(0)).getString("opencode");
            openTime = JSON.parseObject(jsonArr.getString(0)).getString("opentime");
            lottery = lottery.replace("+", ",");
        }
        System.out.println("[INFO] " + "OPEN_TIME: " + openTime);
        System.out.println("[INFO] " + lottoType.toString() + ": " + lottery);
        return lottery;
    }




}
