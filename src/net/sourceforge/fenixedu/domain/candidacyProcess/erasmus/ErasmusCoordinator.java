package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ErasmusCoordinator extends ErasmusCoordinator_Base {
    
    private ErasmusCoordinator() {
        super();

	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ErasmusCoordinator(final ErasmusCandidacyProcess candidacyProcess, final ErasmusCoordinatorBean bean) {
	this(candidacyProcess, bean.getTeacher(), bean.getDegree());
    }

    public ErasmusCoordinator(final ErasmusCandidacyProcess candidacyProcess, final Teacher teacher, final Degree degree) {
	super();

	check(candidacyProcess, teacher, degree);

	this.setProcess(candidacyProcess);
	this.setTeacher(teacher);
	this.setDegree(degree);
    }

    private void check(final ErasmusCandidacyProcess candidacyProcess, final Teacher teacher, final Degree degree) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.erasmus.coordinator.candidacyProcess.must.not.be.null");
	}

	if (teacher == null) {
	    throw new DomainException("error.erasmus.coordinator.teacher.must.not.be.null");
	}

	if (degree == null) {
	    throw new DomainException("error.erasmus.coordinator.degree.must.not.be.null");
	}

	if (candidacyProcess.isTeacherErasmusCoordinatorForDegree(teacher, degree)) {
	    throw new DomainException("error.erasmus.coordinator.teacher.is.assigned.for.process.and.degree");
	}
    }

    public void delete() {
	removeDegree();
	removeProcess();
	removeTeacher();
	removeRootDomainObject();

	super.deleteDomainObject();
    }

}
