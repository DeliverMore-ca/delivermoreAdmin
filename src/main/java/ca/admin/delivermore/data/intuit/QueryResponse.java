
package ca.admin.delivermore.data.intuit;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "NamedItem",
    "startPosition",
    "maxResults"
})
@Generated("jsonschema2pojo")
public class QueryResponse {

    @JsonProperty("NamedItem")
    @JsonAlias({"Vendor", "Employee", "Account", "Customer", "Item", "PaymentMethod"})
    private List<NamedItem> NamedItem = null;
    @JsonProperty("startPosition")
    private Integer startPosition;
    @JsonProperty("maxResults")
    private Integer maxResults;

    @JsonProperty("NamedItem")
    public List<NamedItem> getNamedItem() {
        return NamedItem;
    }

    @JsonProperty("NamedItem")
    public void setNamedItem(List<NamedItem> NamedItem) {
        this.NamedItem = NamedItem;
    }

    public Map<String, NamedItem> getNamedItemMap(){
        Map<String, NamedItem> namedItemMap = new TreeMap<>();
        if(getNamedItem()!=null){
            for (NamedItem namedItem: getNamedItem()) {
                namedItemMap.put(namedItem.getDisplayName(), namedItem);
            }
        }else{
            return Map.of();
        }
        return namedItemMap;
    }

    @JsonProperty("startPosition")
    public Integer getStartPosition() {
        return startPosition;
    }

    @JsonProperty("startPosition")
    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    @JsonProperty("maxResults")
    public Integer getMaxResults() {
        return maxResults;
    }

    @JsonProperty("maxResults")
    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "NamedItem=" + NamedItem +
                ", startPosition=" + startPosition +
                ", maxResults=" + maxResults +
                ", namedItem=" + getNamedItem() +
                '}';
    }
}
