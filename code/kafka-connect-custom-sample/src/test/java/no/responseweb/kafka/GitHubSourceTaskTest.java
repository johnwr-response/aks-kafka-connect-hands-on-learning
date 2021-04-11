package no.responseweb.kafka;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import no.responseweb.kafka.model.Issue;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static no.responseweb.kafka.GitHubSourceConnectorConfig.*;

class GitHubSourceTaskTest {

    GitHubSourceTask gitHubSourceTask = new GitHubSourceTask();

    private Map<String, String> initialConfig() {
        Map<String, String> baseProps = new HashMap<>();
        int batchSize = 10;
        baseProps.put(OWNER_CONFIG, "apache");
        baseProps.put(REPO_CONFIG, "kafka");
        baseProps.put(SINCE_CONFIG, "2021-04-26T01:23:44Z");
        baseProps.put(BATCH_SIZE_CONFIG, Integer.toString(batchSize));
        baseProps.put(TOPIC_CONFIG, "github-issues");
        return baseProps;
    }

    @Test
    void test() throws UnirestException {
        gitHubSourceTask.config = new GitHubSourceConnectorConfig(initialConfig());
        gitHubSourceTask.nextPageToVisit = 1;
        gitHubSourceTask.nextQuerySince = Instant.parse("2021-01-01T00:00:00Z");
        gitHubSourceTask.gitHubHttpAPIClient = new GitHubAPIHttpClient(gitHubSourceTask.config);
        String url = gitHubSourceTask.gitHubHttpAPIClient.constructUrl(gitHubSourceTask.nextPageToVisit, gitHubSourceTask.nextQuerySince);
        System.out.println(url);
        HttpResponse<JsonNode> httpResponse = gitHubSourceTask.gitHubHttpAPIClient.getNextIssuesAPI(gitHubSourceTask.nextPageToVisit, gitHubSourceTask.nextQuerySince);
        if (httpResponse.getStatus() != 403) {
            assertEquals(200, httpResponse.getStatus());
            Set<String> headers = httpResponse.getHeaders().keySet();
            assertTrue(headers.contains("ETag"));
            assertTrue(headers.contains("X-RateLimit-Limit"));
            assertTrue(headers.contains("X-RateLimit-Remaining"));
            assertTrue(headers.contains("X-RateLimit-Reset"));
            assertEquals(10, httpResponse.getBody().getArray().length());
            JSONObject jsonObject = (JSONObject) httpResponse.getBody().getArray().get(0);
            Issue issue = Issue.fromJson(jsonObject);
            assertNotNull(issue);
            assertEquals(9622, issue.getNumber());
        }
    }
}
