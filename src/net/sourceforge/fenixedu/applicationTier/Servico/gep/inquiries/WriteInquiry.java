package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class WriteInquiry extends Service {
	
    public void run(final InfoInquiry inquiry, final InfoStudent infoStudent) throws FenixServiceException, ExcepcaoPersistencia {
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

    private InquiriesCourse writeInquiriesCourse(final InfoInquiry ii, final InfoInquiriesCourse iic, final InfoStudent infoStudent)
            throws ExcepcaoPersistencia {
		
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(ii.getExecutionCourse().getIdInternal());
		ExecutionDegree executionDegreeCourse = rootDomainObject.readExecutionDegreeByOID(iic.getExecutionDegreeCourse().getIdInternal());
        ExecutionDegree executionDegreeStudent = rootDomainObject.readExecutionDegreeByOID(ii.getExecutionDegreeStudent().getIdInternal());
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(ii.getExecutionPeriod().getIdInternal());
        Student student = rootDomainObject.readStudentByOID(infoStudent.getIdInternal());

		SchoolClass schoolClass = null;
        if (iic.getStudentSchoolClass() != null) {
			schoolClass = rootDomainObject.readSchoolClassByOID(iic.getStudentSchoolClass().getIdInternal());
        }

		return new InquiriesCourse(executionCourse, executionDegreeCourse, executionDegreeStudent, executionPeriod, schoolClass, iic,
                student.getEntryGradeClassification(), student.getApprovationRatioClassification(), student.getArithmeticMeanClassification());
    }

    private void writeInquiriesTeacher(final InfoInquiriesTeacher iit, final InquiriesCourse inquiriesCourse) throws ExcepcaoPersistencia {
        for (ShiftType shiftType : iit.getClassTypes()) {

			InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes = iit
                    .getTeacherOrNonAffiliatedTeacher();
            if (infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getTeacher() != null) {
				Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getIdInternal());
				inquiriesCourse.createInquiriesTeacher(teacher, shiftType, iit);
            } else if (infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getNonAffiliatedTeacher() != null) {
				NonAffiliatedTeacher nonAffiliatedTeacher = rootDomainObject.readNonAffiliatedTeacherByOID(infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getIdInternal());
				inquiriesCourse.createInquiriesTeacher(nonAffiliatedTeacher, shiftType, iit);
            }
        }
    }

    private void writeInquiriesRoom(final InfoInquiriesRoom iir, final InquiriesCourse inquiriesCourse) throws ExcepcaoPersistencia {
		OldRoom room = (OldRoom) rootDomainObject.readSpaceByOID(iir.getRoom().getIdInternal());
		inquiriesCourse.createInquiriesRoom(room, iir);
    }

    private InquiriesRegistry writeInquiriesRegistry(final InquiriesCourse inquiriesCourse, final InfoStudent infoStudent) throws ExcepcaoPersistencia {
        Student student = rootDomainObject.readStudentByOID(infoStudent.getIdInternal());
        return new InquiriesRegistry(inquiriesCourse.getExecutionCourse(), inquiriesCourse.getExecutionPeriod(), student);
    }
    
}
