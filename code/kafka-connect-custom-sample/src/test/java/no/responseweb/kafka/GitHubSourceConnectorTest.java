package no.responseweb.kafka;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static no.responseweb.kafka.GitHubSourceConnectorConfig.*;

class GitHubSourceConnectorTest {
    private Map<String, String> initialConfig() {
        Map<String, String> baseProps = new HashMap<>();
        baseProps.put(OWNER_CONFIG, "foo");
        baseProps.put(REPO_CONFIG, "bar");
        baseProps.put(SINCE_CONFIG, "2021-01-01T01:23:45Z");
        baseProps.put(BATCH_SIZE_CONFIG, "100");
        baseProps.put(TOPIC_CONFIG, "github-issues");
        return (baseProps);
    }

    @Test
    void taskConfigsShouldReturnOneTaskConfig() {
        GitHubSourceConnector gitHubSourceConnector = new GitHubSourceConnector();
        gitHubSourceConnector.start(initialConfig());
        assertEquals(1,gitHubSourceConnector.taskConfigs(1).size());
        assertEquals(1,gitHubSourceConnector.taskConfigs(10).size());
    }
}
