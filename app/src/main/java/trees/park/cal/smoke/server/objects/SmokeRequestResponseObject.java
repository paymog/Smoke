package trees.park.cal.smoke.server.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by michael on 2014-09-06.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmokeRequestResponseObject {
    private final long multicastId;
    private final int success;
    private final int failure;
    private final int canonicalIds;
    private final List<Map<String,String>> results;

    @JsonCreator
    public SmokeRequestResponseObject(@JsonProperty("multicast_id")long multicastId,
                                      @JsonProperty("success")int success,
                                      @JsonProperty("failure")int failure,
                                      @JsonProperty("canonical_ids")int canonicalIds,
                                      @JsonProperty("results")List<Map<String,String>> results) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
    }

    public long getMulticastId() {
        return multicastId;
    }

    public int getFailure() {
        return failure;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public List<Map<String, String>> getResults() {
        return results;
    }

    public int getSuccess() {
        return success;
    }
}
