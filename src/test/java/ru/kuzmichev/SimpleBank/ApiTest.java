package ru.kuzmichev.SimpleBank;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestDatasourceConfig.class)
@AutoConfigureMockMvc
public abstract class ApiTest {

    @Autowired
    protected MockMvc mvc;

    public PerformBuilder anonymousApi(String url, boolean post) {
        return new PerformBuilder(mvc, url, post);
    }

    protected static ResultMatcher successfulResponse() {
        return result -> {
            status().isOk().match(result);
            jsonPath("$.status").value("SUCCESS").match(result);
        };
    }

    protected static ResultMatcher failedResponse() {
        return result -> {
            status().isBadRequest().match(result);
            jsonPath("$.status").value("FAIL").match(result);
        };
    }

    public PerformBuilder api(String url) {
        return anonymousApi(url, true);
    }

    public PerformBuilder api(String url, boolean post) {
        return anonymousApi(url, post);
    }
}
