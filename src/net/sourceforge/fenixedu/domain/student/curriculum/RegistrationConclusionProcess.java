package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class RegistrationConclusionProcess extends RegistrationConclusionProcess_Base {

    private RegistrationConclusionProcess(final RegistrationConclusionBean bean) {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());

	final Registration registration = bean.getRegistration();
	final ExecutionYear conclusionYear = bean.getConclusionYear();

	check(registration, "error.RegistrationConclusionProcess.argument.must.not.be.null");
	check(conclusionYear, "error.RegistrationConclusionProcess.conclusionYear.cannot.be.null");

	super.setRegistration(registration);
	super.setConclusionYear(conclusionYear);
	addVersions(bean);
    }

    @Override
    public boolean isRegistrationConclusionProcess() {
	return true;
    }

    public static void conclude(final RegistrationConclusionBean bean) {
	if (bean.isConclusionProcessed()) {
	    throw new DomainException("error.ConclusionProcess.already.concluded.must.update");
	}

	createRegistrationStates(new RegistrationConclusionProcess(bean));
    }

    @Override
    public void update(final RegistrationConclusionBean bean) {
	if (!bean.isConclusionProcessed()) {
	    throw new DomainException("error.ConclusionProcess.is.not.concluded");
	}

	addVersions(bean);

    }

    private static void createRegistrationStates(final RegistrationConclusionProcess conclusionProcess) {
	final Registration reg = conclusionProcess.getRegistration();
	final Person responsible = conclusionProcess.getResponsible();
	final DateTime creation = conclusionProcess.getCreationDateTime();

	if (reg.getDegreeType() == DegreeType.MASTER_DEGREE
		|| reg.getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
	    if (!reg.isSchoolPartConcluded() && !reg.isConcluded()) {
		RegistrationStateCreator.createState(reg, responsible, creation, RegistrationStateType.SCHOOLPARTCONCLUDED);
	    }
	} else {
	    if (!reg.isConcluded()) {
		RegistrationStateCreator.createState(reg, responsible, creation, RegistrationStateType.CONCLUDED);
	    }
	}
    }

    @Override
    final public void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate,
	    final String notes) {
	addVersions(new RegistrationConclusionBean(getRegistration()));
	getLastVersion().update(responsible, finalAverage, conclusionDate, notes);
    }

    final public void update(final Person responsible, final Integer finalAverage, BigDecimal average,
	    final LocalDate conclusionDate, final String notes) {
	addVersions(new RegistrationConclusionBean(getRegistration()));
	getLastVersion().update(responsible, finalAverage, average, conclusionDate, notes);
    }

    @Override
    protected void addSpecificVersionInfo() {
	getLastVersion().setMasterDegreeThesis(getRegistration().getMasterDegreeThesis());
    }

    @Override
    public void setRootDomainObject(RootDomainObject rootDomainObject) {
	throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setRegistration(Registration registration) {
	throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
	throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

}
