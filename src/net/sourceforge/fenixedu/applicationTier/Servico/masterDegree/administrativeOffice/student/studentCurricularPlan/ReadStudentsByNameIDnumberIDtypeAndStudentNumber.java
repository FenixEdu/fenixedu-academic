package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.IdDocument;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author David Santos 2/Out/2003
 */

public class ReadStudentsByNameIDnumberIDtypeAndStudentNumber extends Service {

    private static class SearchSet extends HashSet<Registration> {

	private final String studentName;
	private final String idNumber;
	private final Integer studentNumber;

	private SearchSet(final String studentName, final String idNumber, final Integer studentNumber) {
	    this.studentName = studentName == null ? null : studentName.replaceAll("%", ".*").toLowerCase();
	    this.idNumber = idNumber;
	    this.studentNumber = studentNumber;
	}

	@Override
	public boolean add(final Registration registration) {
	    return matchesCriteria(registration) && super.add(registration);
	}

	private boolean matchesCriteria(final Registration registration) {
	    final Student student = registration.getStudent();
	    final Person person = student.getPerson();
	    if (studentName != null && !person.getName().toLowerCase().matches(studentName)) {
		return false;
	    }
	    if (idNumber != null) {
		boolean hasMatch = false;
		for (final IdDocument idDocument : person.getIdDocumentsSet()) {
		    if (idDocument.getValue().equalsIgnoreCase(idNumber)) {
			hasMatch = true;
		    }
		}
		if (!hasMatch) {
		    return false;
		}
	    }
	    if (studentNumber != null
		    && studentNumber.intValue() != registration.getNumber().intValue()
		    && studentNumber.intValue() != student.getNumber().intValue()) {
		return false;
	    }
	    return true;
	}

    }

    public List run(String studentName, String idNumber, IDDocumentType idType, Integer studentNumber) {
	final SearchSet searchSet = new SearchSet(studentName, idNumber, studentNumber);
	Registration.readMasterDegreeStudentsByNameDocIDNumberIDTypeAndStudentNumber(
		searchSet, studentName, idNumber, idType, studentNumber);

	final List<InfoStudent> result = new ArrayList<InfoStudent>();
	for (final Registration registration : searchSet) {
	    result.add(InfoStudent.newInfoFromDomain(registration));
	}
	return result;
    }

}