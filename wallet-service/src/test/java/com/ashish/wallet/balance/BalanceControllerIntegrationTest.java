package com.ashish.wallet.balance;

import com.ashish.wallet.model.GenericWalletRequest;
import com.ashish.wallet.model.WalletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@EnableWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes={com.ashish.wallet.balance.BalanceController.class, com.ashish.wallet.balance.BalanceControllerIntegrationTest.class})
@EnableAutoConfiguration
public class BalanceControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private BalanceService balanceService;
    @LocalServerPort
    int randomServerPort;


    @Test
    public void getBalanceSuccessTest() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/v1/wallet/balance.json";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();

        GenericWalletRequest balanceRequest = new GenericWalletRequest(123L);

        HttpEntity<GenericWalletRequest> request = new HttpEntity<>(balanceRequest, headers);
        ResponseEntity<WalletResponse> result = this.restTemplate.postForEntity(uri, request, WalletResponse.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
    }

}