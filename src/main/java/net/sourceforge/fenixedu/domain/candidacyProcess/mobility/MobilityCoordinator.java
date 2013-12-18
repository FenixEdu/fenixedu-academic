package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class MobilityCoordinator extends MobilityCoordinator_Base {

    private MobilityCoordinator() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityCoordinator(final MobilityApplicationProcess applicationProcess, final ErasmusCoordinatorBean bean) {
        this(applicationProcess, bean.getTeacher(), bean.getDegree());
    }

    public MobilityCoordinator(final MobilityApplicationProcess applicationProcess, final Teacher teacher, final Degree degree) {
        this();

        check(applicationProcess, teacher, degree);

        this.setMobilityApplicationProcess(applicationProcess);
        this.setTeacher(teacher);
        this.setDegree(degree);
    }

    private void check(final MobilityApplicationProcess applicationProcess, final Teacher teacher, final Degree degree) {
        if (applicationProcess == null) {
            throw new DomainException("error.erasmus.coordinator.candidacyProcess.must.not.be.null");
        }

        if (teacher == null) {
            throw new DomainException("error.erasmus.coordinator.teacher.must.not.be.null");
        }

        if (degree == null) {
            throw new DomainException("error.erasmus.coordinator.degree.must.not.be.null");
        }

        if (applicationProcess.isTeacherErasmusCoordinatorForDegree(teacher, degree)) {
            throw new DomainException("error.erasmus.coordinator.teacher.is.assigned.for.process.and.degree");
        }
    }

    public void delete() {
        setDegree(null);
        setMobilityApplicationProcess(null);
        setTeacher(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasMobilityApplicationProcess() {
        return getMobilityApplicationProcess() != null;
    }

}
