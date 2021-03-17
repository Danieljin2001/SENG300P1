//Name:Aayush Dahal

import java.math.BigDecimal;
import java.util.ArrayList;

import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.listeners.*;
import org.lsmr.selfcheckout.products.BarcodedProduct;

public class PayWithCoin implements CoinSlotListener, CoinStorageUnitListener, CoinTrayListener,CoinValidatorListener  {
	
	private SelfCheckoutStation aSelfCheckoutStation;
	
	//constructor for use case
	public PayWithCoin(SelfCheckoutStation station) {
		aSelfCheckoutStation=station;
	}
	
	boolean isEnabled=false;;
	public boolean isCoinValid=false;
	
	//the coin payment is made through this function
	
	//emulates a customer inputting a coin into the coin slot
	//if the coin is valid, then the amountInserted is incremented by the value of the inserted coin
	//if there is an exception or the coin is  invalid, amountInserted will remain zero
	//amountInserted is returned at the end of the function	
	public BigDecimal inputCoin(Coin coin) {
		
		//amountInserted will be the total coins added into the slot & also the amount to decrement from their total price each iteration during checkout
		BigDecimal amountInserted= BigDecimal.ZERO;
		
		try {
			//this emulates a customer inserting a coin into the coin slot
			aSelfCheckoutStation.coinSlot.accept(coin);
			
			//if the inserted coin is valid then add the inserted coin value into the total amount added(paid)
			if(isCoinValid==true) {
				
				amountInserted=amountInserted.add(coin.getValue());
				
				isCoinValid=false;
			}
			
		}
		
		catch(Exception e) {
			
			return amountInserted;
		}	
				
		return amountInserted;	
	}
	

	//when a valid coin is detected, this event is triggered in the CoinValidator accept method
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		isCoinValid=true;
		
	}
	

	//not useful to override & write implementation for these methods for our purposes
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void coinAdded(CoinTray tray) {
		// TODO Auto-generated method stub
	}
	@Override
	public void coinsFull(CoinStorageUnit unit) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void coinAdded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void coinInserted(CoinSlot slot) {
		// TODO Auto-generated method stub
		
	}

}


