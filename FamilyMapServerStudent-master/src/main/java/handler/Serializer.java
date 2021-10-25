package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.results.Result;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Serializer {
    private HttpExchange exchange;
    private Gson gson;

    public Serializer(HttpExchange exchange) {
        this.exchange = exchange;
        gson = new Gson();
    }

    public void serialize(Result result) throws IOException {
        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(result, resBody);
        resBody.close();
    }

    public void deserialize() {

    }
}
