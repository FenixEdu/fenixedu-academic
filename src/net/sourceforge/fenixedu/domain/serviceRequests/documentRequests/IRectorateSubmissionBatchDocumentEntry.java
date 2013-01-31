package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.student.Student;

public interface IRectorateSubmissionBatchDocumentEntry {
	public RegistryCode getRegistryCode();

	public DocumentRequestType getDocumentRequestType();

	public CycleType getRequestedCycle();

	public String getProgrammeTypeDescription();

	public Student getStudent();

	public Person getPerson();

	public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType();

	public GeneratedDocument getLastGeneratedDocument();

	public String getViewStudentProgrammeLink();

	public String getReceivedActionLink();

	public boolean isProgrammeLinkVisible();

}
