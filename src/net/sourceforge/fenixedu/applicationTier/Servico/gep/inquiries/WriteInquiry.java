package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class WriteInquiry extends FenixService {

    @Service
    public static void run(final InfoInquiry inquiry, final InfoStudent infoStudent) throws FenixServiceException {
	if (inquiry == null) {
	    throw new FenixServiceException("nullInquiry");
	}

	if (infoStudent == null) {
	    throw new FenixServiceException("nullInfoStudent");
	}

	// Writing the inquiries course
	final InfoInquiriesCourse iic = inquiry.getInquiriesCourse();
	final InquiriesCourse inquiriesCourse = writeInquiriesCourse(inquiry, iic, infoStudent);

	// Writting the inquiries teacher
	List<InfoInquiriesTeacher> inquiriesTeachersList = inquiry.getInquiriesTeachersList();
	if (inquiriesTeachersList != null) {
	    for (InfoInquiriesTeacher infoInquiriesTeacher : inquiriesTeachersList) {
		writeInquiriesTeacher(infoInquiriesTeacher, inquiriesCourse);
	    }
	}

	// Writting the inquiries room
	List<InfoInquiriesRoom> inquiriesRoomsList = inquiry.getInquiriesRoomsList();
	if (inquiriesRoomsList != null) {
	    for (InfoInquiriesRoom infoInquiriesRoom : inquiriesRoomsList) {
		writeInquiriesRoom(infoInquiriesRoom, inquiriesCourse);
	    }
	}

	// updating the registry
	writeInquiriesRegistry(inquiriesCourse, infoStudent);
    }

    private static InquiriesCourse writeInquiriesCourse(final InfoInquiry ii, final InfoInquiriesCourse iic,
	    final InfoStudent infoStudent) {

	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(ii.getExecutionCourse().getIdInternal());
	ExecutionDegree executionDegreeCourse = rootDomainObject.readExecutionDegreeByOID(iic.getExecutionDegreeCourse()
		.getIdInternal());
	ExecutionDegree executionDegreeStudent = rootDomainObject.readExecutionDegreeByOID(ii.getExecutionDegreeStudent()
		.getIdInternal());
	ExecutionSemester executionSemester = rootDomainObject
		.readExecutionSemesterByOID(ii.getExecutionPeriod().getIdInternal());
	Registration registration = rootDomainObject.readRegistrationByOID(infoStudent.getIdInternal());

	SchoolClass schoolClass = null;
	if (iic.getStudentSchoolClass() != null) {
	    schoolClass = rootDomainObject.readSchoolClassByOID(iic.getStudentSchoolClass().getIdInternal());
	}

	return new InquiriesCourse(executionCourse, executionDegreeCourse, executionDegreeStudent, executionSemester,
		schoolClass, iic, registration.getEntryGradeClassification(), registration.getApprovationRatioClassification(),
		registration.getArithmeticMeanClassification());
    }

    private static void writeInquiriesTeacher(final InfoInquiriesTeacher iit, final InquiriesCourse inquiriesCourse) {
	for (ShiftType shiftType : iit.getClassTypes()) {

	    InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes = iit
		    .getTeacherOrNonAffiliatedTeacher();
	    if (infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getPerson() != null) {
		final Person person = infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getPerson().getPerson();
		final Professorship professorship = inquiriesCourse.getExecutionCourse().getProfessorship(person);
		inquiriesCourse.createInquiriesTeacher(professorship, shiftType, iit);
	    } else if (infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getNonAffiliatedTeacher() != null) {
		NonAffiliatedTeacher nonAffiliatedTeacher = rootDomainObject
			.readNonAffiliatedTeacherByOID(infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getIdInternal());
		inquiriesCourse.createInquiriesTeacher(nonAffiliatedTeacher, shiftType, iit);
	    }
	}
    }

    private static void writeInquiriesRoom(final InfoInquiriesRoom iir, final InquiriesCourse inquiriesCourse) {
	AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(iir.getRoom().getIdInternal());
	inquiriesCourse.createInquiriesRoom(room, iir);
    }

    private static InquiriesRegistry writeInquiriesRegistry(final InquiriesCourse inquiriesCourse, final InfoStudent infoStudent) {
	Registration registration = rootDomainObject.readRegistrationByOID(infoStudent.getIdInternal());
	return new InquiriesRegistry(inquiriesCourse.getExecutionCourse(), inquiriesCourse.getExecutionPeriod(), registration);
    }

}