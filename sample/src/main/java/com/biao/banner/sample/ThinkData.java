package com.biao.banner.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dell on 2015/1/28.
 */
public class ThinkData {
    private static final Random random = new Random();
    private static final String[] data = {
            "http://b.hiphotos.baidu.com/image/pic/item/8cb1cb1349540923ec2138799058d109b3de4968.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/0823dd54564e9258e01fc2e99e82d158ccbf4e89.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/91ef76c6a7efce1b9fca75a2ac51f3deb58f65f0.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/d0c8a786c9177f3e7f106bc272cf3bc79e3d56dc.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/09fa513d269759ee2ea448afb1fb43166c22dfd9.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/eaf81a4c510fd9f9ce2a5205262dd42a2834a498.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd46f5cb55da28b87d6277ff959.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/6609c93d70cf3bc7141dca80d200baa1cd112ab8.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/9d82d158ccbf6c81037f3d5bbf3eb13533fa403c.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/9f510fb30f2442a7186eec16d243ad4bd1130224.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/43a7d933c895d1436916f59070f082025aaf0724.jpg",
    };

    public static List<String> getData() {
        List<Integer> dataInts = new ArrayList<>();
        List<String> dataStrs = new ArrayList<>();
        int size = random.nextInt(2) + 3;
        int temp;
        while (dataInts.size() < size) {
            temp = random.nextInt(data.length);
            if (!dataInts.contains(temp)) {
                dataInts.add(temp);
            }
        }

        for (Integer integer : dataInts) {
            dataStrs.add(data[integer]);
        }
        return dataStrs;
    }
}
