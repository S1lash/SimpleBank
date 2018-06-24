package ru.kuzmichev.SimpleBank;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class PerformBuilder {
    public static final String SESSION_HEADER = "x-auth-token";
    public static final String JSON_TYPE = "application/json;charset=UTF-8";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parseMediaType(JSON_TYPE);

    private final MockMvc mvc;
    private final String url;

    private String content;
    private boolean post;

    public PerformBuilder(MockMvc mvc, String url, boolean post) {
        this.mvc = mvc;
        this.url = url;
        this.post = post;
    }


    public ContentBuilder withContent() {
        return new ContentBuilder(this);
    }

    public PerformBuilder withContent(String content) {
        Assert.assertTrue(StringUtils.isNotBlank(content));
        this.content = content;
        return this;
    }

    public ResultActions call() throws Exception {
        MockHttpServletRequestBuilder mockRequestBuilder;
        if (post) {
            mockRequestBuilder = post(url)
                .contentType(JSON_MEDIA_TYPE)
                .accept(JSON_MEDIA_TYPE);
        } else {
            mockRequestBuilder = get(url)
                .contentType(JSON_MEDIA_TYPE)
                .accept(JSON_MEDIA_TYPE);

        }
        if (content != null) {
            mockRequestBuilder.content(content);
        }

        return mvc.perform(mockRequestBuilder).andExpect(content().contentType(JSON_MEDIA_TYPE));
    }
}
