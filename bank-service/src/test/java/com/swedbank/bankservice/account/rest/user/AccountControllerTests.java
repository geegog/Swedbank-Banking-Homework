package com.swedbank.bankservice.account.rest.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swedbank.account.application.dto.AccountDto;
import com.swedbank.account.application.dto.AccountTransactionRequest;
import com.swedbank.account.domian.model.CreateAccountRequest;
import com.swedbank.bankservice.common.rest.BaseIntegrationTest;
import com.swedbank.common.application.Dto.MoneyDto;
import com.swedbank.user.application.dto.UserAccountRequest;
import com.swedbank.user.application.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static com.swedbank.bankservice.common.utils.ApiUtil.mockPostApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTests extends BaseIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@LocalServerPort
	private Integer port;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	private UserDto user() {
		UserDto userDto = new UserDto();
		userDto.setFirstName("FirstName");
		userDto.setLastName("LastName");
		userDto.setEmail("test@gmail.com");
		userDto.setPassword("password");
		return userDto;
	}

	// 👑 Changed return type from 'void' to 'List<AccountDto>'
	List<AccountDto> createAccounts() throws Exception {

		CreateAccountRequest createAccountRequest1 = new CreateAccountRequest();
		createAccountRequest1.setAccountName("AccountName1");
		createAccountRequest1.setCurrency(Currency.getInstance("USD"));

		CreateAccountRequest createAccountRequest2 = new CreateAccountRequest();
		createAccountRequest2.setAccountName("AccountName2");
		createAccountRequest2.setCurrency(Currency.getInstance("EUR"));

		CreateAccountRequest createAccountRequest3 = new CreateAccountRequest();
		createAccountRequest3.setAccountName("AccountName3");
		createAccountRequest3.setCurrency(Currency.getInstance("SEK"));

		UserAccountRequest userAccountRequest = new UserAccountRequest();
		userAccountRequest.setUser(user());
		userAccountRequest.setCreateAccounts(List.of(createAccountRequest1, createAccountRequest2, createAccountRequest3));

		var payload = objectMapper.writeValueAsString(userAccountRequest);

		// 👑 Capture the full execution response using .andReturn()
		var mvcResult = mockPostApi(
				mockMvc,
				port,
				payload,
				"/account/user",
				null,
				null
		)
				.andExpect(status().isCreated())
				.andReturn(); // Ends the chain and hands you the result object

		// 👑 Deserialize the payload in the main method scope
		List<AccountDto> accounts = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(),
				objectMapper.getTypeFactory().constructCollectionType(List.class, AccountDto.class)
		);

		// Maintain safety checks
		Assertions.assertEquals(3, accounts.size());

		// 👑 Return the populated collection back to your test pipeline
		return accounts;
	}

	@Test
	void userAccountsCreationTest() throws Exception {
		createAccounts();
	}

	@Test
	void accountDepositTest() throws Exception {
		var accounts = createAccounts();
		AccountTransactionRequest  accountTransactionRequest = new AccountTransactionRequest();
		accountTransactionRequest.setAccountNumber(accounts.get(0).getAccountNumber());
		accountTransactionRequest.setValue(MoneyDto.builder()
				.amount(BigDecimal.valueOf(45))
				.currency(Currency.getInstance("USD"))
				.build());
		accountTransactionRequest.setReference("Reference");

		var payload = objectMapper.writeValueAsString(accountTransactionRequest);

		mockPostApi(
				mockMvc,
				port,
				payload,
				"/account/deposit",
				user(),
				null
		).andExpect(status().isCreated()).andExpect(result -> {
			var account = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					AccountDto.class
			);
			Assertions.assertEquals(BigDecimal.valueOf(45), account.getBalance().getAmount());
			Assertions.assertEquals(Currency.getInstance("USD"), account.getBalance().getCurrency());
		});

	}

}
