import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.Test;
import org.lsmr.p1.PayWithBanknote;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

//testing for PayWithBanknote
public class PayWithBanknoteTest {
	//setting up the constructor for the SelfCheckoutStation
	private Currency currency = Currency.getInstance("CAD");
	private int[] banknoteDenominations = {5,10,20,50,100};
	private BigDecimal[] coinDenominations = {new BigDecimal(0.01), new BigDecimal(0.05), new BigDecimal(0.1),new BigDecimal(0.25), new BigDecimal(0.50)};
	private int scaleMaximumWeight= 1000;
	private int scaleSensitivity=1;
	
	SelfCheckoutStation scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
	PayWithBanknote banknotePayment = new PayWithBanknote (scs);
	
	//testing when the bank note has correct denomination but wrong currency
	@Test
	public void testAcceptBanknote_WithWrongCurrency() {
		Currency aCurrency = Currency.getInstance("USD");
		int aBanknoteDenomination = 5;
		Banknote aBanknote = new Banknote(aBanknoteDenomination, aCurrency);
		int amountInserted = banknotePayment.acceptBanknote(aBanknote);
		int expectedAmountInserted = 0;
		assertEquals(expectedAmountInserted, amountInserted);
		
	}
	
	//testing with a valid bank note
	@Test
	public void testAcceptBanknote_WithValidBanknote() {
		Currency aCurrency = Currency.getInstance("CAD");
		int aBanknoteDenomination = 5;
		Banknote aBanknote = new Banknote(aBanknoteDenomination, aCurrency);
		int amountInserted = banknotePayment.acceptBanknote(aBanknote);
		int expectedAmountInserted = 5;
		assertEquals(expectedAmountInserted, amountInserted);
		
	}
	
	//testing with an invalid bank note
	@Test
	public void testAcceptBanknote_WithInvalidBanknote() {
		Currency aCurrency = Currency.getInstance("CAD");
		int aBanknoteDenomination = 6;
		Banknote aBanknote = new Banknote(aBanknoteDenomination, aCurrency);
		int amountInserted = banknotePayment.acceptBanknote(aBanknote);
		int expectedAmountInserted = 0;
		assertEquals(expectedAmountInserted, amountInserted);
		
	}
	
	//testing when the bank note slot is disabled
	@Test
	public void testAcceptBanknote_WhenBanknoteSlotDisabled() {
		Currency aCurrency = Currency.getInstance("CAD");
		int aBanknoteDenomination = 5;
		Banknote aBanknote = new Banknote(aBanknoteDenomination, aCurrency);
		scs.banknoteValidator.disable();
		int amountInserted = banknotePayment.acceptBanknote(aBanknote);
		int expectedAmountInserted = 0;
		assertEquals(expectedAmountInserted, amountInserted);
		
	}
	
	//testing when the bank note slot is enabled
	@Test
	public void testAcceptBanknote_WhenBanknoteSlotEnabled() {
		Currency aCurrency = Currency.getInstance("CAD");
		int aBanknoteDenomination = 5;
		Banknote aBanknote = new Banknote(aBanknoteDenomination, aCurrency);
		scs.banknoteValidator.enable();
		int amountInserted = banknotePayment.acceptBanknote(aBanknote);
		int expectedAmountInserted = 5;
		assertEquals(expectedAmountInserted, amountInserted);
		
	}
		
}
