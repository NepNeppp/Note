package com.example.note.model.utils;

import android.content.Intent;

public class Utils {

    /**
     * @param msgText 数据库中的content
     */
    public Intent shareText(String msgText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, msgText);
        return shareIntent;
    }
}
