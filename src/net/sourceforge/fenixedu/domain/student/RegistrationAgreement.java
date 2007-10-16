package net.sourceforge.fenixedu.domain.student;

public enum RegistrationAgreement {

    NORMAL(true), AFA(false), MA(false), NC(false), ERASMUS(false), SOCRATES(false), SOCRATES_ERASMUS(false), TEMPUS(false), BILATERAL_AGREEMENT(
	    false), ALFA2(false), UNIFOR(false), TIME(false), TOTAL(false), OTHER_EXTERNAL(false);

    private boolean enrolmentByStudentAllowed;

    private RegistrationAgreement(final boolean enrolmentByStudentAllowed) {
	this.enrolmentByStudentAllowed = enrolmentByStudentAllowed;
    }

    public boolean isNormal() {
	return this.equals(RegistrationAgreement.NORMAL);
    }

    public boolean isMilitaryAgreement() {
	return this.equals(RegistrationAgreement.AFA) || this.equals(RegistrationAgreement.MA);
    }

    public String getName() {
	return name();
    }

    public boolean isEnrolmentByStudentAllowed() {
	return this.enrolmentByStudentAllowed;
    }

    public static RegistrationAgreement getByLegacyCode(int code) {

	switch (code) {
	case 1:
	    return AFA;
	case 2:
	    return MA;
	case 3:
	    return NC;
	case 4:
	    return ERASMUS;
	case 5:
	    return SOCRATES;
	case 6:
	    return SOCRATES_ERASMUS;
	case 7:
	    return TEMPUS;
	case 8:
	    return BILATERAL_AGREEMENT;
	case 9:
	    return ALFA2;
	case 10:
	    return UNIFOR;
	}

	return null;
    }

}
