package net.sourceforge.fenixedu.dataTransferObject.alumni;

import net.sourceforge.fenixedu.domain.AlumniRequestType;

public class AlumniIdentityCheckRequestBean extends AlumniErrorSendingMailBean {

	private String districtOfBirth;
	private String districtSubdivisionOfBirth;
	private String parishOfBirth;
	private AlumniRequestType requestType;

	public AlumniIdentityCheckRequestBean(AlumniRequestType requestType) {
		setRequestType(requestType);
	}

	public String getDistrictOfBirth() {
		return districtOfBirth;
	}

	public void setDistrictOfBirth(String districtOfBirth) {
		this.districtOfBirth = districtOfBirth;
	}

	public String getDistrictSubdivisionOfBirth() {
		return districtSubdivisionOfBirth;
	}

	public void setDistrictSubdivisionOfBirth(String districtSubdivisionOfBirth) {
		this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
	}

	public void setParishOfBirth(String parishOfBirth) {
		this.parishOfBirth = parishOfBirth;
	}

	public String getParishOfBirth() {
		return parishOfBirth;
	}

	public AlumniRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(AlumniRequestType requestType) {
		this.requestType = requestType;
	}
}
