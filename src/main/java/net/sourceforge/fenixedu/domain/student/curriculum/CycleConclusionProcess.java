package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.LocalDate;

public class CycleConclusionProcess extends CycleConclusionProcess_Base {

    private CycleConclusionProcess(final RegistrationConclusionBean bean) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        final CycleCurriculumGroup cycle = bean.getCycleCurriculumGroup();
        final ExecutionYear conclusionYear = bean.getConclusionYear();
        String[] args = {};

        if (cycle == null) {
            throw new DomainException("error.CycleConclusionProcess.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusionYear == null) {
            throw new DomainException("error.CycleConclusionProcess.argument.must.not.be.null", args1);
        }

        super.setCycle(cycle);
        super.setConclusionYear(conclusionYear);
        addVersions(bean);
    }

    @Override
    public boolean isCycleConclusionProcess() {
        return true;
    }

    public static void conclude(final RegistrationConclusionBean bean) {
        if (bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.already.concluded.must.update");
        }

        new CycleConclusionProcess(bean);
    }

    @Override
    public void update(RegistrationConclusionBean bean) {
        if (!bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.is.not.concluded");
        }

        addVersions(bean);

    }

    @Override
    final public void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate,
            final String notes) {
        addVersions(new RegistrationConclusionBean(getRegistration(), getCycle()));
        getLastVersion().update(responsible, finalAverage, conclusionDate, notes);
    }

    @Override
    protected void addSpecificVersionInfo() {
        if (getCycleType() == CycleType.SECOND_CYCLE) {
            getLastVersion().setDissertationEnrolment(getRegistration().getDissertationEnrolment());
        }
    }

    @Override
    public void setRootDomainObject(Bennu rootDomainObject) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setCycle(CycleCurriculumGroup cycle) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    public CycleType getCycleType() {
        return getCycle().getCycleType();
    }

    @Override
    public Registration getRegistration() {
        return getCycle().getRegistration();
    }

    @Deprecated
    public boolean hasCycle() {
        return getCycle() != null;
    }

}
