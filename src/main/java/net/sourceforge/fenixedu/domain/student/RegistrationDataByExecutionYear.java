package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageEnrolmentModelBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationDataByExecutionYear extends RegistrationDataByExecutionYear_Base {

    public RegistrationDataByExecutionYear() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    public static class EnrolmentModelFactoryEditor extends ManageEnrolmentModelBean implements FactoryExecutor {
        public EnrolmentModelFactoryEditor(final Registration registration) {
            super(registration);
        }

        @Override
        public Object execute() {
            getRegistration().setEnrolmentModelForExecutionYear(getExecutionYear(), getEnrolmentModel());
            return null;
        }
    }

    public void delete() {
        setExecutionYear(null);
        setRegistration(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEnrolmentModel() {
        return getEnrolmentModel() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
