package co.jp.batch;

import co.jp.Common.NetUtil;
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

    private final String LOTTO_TYPE_DLT = "dlt";



    public static String getAPI(String lottoType, int row, String callbackType) {
        return String.format(freeAPI, lottoType, row, callbackType);
    }


    public String getLatestLotto(String lottoType) {
        String url = getAPI(lottoType, 1, "json");
        String jsonStr = null;
        String lottery = null;

        try {
            jsonStr = NetUtil.sendHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonStr != null) {
            JSONObject jsonObj = JSON.parseObject(jsonStr);
            JSONArray jsonArr = jsonObj.getJSONArray("data");
            lottery = JSON.parseObject(jsonArr.getString(0)).getString("opencode");
            lottery = lottery.replace("+", ",");
        }

        return lottery;
    }




}
