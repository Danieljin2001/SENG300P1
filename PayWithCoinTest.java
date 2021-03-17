import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;

//Test class for Customer pays with coin use case

public class PayWithCoinTest {
	
	//Parameters for SelfCheckoutStation
	Currency currency= Currency.getInstance("CAD");
	int[] bankNoteDenominations= {5,10,20,50,100};
	BigDecimal[] coinDenominations= {new BigDecimal(0.01), new BigDecimal(0.05),
			 new BigDecimal(0.1),new BigDecimal(0.25), new BigDecimal(0.50)};
	int scaleMaximumWeight= 1000;
	int scaleSensitivity=1;
	
	SelfCheckoutStation scs = new SelfCheckoutStation(currency, bankNoteDenominations,
			coinDenominations, scaleMaximumWeight, scaleSensitivity);
	
	PayWithCoin coinPayment= new PayWithCoin(scs);
	
	//This registers the instance of our self checkout station and the required appropriate registers before each test
	@Before
	public void setUp() throws Exception{
		scs.coinSlot.register(coinPayment);
		scs.coinValidator.register(coinPayment);
		scs.coinStorage.register(coinPayment);
		scs.coinTray.register(coinPayment);
			
	}
	
	//Testing a valid coin with a valid currency & valid denomination
	//System should increment a valid coin value into amount inserted
	@Test
	public void testCoinPayment_WithValidCoin() {
		Currency aCurrency= Currency.getInstance("CAD");
		BigDecimal aDenomination= new BigDecimal(0.50);
		Coin aCoin= new Coin(aDenomination, aCurrency);
		
		
		BigDecimal amountInserted = coinPayment.inputCoin(aCoin);
		BigDecimal expectedAmountInserted  = new BigDecimal(0.50);
		
		assertEquals(expectedAmountInserted, amountInserted	);
		
	}
	
	//System should not increment an invalid coin value into amount inserted 
	@Test
	public void testCoinPayment_WithInvalidCoin(){
		Currency aCurrency= Currency.getInstance("CAD");
		BigDecimal aDenomination= new BigDecimal(0.51);
		Coin aCoin= new Coin(aDenomination, aCurrency);
		
		
		BigDecimal amountInserted = coinPayment.inputCoin(aCoin);
		BigDecimal expectedAmountInserted  = new BigDecimal(0);
		
		assertEquals(expectedAmountInserted, amountInserted	);
		
	}
	

	//System shouldn't increment even a valid coin value into amount paid when the coin slot is disabled
	@Test
	public void testCoinPayment_WhenCoinSlotisDisabled() {
		
		//valid coin is created
		Currency aCurrency= Currency.getInstance("CAD");
		BigDecimal aDenomination= new BigDecimal(0.50); 	
		Coin aCoin= new Coin(aDenomination, aCurrency);
		
		scs.coinSlot.disable();
		BigDecimal amountInserted=  coinPayment.inputCoin(aCoin);
		BigDecimal expectedAmountInserted  = new BigDecimal(0);
		
		
		//amountInserted must be zero ($0.50 can't be accepted) since the  coin slot is disabled
		assertEquals( expectedAmountInserted,amountInserted);
		
	}
	
	//System should properly increment valid coin value into amount paid when the coin slot is enabled
	@Test
	public void testCoinPayment_WhenCoinSlotisEnabled() {
		
		//valid coin is created
		Currency aCurrency= Currency.getInstance("CAD");
		BigDecimal aDenomination= new BigDecimal(0.50); 	
		Coin aCoin= new Coin(aDenomination, aCurrency);
		
		scs.coinSlot.enable();
		BigDecimal amountInserted=  coinPayment.inputCoin(aCoin);
		BigDecimal expectedAmountInserted  = new BigDecimal(0.50);
		
		//amountInserted must be zero ($0.50 can't be accepted) since the  coin slot is disabled
		assertEquals(expectedAmountInserted,amountInserted);
		
	}
	
	//When coin storage unit is loaded, a customer must be able to make payments as normal
	@Test
	public void testCoinPayment_WhenCoinStorageUnitisLoaded() throws SimulationException, OverloadException {
		
		//valid coin is created
		Currency aCurrency= Currency.getInstance("CAD");
		BigDecimal aDenomination= new BigDecimal(0.50); 	
		Coin aCoin= new Coin(aDenomination, aCurrency);
		
		BigDecimal anotherDenomination= new BigDecimal(0.50); 
		Coin loadedCoin= new Coin(anotherDenomination,aCurrency);
		
		scs.coinStorage.load(loadedCoin,loadedCoin);
		BigDecimal amountInserted=  coinPayment.inputCoin(aCoin);
		BigDecimal expectedAmountInserted  = new BigDecimal(0.50);
		
		
		assertEquals(expectedAmountInserted,amountInserted);
	}
		
	
	//When coin storage unit is unloaded, the storage unit must be empty, thus the storage unit capacity must be 1000	
	@Test
	public void testCoinPayment_WhenCoinStorageUnitisUnloaded() throws SimulationException, OverloadException {
		
		scs.coinStorage.unload();
		int actualStorageUnitCapacity=scs.coinStorage.getCapacity(); //must be 0 after unloading
		
		int expectedStorageUnitCapacity=1000;
		
		System.out.println(scs.coinStorage.getCapacity());
		
		
		assertEquals(expectedStorageUnitCapacity, actualStorageUnitCapacity);
		
		
		//amountInserted must be zero ($0.50 can't be accepted) since the  coin slot is disabled
		
	}

	//Inserting a valid coin into a full coin storage unit
	//When a coin storage unit is full, even if you make a payment with a valid coin, that coin should not be accepted
	//and the inserted coin value should a part of amountInserted.

	@Test
	public void testCoinPayment_WhenCoinStorageUnitisFull() throws SimulationException, OverloadException {
	
		//Creating a valid coin 
		Currency aCurrency= Currency.getInstance("CAD");
		BigDecimal aDenomination= new BigDecimal(0.50); 	
		Coin aCoin= new Coin(aDenomination, aCurrency);
		
		//Making coin storage unit full by adding a valid coin 1000 times (max limit)
		while(scs.coinStorage.getCoinCount()<=999) {
			coinPayment.inputCoin(aCoin);
		}
		
		BigDecimal amountInserted=  coinPayment.inputCoin(aCoin);
		BigDecimal expectedAmountInserted  = new BigDecimal(0);
		
		assertEquals(expectedAmountInserted,amountInserted);
	}
	

}
