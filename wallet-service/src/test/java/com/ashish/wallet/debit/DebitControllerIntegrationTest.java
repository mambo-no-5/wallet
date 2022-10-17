package com.ashish.wallet.debit;

import com.ashish.wallet.credit.CreditController;
import com.ashish.wallet.credit.CreditService;
import com.ashish.wallet.model.WalletRequest;
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
        classes={DebitController.class, DebitController.class})
@EnableAutoConfiguration
public class DebitControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private DebitService debitService;
    @LocalServerPort
    int randomServerPort;


    @Test
    public void creditSuccessTest() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/v1/wallet/debit.json";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();

        WalletRequest debitRequest = new WalletRequest(123L,2.0,"EUR", 234567L);

        HttpEntity<WalletRequest> request = new HttpEntity<>(debitRequest, headers);
        ResponseEntity<WalletResponse> result = this.restTemplate.postForEntity(uri, request, WalletResponse.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
    }

}