package org.interledger.ilp.ledger.adaptor.rest.json;

import org.interledger.ilp.core.ledger.model.ConnectorInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonConnectorInfo implements ConnectorInfo {

  private String id;
  private String name;

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.ConnectorInfo#getId()
   */
  @Override
  @JsonProperty(value = "id")
  public String getId() {
    return id;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.ConnectorInfo#getName()
   */
  @Override
  @JsonProperty(value = "name")
  public String getName() {
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

}
