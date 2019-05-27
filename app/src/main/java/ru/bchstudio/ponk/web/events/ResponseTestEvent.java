package ru.bchstudio.ponk.web.events;

public class ResponseTestEvent implements HttpResponse {

    public String message;


    @Override
    public void setResult(String string) {
        this.message = string;
    }
}