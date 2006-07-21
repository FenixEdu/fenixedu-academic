package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public abstract class AcademicServiceRequest extends AcademicServiceRequest_Base {

    protected AcademicServiceRequest() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setOjbConcreteClass(this.getClass().getName());
        super.setCreationDate(new DateTime());
    }

    protected AcademicServiceRequest(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice) {
        this();
        init(studentCurricularPlan, administrativeOffice);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice) {
        if (studentCurricularPlan == null) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequest.studentCurricularPlan.cannot.be.null");
        }
        if (administrativeOffice == null) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequest.administrativeOffice.cannot.be.null");
        }

    }

    protected void init(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice) {
        checkParameters(studentCurricularPlan, administrativeOffice);
        super.setAdministrativeOffice(administrativeOffice);
        super.setStudentCurricularPlan(studentCurricularPlan);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.modify.administrativeOffice");
    }

    @Override
    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.modify.studentCurricularPlan");
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.modify.creationDate");
    }

    @Override
    public void addAcademicServiceRequestSituations(
            AcademicServiceRequestSituation academicServiceRequestSituation) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.add.academicServiceRequestSituation");
    }

    @Override
    public List<AcademicServiceRequestSituation> getAcademicServiceRequestSituations() {
        return Collections.unmodifiableList(super.getAcademicServiceRequestSituations());
    }

    @Override
    public Set<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsSet() {
        return Collections.unmodifiableSet(super.getAcademicServiceRequestSituationsSet());
    }

    @Override
    public Iterator<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsIterator() {
        return getAcademicServiceRequestSituationsSet().iterator();
    }

    @Override
    public void removeAcademicServiceRequestSituations(
            AcademicServiceRequestSituation academicServiceRequestSituation) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.remove.academicServiceRequestSituation");
    }

    public boolean isNewRequest() {
        return !hasAnyAcademicServiceRequestSituations();
    }

    public AcademicServiceRequestSituation createSituation(
            AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
        return new AcademicServiceRequestSituation(this, academicServiceRequestSituationType, employee);
    }

    public AcademicServiceRequestSituation getActiveSituation() {
        return (AcademicServiceRequestSituation) Collections.max(getAcademicServiceRequestSituations(),
                new BeanComparator("creationDate"));
    }

    public final void changeState(
            AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee,
            String justification) {

        checkIfContainsSituationWithType(academicServiceRequestSituationType);

        new AcademicServiceRequestSituation(this, academicServiceRequestSituationType, employee,
                justification);

        internalChangeState(academicServiceRequestSituationType);

    }

    protected void internalChangeState(
            AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        // nothing to be done
    }

    private void checkIfContainsSituationWithType(
            AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        for (final AcademicServiceRequestSituation academicServiceRequestSituation : getAcademicServiceRequestSituations()) {
            if (academicServiceRequestSituation.getAcademicServiceRequestSituationType() == academicServiceRequestSituationType) {
                throw new DomainException(
                        "error.serviceRequests.AcademicServiceRequest.academicSituationType.already.exists");
            }
        }
    }

}
