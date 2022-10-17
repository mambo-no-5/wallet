package com.ashish.wallet.balance;

import com.ashish.accounttransactionservice.account.PlayerAccountServiceException;
import com.ashish.wallet.exception.WalletExceptionHandler;
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
class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;
    @InjectMocks
    private BalanceController balanceController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
                new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc = standaloneSetup(balanceController)
                .setContentNegotiationManager(contentNegotiationManager)
                .setSingleView(mappingJackson2JsonView)
                .setHandlerExceptionResolvers(new WalletExceptionHandler())
                .build();
    }

    @Test
    void getBalanceTest() throws Exception {

        String expectedBalanceResponse = """
                        {
                            "playerId": 999,
                            "balance": 500.0,
                            "currency": "EUR"
                        }
                """;

        Mockito.when(balanceService.playerBalance(999L))
                .thenReturn(new WalletResponse(999L, 500.0, 544556677));

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestPayload());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(balanceService, times(1)).playerBalance(999L);

    }

    @Test
    void getBalanceTestWhenRequestInvalid() throws Exception {
        String getInvalidRequestPayload = """
                    {
                        "customerId": 123
                    }
                """;

        String expectedBalanceResponse = """
                    {
                        "errorMessage": "Mandatory field playerId is missing",
                        "errorStatus": 400
                    }
                """;

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getInvalidRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verifyNoInteractions(balanceService);

    }

    @Test
    void getBalanceTestWhenInvalidPlayerId() throws Exception {

        String expectedBalanceResponse = """
                    {
                        "errorMessage": "Invalid player id! player account doesn't exists",
                        "errorStatus": 400
                    }
                """;

        Mockito.when(balanceService.playerBalance(999L))
                .thenThrow(new WalletException("Invalid player id! player account doesn't exists"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestPayload());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(balanceService, times(1)).playerBalance(999L);

    }

    @Test
    void getBalanceTestUnExpectedAccountServiceException() throws Exception {
        String expectedBalanceResponse = """
                    {
                        "errorMessage": "Unexpected error occurred while fetching player balance",
                        "errorStatus": 500
                    }
                """;

        Mockito.when(balanceService.playerBalance(999L))
                .thenThrow(new PlayerAccountServiceException("Unexpected error occurred while fetching player balance"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestPayload());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(balanceService, times(1)).playerBalance(999L);

    }

    private String getRequestPayload() {
        return """
                    {
                        "playerId": 999
                    }
                """;
    }

}