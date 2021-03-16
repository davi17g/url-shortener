package com.davi17g;

import com.aerospike.client.AerospikeException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Shortener implements HttpHandler {

    private String DbHostname = null;
    private String ServerDomain = null;

    private int ServerPort = 0;
    private int DbPort = 0;

    private int IdSize = 0;

    public Shortener(String DbHostname, int DbPort, String ServerDomain, int serverPort, int idlength) {
        this.DbHostname = DbHostname;
        this.DbPort = DbPort;
        this.ServerDomain = ServerDomain;
        this.ServerPort = serverPort;
        this.IdSize = idlength;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try{
            AerospikeConnection aerospikeConnection = AerospikeConnection.getInstance(
                    this.DbHostname, this.DbPort, this.IdSize);
            String url = exchange.getRequestURI().toString();
            String result = "";
            if (exchange.getRequestMethod().equals("POST")) {

                result = String.format("http://%s:%d/%s",
                        this.ServerDomain,  this.ServerPort, aerospikeConnection.Set(url));

                System.out.printf("Created short url: %s\n", result);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(exchange.getResponseBody());
                outputStreamWriter.write(result);
                exchange.sendResponseHeaders(200, 0);
                outputStreamWriter.close();

            } else if (exchange.getRequestMethod().equals("GET")) {
                result = aerospikeConnection.Get(url.substring(1));
                Headers headers = exchange.getResponseHeaders();
                String redirect = String.format("http://%s:%d%s", this.ServerDomain,  this.ServerPort, result);
                System.out.printf("Redirect: %s\n", redirect);
                headers.add("Location", redirect);
                exchange.sendResponseHeaders(301, -1);

            } else{
                exchange.sendResponseHeaders(404, -1);
            }

        } catch (AerospikeException e) {
            System.err.println(e);
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
        }

    }



