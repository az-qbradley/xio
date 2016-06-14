package com.xjeffrose.xio.server;

import com.xjeffrose.xio.core.ZkClient;
import java.util.HashSet;

public class XioDeterministicRuleEngine extends XioFirewall {

  public XioDeterministicRuleEngine(boolean noOp) {
    super(noOp);
  }

  public XioDeterministicRuleEngine(HashSet blacklist, HashSet whitelist) {
    super(blacklist, whitelist);
  }

  public XioDeterministicRuleEngine(ZkClient zkClient, boolean b) {
    super(zkClient, b);
  }
}
