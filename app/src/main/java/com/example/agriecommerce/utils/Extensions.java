package com.example.agriecommerce.utils;

import android.app.Activity;
import android.widget.Toast;

public class Extensions {

    public static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static String cardXXGen(String card) {
        int size = card.length();
        StringBuilder cardG = new StringBuilder();

        for (char i : card.toCharArray()) {
            size--;
            if (size < 4) {
                cardG.append(i);
            }
        }
        return "**** **** **** " + cardG.toString();
    }
}
