package org.lsmr.p1;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.DisabledException;

//accepts one bank note at a time after validating it
public class PayWithBanknote {
	private BanknoteValidator banknoteValidator;
	private int amountPaid;

	/*Constructor: the banknoteValidator should be a constant therefore, the bankValidator should have
	been initiated with what currency and denominations to accept beforehand*/
	public PayWithBanknote(BanknoteValidator aBanknoteValidator) {
		this.banknoteValidator = aBanknoteValidator;
		this.amountPaid = 0;
	}
	
	//if the bank note is accepted, it is delivered into the Unidirectional sink and amount paid is registered
	//if the bank note is not accepted, it is ejected out of the same place the bank note was initially inserted into and amount paid is $0
	public void acceptBanknote(Banknote aBanknote) {
		try {
			banknoteValidator.accept(aBanknote);
			amountPaid = aBanknote.getValue();
		}
		catch (DisabledException e) {
			amountPaid = 0;
		}
	}
	
	public int getAmountPaid() {
		return amountPaid;
	}
}

