package com.ashish.wallet.credit;

import com.ashish.accounttransactionservice.transaction.TransactionServiceException;
import com.ashish.wallet.exception.WalletExceptionHandler;
import com.ashish.wallet.model.WalletRequest;
import com.ashish.wallet.model.WalletResponse;
import com.ashish.wallet.exception.WalletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class CreditControllerTest {

    @Mock
    private CreditService creditService;
    @InjectMocks
    private CreditController creditController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
                new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc = standaloneSetup(creditController)
                .setContentNegotiationManager(contentNegotiationManager)
                .setSingleView(mappingJackson2JsonView)
                .setHandlerExceptionResolvers(new WalletExceptionHandler())
                .build();
    }

    @Test
    void creditTest() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 2,
                            "currency": "EUR",
                            "transactionId": 4567890
                        }
                """;

        String expectedResponse = """
                        {
                            "playerId": 999,
                            "balance": 2.0,
                            "transactionId": 4567890
                        }
                """;

        WalletRequest creditRequest = new WalletRequest(13L, 2, "EUR", 4567890L);
        Mockito.when(creditService.credit(creditRequest))
                .thenReturn(new WalletResponse(999L, 2.0, 4567890));

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(creditService, times(1)).credit(creditRequest);

    }


    @Test
    void creditTestInvalidRequestMissingTransactionId() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 1,
                            "currency": "EUR"
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Mandatory field, transactionId is missing",
                        "errorStatus": 400
                    }
                """;

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verifyNoInteractions(creditService);

    }

    @Test
    void creditTestDuplicateTransaction() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 1,
                            "currency": "EUR",
                            "transactionId": 4567890
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Transaction id=4567890 is not unique.",
                        "errorStatus": 400
                    }
                """;

        WalletRequest creditRequest = new WalletRequest(13L, 1, "EUR", 4567890L);
        Mockito.when(creditService.credit(creditRequest))
                .thenThrow(new WalletException("Transaction id=4567890 is not unique."));

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(creditService, times(1)).credit(creditRequest);

    }

    @Test
    void creditTestUnExpectedTransactionServiceException() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 5,
                            "currency": "EUR",
                            "transactionId": 4567890
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Unexpected transaction service exception",
                        "errorStatus": 500
                    }
                """;

        WalletRequest creditRequest = new WalletRequest(13L, 5, "EUR", 4567890L);
        Mockito.when(creditService.credit(creditRequest))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(creditService, times(1)).credit(creditRequest);

    }


}