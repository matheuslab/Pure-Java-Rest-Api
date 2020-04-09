package com.pipa.api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface Handler extends HttpHandler {

  void handle(HttpExchange exchange) throws IOException;

}
