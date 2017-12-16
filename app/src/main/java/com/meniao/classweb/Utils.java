package com.meniao.classweb;

import java.util.Random;

/**
 * Created by Meniao Company on 2017/9/10.
 */

public class Utils {

    private static String color[] = {"F44336", "E91E63", "9C27B0", "673AB7", "3F51B5", "2196F3", "03A9F4", "00BCD4", "009688",
            "4CAF50", "8BC34A", "CDDC39", "FFEB3B", "FFC107", "FF9800", "FF5722", "795548",
            "9E9E9E", "607D8B"};

    public static String getARadomColors() {
        int max = 18;
        int min = 1;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        String colors = color[s];
        String en = "#" + colors;

        return en;
    }
}
