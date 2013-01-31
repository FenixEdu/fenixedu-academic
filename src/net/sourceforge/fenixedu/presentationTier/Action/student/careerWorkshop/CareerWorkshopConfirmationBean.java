package net.sourceforge.fenixedu.presentationTier.Action.student.careerWorkshop;

import java.io.Serializable;

public class CareerWorkshopConfirmationBean implements Serializable {

	private String confirmationCode;

	private String externalId;

	public CareerWorkshopConfirmationBean(String externalId) {
		super();
		setExternalId(externalId);
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
