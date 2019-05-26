package ru.bchstudio.ponk.web;

import android.content.Context;

public interface WebProtocol {
    String doGet(Context context, String url, int httpRequestTimeout) throws Exception;
}
