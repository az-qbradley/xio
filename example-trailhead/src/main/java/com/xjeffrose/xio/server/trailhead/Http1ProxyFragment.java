package com.xjeffrose.xio.server.trailhead;

import com.xjeffrose.xio.application.ApplicationState;
import com.xjeffrose.xio.pipeline.XioPipelineFragment;
import com.xjeffrose.xio.server.XioServerConfig;
import com.xjeffrose.xio.server.XioServerState;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;

// [X] parse request
//      * [X] for now parse the entire request
//      * [X] in the future parse just the header and pass through the content
// [X] determine url
//      * [X] for now just grab matching url and proxy
//      * [ ] in the future use substring match or regex
// [X] map url to config use server.Route.java
// [X] proxy or error
// [X] proxy logic
//      * [X] now just read/write/flush
//      * [ ] later do some sweet channel piping
public class Http1ProxyFragment implements XioPipelineFragment {

  private final UrlRouter router;

  public Http1ProxyFragment(UrlRouter router) {
    this.router = router;
  }

  public String applicationProtocol() {
    return null;
  }

  public void buildHandlers(ApplicationState appState, XioServerConfig config, XioServerState state, ChannelPipeline pipeline) {
    pipeline.addLast(new Http1ProxyHandler(router));
  }

}
