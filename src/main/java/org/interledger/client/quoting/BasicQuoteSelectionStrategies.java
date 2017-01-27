package org.interledger.client.quoting;

import java.util.Set;

import org.interledger.quoting.QuoteSelectionStrategy;
import org.interledger.quoting.model.QuoteResponse;

public class BasicQuoteSelectionStrategies {

  public static QuoteSelectionStrategy LowestDestinationAmount = (Set<QuoteResponse> quotes) -> {
    
    QuoteResponse bestQuote = null;
    for (QuoteResponse quoteResponse : quotes) {
      if(bestQuote == null || bestQuote.getDestinationAmount().isGreaterThan(quoteResponse.getDestinationAmount())) {
        bestQuote = quoteResponse;
      }
    }
    
    return bestQuote;
  };

  public static QuoteSelectionStrategy LowestSourceAmount = (Set<QuoteResponse> quotes) -> {
    
    QuoteResponse bestQuote = null;
    for (QuoteResponse quoteResponse : quotes) {
      if(bestQuote == null || bestQuote.getSourceAmount().isGreaterThan(quoteResponse.getSourceAmount())) {
        bestQuote = quoteResponse;
      }
    }
    
    return bestQuote;
  };

}
