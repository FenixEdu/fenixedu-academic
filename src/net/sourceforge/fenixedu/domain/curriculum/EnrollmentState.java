package net.sourceforge.fenixedu.domain.curriculum;


/**
 * @author Luis Cruz
 * 
 */
public enum EnrollmentState {

    APROVED,

    NOT_APROVED,

    ENROLLED,

    TEMPORARILY_ENROLLED,

    ANNULED,

    NOT_EVALUATED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return EnrollmentState.class.getSimpleName() + "." + name();
    }

}
