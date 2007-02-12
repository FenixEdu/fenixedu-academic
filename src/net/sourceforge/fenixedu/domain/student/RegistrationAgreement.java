package net.sourceforge.fenixedu.domain.student;

public enum RegistrationAgreement {

    NORMAL, AFA, MA, NC, ERASMUS, SOCRATES, SOCRATES_ERASMUS, TEMPUS, BILATERAL_AGREEMENT, ALFA2, UNIFOR, OTHER_EXTERNAL;

    public boolean isNormal() {
	return this.equals(RegistrationAgreement.NORMAL);
    }

    public boolean isMilitaryAgreement() {
	return this.equals(RegistrationAgreement.AFA) || this.equals(RegistrationAgreement.MA);
    }

}
