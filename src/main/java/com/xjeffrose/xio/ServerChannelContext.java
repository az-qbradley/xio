package com.xjeffrose.xio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.*;

import com.xjeffrose.log.*;

// TODO: Parse JSON

class ServerChannelContext extends ChannelContext{
  private static final Logger log = Log.getLogger(ServerChannelContext.class.getName());

  public final HttpParser parser = new HttpParser();
  public final HttpRequest req = new HttpRequest();
  public final HttpResponse resp = new HttpResponse();

  private final Map<String, Service> routes;
  private State state = State.got_request;
  private Service service;

  ServerChannelContext(SocketChannel channel, Map<String, Service> routes) {
    super(channel);
    this.routes = routes;
  }

  private enum State {
    got_request,
    start_parse,
    finished_parse,
    start_response,
    finished_response,
  };


  public void read() {
    int nread = 1;

    while (nread > 0) {
      try {
        if (parser.getBody && req.setBody()) {
          nread = channel.read(req.body);
        } else {
          nread = channel.read(req.requestBuffer);
        }

        state = State.start_parse;
        if (!parser.parse(req, req.requestBuffer)) {
          throw new RuntimeException("Parser Failed to Parse");
        }

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (nread == -1) {
      try {
        channel.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    state = State.finished_parse;
    handleReq();
  }

  private void handleReq() {
    final String uri = req.uri();
    if (state == State.finished_parse) {
      service = routes.get(uri);
      if (!service.equals(null)) {
        service.handle(req, resp);
      }
    }
  }

  public void write() {
    try {
      if(state == State.finished_parse) {
        state = State.start_response;
        channel.write(resp.get());
        channel.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    state = State.finished_response;
  }
}