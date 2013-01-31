package net.sourceforge.fenixedu.domain.serviceRequests;

public enum RectorateSubmissionState {
	UNSENT, CLOSED, SENT, RECEIVED;

	public String getName() {
		return name();
	}
}
