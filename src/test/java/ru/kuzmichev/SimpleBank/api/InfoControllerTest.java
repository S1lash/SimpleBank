package ru.kuzmichev.SimpleBank.api;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import ru.kuzmichev.SimpleBank.ApiTest;
import ru.kuzmichev.SimpleBank.RestMethods;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class InfoControllerTest extends ApiTest {

    @Test
    public void accountsFindAllWithoutIdsTest() throws Exception {
        accounts(Collections.EMPTY_LIST)
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts.length()").value(9));
    }

    @Test
    public void accountsFindAllWithCorrectIdsTest() throws Exception {
        accounts(Arrays.asList(1L, 2L, 3L, 5999L))
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts.length()").value(3));
    }

    @Test
    public void accountsFindAllWithIncorrectIdsTest() throws Exception {
        accounts(Arrays.asList(13234L, 5999L))
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts").isEmpty());
    }

    @Test
    public void terminalsFindAllWithoutIdsTest() throws Exception {
        terminals(Collections.EMPTY_LIST)
                .andExpect(jsonPath("$.terminals").isArray())
                .andExpect(jsonPath("$.terminals.length()").value(2));
    }

    @Test
    public void terminalsFindAllWithCorrectIdsTest() throws Exception {
        terminals(Arrays.asList(1L, 334L, 5999L))
                .andExpect(jsonPath("$.terminals").isArray())
                .andExpect(jsonPath("$.terminals.length()").value(1));
    }

    @Test
    public void terminalsFindAllWithIncorrectIdsTest() throws Exception {
        terminals(Arrays.asList(13234L, 5999L))
                .andExpect(jsonPath("$.terminals").isArray())
                .andExpect(jsonPath("$.terminals").isEmpty());
    }

    @Test
    public void transactiosFindAllWithoutIdsTest() throws Exception {
        transactions(Collections.EMPTY_LIST)
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(4));
    }

    @Test
    public void transactionsFindAllWithCorrectIdsTest() throws Exception {
        transactions(Arrays.asList(1L, 2L, 3L, 5999L))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(3));
    }

    @Test
    public void transactionsFindAllWithIncorrectIdsTest() throws Exception {
        transactions(Arrays.asList(13234L, 5999L))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty());
    }

    @Test
    public void accountOwnersFindAllWithoutIdsTest() throws Exception {
        accountOwners(Collections.EMPTY_LIST)
                .andExpect(jsonPath("$.accountOwners").isArray())
                .andExpect(jsonPath("$.accountOwners.length()").value(7));
    }

    @Test
    public void accountOwnersFindAllWithCorrectIdsTest() throws Exception {
        accountOwners(Arrays.asList(1L, 2L, 3L, 5999L))
                .andExpect(jsonPath("$.accountOwners").isArray())
                .andExpect(jsonPath("$.accountOwners.length()").value(3));
    }

    @Test
    public void accountOwnersFindAllWithIncorrectIdsTest() throws Exception {
        accountOwners(Arrays.asList(13234L, 5999L))
                .andExpect(jsonPath("$.accountOwners").isArray())
                .andExpect(jsonPath("$.accountOwners").isEmpty());
    }


    private ResultActions accounts(List<Long> ids) throws Exception {
        return api(RestMethods.ACCOUNTS, false).withContent()
                .add("ids", ids)
                .call();
    }

    private ResultActions transactions(List<Long> ids) throws Exception {
        return api(RestMethods.TRANSACTONS, false).withContent()
                .add("ids", ids)
                .call();
    }

    private ResultActions terminals(List<Long> ids) throws Exception {
        return api(RestMethods.TERMINALS, false).withContent()
                .add("ids", ids)
                .call();
    }

    private ResultActions accountOwners(List<Long> ids) throws Exception {
        return api(RestMethods.ACCOUNT_OWNERS, false).withContent()
                .add("ids", ids)
                .call();
    }
}
