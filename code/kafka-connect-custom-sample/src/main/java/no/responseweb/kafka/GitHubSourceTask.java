package no.responseweb.kafka;

import no.responseweb.kafka.utils.DateUtils;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.responseweb.kafka.GitHubSchemas.*;

public class GitHubSourceTask extends SourceTask {
    public GitHubSourceConnectorConfig config;

    protected Instant nextQuerySince;
    protected Integer nextPageToVisit = 1;

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> map) {

    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        return null;
    }

    @Override
    public void stop() {

    }
    private Map<String, String> sourcePartition() {
        Map<String, String> map = new HashMap<>();
        map.put(OWNER_FIELD, config.getOwnerConfig());
        map.put(REPOSITORY_FIELD, config.getRepoConfig());
        return map;
    }

    private Map<String, String> sourceOffset(Instant updatedAt) {
        Map<String, String> map = new HashMap<>();
        map.put(UPDATED_AT_FIELD, DateUtils.maxInstant(updatedAt, nextQuerySince).toString());
        map.put(NEXT_PAGE_FIELD, nextPageToVisit.toString());
        return map;
    }
}
