package org.knowm.xchange.btcmarkets.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.btcmarkets.BTCMarkets;
import org.knowm.xchange.btcmarkets.BTCMarketsAuthenticated;
import org.knowm.xchange.btcmarkets.BTCMarketsAuthenticatedV3;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.RestProxyFactory;
import si.mazi.rescu.SynchronizedValueFactory;

public class BTCMarketsBaseService extends BaseExchangeService implements BaseService {

  protected final BTCMarkets btcmPublic;
  @Deprecated protected final BTCMarketsAuthenticated btcm;
  protected final BTCMarketsAuthenticatedV3 btcmv3;
  @Deprecated protected final BTCMarketsDigest signerV1;
  @Deprecated protected final BTCMarketsDigest signerV2;
  protected final BTCMarketsDigestV3 signerV3;
  protected final SynchronizedValueFactory<Long> nonceFactory;

  public BTCMarketsBaseService(Exchange exchange) {
    super(exchange);
    final ExchangeSpecification spec = exchange.getExchangeSpecification();
    this.btcm =
        RestProxyFactory.createProxy(
            BTCMarketsAuthenticated.class, spec.getSslUri(), getClientConfig());
    this.btcmv3 =
        RestProxyFactory.createProxy(
            BTCMarketsAuthenticatedV3.class, spec.getSslUri(), getClientConfig());
    this.btcmPublic =
        RestProxyFactory.createProxy(
            BTCMarkets.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
    if (spec.getSecretKey() != null) {
      this.signerV1 = new BTCMarketsDigest(spec.getSecretKey(), false);
      this.signerV2 = new BTCMarketsDigest(spec.getSecretKey(), true);
      this.signerV3 = new BTCMarketsDigestV3(spec.getSecretKey());
    } else {
      this.signerV1 = null;
      this.signerV2 = null;
      this.signerV3 = null;
    }
    this.nonceFactory = exchange.getNonceFactory();
  }
}
