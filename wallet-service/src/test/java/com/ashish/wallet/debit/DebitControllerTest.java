package com.ashish.wallet.debit;

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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class DebitControllerTest {

    @Mock
    private DebitService debitService;
    @InjectMocks
    private DebitController debitController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
                new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc = standaloneSetup(debitController)
                .setContentNegotiationManager(contentNegotiationManager)
                .setSingleView(mappingJackson2JsonView)
                .setHandlerExceptionResolvers(new WalletExceptionHandler())
                .build();
    }

    @Test
    void debitTestSuccess() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 4567890
                        }
                """;

        String expectedResponse = """
                        {
                            "playerId": 999,
                            "balance": 5.0,
                            "transactionId": 4567890
                        }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 4567890L);
        Mockito.when(debitService.debit(debitRequest))
                .thenReturn(new WalletResponse(999L, 5.0, 4567890));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debit(debitRequest);

    }

    @Test
    void debitTestFailureWhenInsufficientFunds() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 4567890
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Transaction declined! player has in-sufficient funds",
                        "errorStatus": 400
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 4567890L);
        Mockito.when(debitService.debit(debitRequest))
                .thenThrow(new WalletException("Transaction declined! player has in-sufficient funds"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debit(debitRequest);

    }

    @Test
    void debitTestFailureDuplicateTransaction() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 4567890
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Transaction id 4567890 is not unique!",
                        "errorStatus": 400
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 4567890L);
        Mockito.when(debitService.debit(debitRequest))
                .thenThrow(new WalletException("Transaction id 4567890 is not unique!"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debit(debitRequest);

    }

    @Test
    void debitTestFailureUnExpectedErrorTransactionServiceException() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 999,
                            "amount": 5.0,
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

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 4567890L);
        Mockito.when(debitService.debit(debitRequest))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debit(debitRequest);

    }

}