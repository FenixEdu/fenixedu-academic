package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageEnrolmentModelBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationDataByExecutionYear extends RegistrationDataByExecutionYear_Base {

    public RegistrationDataByExecutionYear() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public RegistrationDataByExecutionYear(Registration registration) {
	this();
	setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	setRegistration(registration);
    }

    public RegistrationDataByExecutionYear(Registration registration, ExecutionYear executionYear) {
	this();
	setExecutionYear(executionYear);
	setRegistration(registration);
    }

    public static class EnrolmentModelFactoryEditor extends ManageEnrolmentModelBean implements
	    FactoryExecutor {
	public EnrolmentModelFactoryEditor(final Registration registration) {
	    super(registration);
	}

	public Object execute() {
	    getRegistration().setEnrolmentModelForExecutionYear(getExecutionYear(), getEnrolmentModel());
	    return null;
	}
    }

}
