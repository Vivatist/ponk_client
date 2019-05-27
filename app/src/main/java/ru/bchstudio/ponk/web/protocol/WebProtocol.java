package ru.bchstudio.ponk.web.protocol;

import android.content.Context;

public interface WebProtocol {
    String doGet(String url, int httpRequestTimeout) throws Exception;
}
