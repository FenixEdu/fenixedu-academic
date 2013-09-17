package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;

public class GroupStudent extends GroupStudent_Base {

    public static final Comparator<GroupStudent> COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator("student.number");

    public static final Comparator<GroupStudent> COMPARATOR_BY_YEAR = new Comparator<GroupStudent>() {

        @Override
        public int compare(final GroupStudent gs1, final GroupStudent gs2) {
            final ExecutionDegree ed1 = gs1.getFinalDegreeDegreeWorkGroup().getExecutionDegree();
            final ExecutionDegree ed2 = gs2.getFinalDegreeDegreeWorkGroup().getExecutionDegree();
            final ExecutionYear ey1 = ed1.getExecutionYear();
            final ExecutionYear ey2 = ed2.getExecutionYear();
            return ey1 == ey2 ? ed1.getDegree().getSigla().compareTo(ed2.getDegree().getSigla()) : ey1.compareTo(ey2);
        }

    };

    public static final Comparator<GroupStudent> COMPARATOR_BY_YEAR_REVERSE = new Comparator<GroupStudent>() {

        @Override
        public int compare(final GroupStudent gs1, final GroupStudent gs2) {
            return 0 - COMPARATOR_BY_YEAR.compare(gs1, gs2);
        }

    };

    public GroupStudent() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setRootDomainObject(null);
        setFinalDegreeDegreeWorkGroup(null);
        setFinalDegreeWorkProposalConfirmation(null);
        setRegistration(null);
        deleteDomainObject();
    }

    @Override
    @Deprecated
    public Registration getStudent() {
        return getRegistration();
    }

    @Deprecated
    public boolean hasStudent() {
        return hasRegistration();
    }

    @Override
    @Deprecated
    public void setStudent(Registration registration) {
        setRegistration(registration);
    }

    public Registration getRegistration() {
        return super.getStudent();
    }

    public boolean hasRegistration() {
        return super.getStudent() != null;
    }

    public void removeRegistration() {
        super.setStudent(null);
    }

    public void setRegistration(Registration registration) {
        super.setStudent(registration);
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFinalDegreeWorkProposalConfirmation() {
        return getFinalDegreeWorkProposalConfirmation() != null;
    }

    @Deprecated
    public boolean hasFinalDegreeDegreeWorkGroup() {
        return getFinalDegreeDegreeWorkGroup() != null;
    }

}
