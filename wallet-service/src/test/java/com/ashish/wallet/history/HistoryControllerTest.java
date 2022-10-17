package com.ashish.wallet.history;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.accounttransactionservice.transaction.Transaction;
import com.ashish.accounttransactionservice.transaction.TransactionService;
import com.ashish.accounttransactionservice.transaction.TransactionType;
import com.ashish.wallet.exception.WalletExceptionHandler;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @Mock
    private PlayerAccountService playerAccountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private HistoryController historyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
                new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc = standaloneSetup(historyController)
                .setContentNegotiationManager(contentNegotiationManager)
                .setSingleView(mappingJackson2JsonView)
                .setHandlerExceptionResolvers(new WalletExceptionHandler())
                .build();
    }

    @Test
    void playerHistoryTestSuccess() throws Exception {

        String expectedResponse = """
                    {
                        "transactions" :
                            [
                                {
                                    "transactionId": 4567890,
                                    "playerId": 999,
                                    "amount": 0.85,
                                    "transactionType": "DEBIT",
                                    "transactionTimestamp": "2022-05-11T23:28:06.492311994Z"
                                },
                                {
                                    "transactionId": 5678345,
                                    "playerId": 999,
                                    "amount": 2.0,
                                    "transactionType": "CREDIT",
                                    "transactionTimestamp": "2022-05-11T20:00:17.386492513Z"
                                }
                            ]
                    }
                """;

        List<Transaction> response = List.of(
                new Transaction(4567890L, 999L, 0.85, TransactionType.DEBIT, "2022-05-11T23:28:06.492311994Z"),
                new Transaction(5678345L, 999L, 2.0, TransactionType.CREDIT, "2022-05-11T20:00:17.386492513Z")
        );
        Mockito.when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        Mockito.when(transactionService.getTransactions(999L)).thenReturn(response);

        var request = MockMvcRequestBuilders.get("/v1/wallet/history.json?playerId=999");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(transactionService, times(1)).getTransactions(999L);

    }

    @Test
    void playerHistoryTestWhenInvalidPlayerId() throws Exception {

        String expectedResponse = """
                    {
                        "errorMessage": "Invalid [player id=999], player account doesn't exists.",
                        "errorStatus": 400
                    }
                """;

        Mockito.when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(false);

        var request = MockMvcRequestBuilders.get("/v1/wallet/history.json?playerId=999");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verifyNoInteractions(transactionService);

    }
}