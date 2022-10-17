package com.ashish.wallet.credit;

import com.ashish.wallet.balance.BalanceController;
import com.ashish.wallet.balance.BalanceService;
import com.ashish.wallet.model.GenericWalletRequest;
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
        classes={CreditController.class, CreditController.class})
@EnableAutoConfiguration
public class CreditControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CreditService creditService;
    @LocalServerPort
    int randomServerPort;


    @Test
    public void creditSuccessTest() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/v1/wallet/credit.json";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();

        WalletRequest creditRequest = new WalletRequest(123L,2.0,"EUR", 234567L);

        HttpEntity<WalletRequest> request = new HttpEntity<>(creditRequest, headers);
        ResponseEntity<WalletResponse> result = this.restTemplate.postForEntity(uri, request, WalletResponse.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
    }

}