package org.lsmr.p1;

import java.util.Currency;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.BanknoteValidatorListener;

//accepts one bank note at a time after validating it
public class PayWithBanknote {
	private SelfCheckoutStation selfCheckoutStation;
	private boolean banknoteIsValid = false;

	/*Constructor: the banknoteValidator should be a constant therefore, the bankValidator should have
	been initiated with what currency and denominations to accept beforehand*/
	public PayWithBanknote(SelfCheckoutStation aSelfCheckoutStation) {
		this.selfCheckoutStation = aSelfCheckoutStation;
		selfCheckoutStation.banknoteValidator.register(bvl);
	}
	
	private BanknoteValidatorListener bvl = new BanknoteValidatorListener() {

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
			// TODO Auto-generated method stub
			banknoteIsValid = true;
			
		}

		@Override
		public void invalidBanknoteDetected(BanknoteValidator validator) {
			// TODO Auto-generated method stub
			banknoteIsValid = false;
			
		}
		
	};
	
	
	//if the bank note is accepted, it is delivered into the Unidirectional sink and amount paid is registered
	//if the bank note is not accepted, it is ejected out of the same place the bank note was initially inserted into and amount paid is $0
	public int acceptBanknote(Banknote aBanknote) {
		int amountPaid = 0;
		try {
			selfCheckoutStation.banknoteValidator.accept(aBanknote);
			if (selfCheckoutStation.banknoteStorage.hasSpace() && banknoteIsValid) { //checks if the sink has space and bank note is valid
				amountPaid = aBanknote.getValue();
				return amountPaid;
			}
			else {
				amountPaid = 0;
				return amountPaid;
			}
		}
		catch (DisabledException e) {
			amountPaid = 0;
			return amountPaid;
		}
	}
	
}

