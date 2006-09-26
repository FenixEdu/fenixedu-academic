package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ReadStudentEnrollmentsAndClass extends Service {

    public List run(IUserView userView) throws FenixServiceException {

	final Registration registration = userView.getPerson().getStudentByType(DegreeType.DEGREE);
	if (registration == null) {
	    throw new FenixServiceException("error.ReadStudentEnrollmentsAndClass.noRegistration");
	}

	final StudentCurricularPlan studentCurricularPlan = registration
		.getActiveStudentCurricularPlan();
	final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();

	final List<Shift> studentShifts = new ArrayList();
	for (final Shift shift : studentCurricularPlan.getStudent().getShifts()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
		studentShifts.add(shift);
	    }
	}

	final InfoClass infoClass = getClass(filterStudentShifts(studentShifts), studentCurricularPlan
		.getDegreeCurricularPlan().getDegree().getName());

	final List<InfoEnrolment> infoEnrollments = new ArrayList<InfoEnrolment>(studentCurricularPlan
		.getEnrolmentsCount());
	for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
	    infoEnrollments.add(InfoEnrolment.newInfoFromDomain(enrolment));
	}

	final List result = new ArrayList(3);
	result.add(infoEnrollments);
	result.add(infoClass);
	result.add(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome());

	return result;
    }

    private InfoClass getClass(List<Shift> studentShifts, String degreeName) {

	final List<SchoolClass> schoolClassList = new ArrayList<SchoolClass>();
	for (final Shift shift : studentShifts) {
	    if (shift.getAssociatedClassesCount() == 1) {
		return InfoClass.newInfoFromDomain(shift.getAssociatedClassesSet().iterator().next());
	    }
	    for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
		if (degreeName.equals(schoolClass.getExecutionDegree().getDegree().getName())) {
		    schoolClassList.add(schoolClass);
		}
	    }
	}
	return InfoClass.newInfoFromDomain(getMaxOcurrenceElement(schoolClassList));
    }

    private List filterStudentShifts(List studentShifts) {
	return (List) CollectionUtils.select(studentShifts, new Predicate() {
	    final List<ShiftType> validTypes = Arrays.asList(new ShiftType[] { ShiftType.PRATICA,
		    ShiftType.TEORICO_PRATICA });

	    public boolean evaluate(Object input) {
		return validTypes.contains(((Shift) input).getTipo());
	    }
	});
    }

    private SchoolClass getMaxOcurrenceElement(List<SchoolClass> classes) {
	int maxNumberOfOcurrencies = 0;
	SchoolClass resultElement = null;
	for (final SchoolClass schoolClass : classes) {
	    int numberOfOcurrencis = CollectionUtils.cardinality(schoolClass, classes);
	    if (numberOfOcurrencis > maxNumberOfOcurrencies) {
		resultElement = schoolClass;
	    }
	}
	return resultElement;
    }
}
