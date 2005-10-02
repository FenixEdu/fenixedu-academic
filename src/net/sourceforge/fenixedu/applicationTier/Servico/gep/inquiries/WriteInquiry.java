/*
 * Created on 11/Abr/2005 - 16:20:23
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import pt.utl.ist.berserk.logic.serviceManager.IService;
/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class WriteInquiry implements IService {

	private IPersistentObject persistentObject;
	
    public void run(final InfoInquiry inquiry, final InfoStudent infoStudent)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (inquiry == null) {
            throw new FenixServiceException("nullInquiry");
        }

        if (infoStudent == null) {
            throw new FenixServiceException("nullInfoStudent");
        }

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		persistentObject = sp.getIPersistentObject();

        // Writing the inquiries course
        final InfoInquiriesCourse iic = inquiry.getInquiriesCourse();
        final IInquiriesCourse inquiriesCourse = writeInquiriesCourse(inquiry, iic);

        // Writting the inquiries teacher

        List inquiriesTeachersList = inquiry.getInquiriesTeachersList();
        if (inquiriesTeachersList != null) {
            Iterator teacherIter = inquiriesTeachersList.iterator();
            while (teacherIter.hasNext()) {
                InfoInquiriesTeacher iit = (InfoInquiriesTeacher) teacherIter.next();
                writeInquiriesTeacher(iit, inquiriesCourse);
            }
        }

        // Writting the inquiries room
        List inquiriesRoomsList = inquiry.getInquiriesRoomsList();
        if (inquiriesRoomsList != null) {
            Iterator roomIter = inquiriesRoomsList.iterator();
            while (roomIter.hasNext()) {
                InfoInquiriesRoom iir = (InfoInquiriesRoom) roomIter.next();
                writeInquiriesRoom(iir, inquiriesCourse);
            }
        }

        // updating the registry
        IPersistentInquiriesRegistry inquiriesRegistryDAO = sp.getIPersistentInquiriesRegistry();
        writeInquiriesRegistry(inquiriesRegistryDAO, inquiriesCourse, infoStudent);
    }

    private IInquiriesCourse writeInquiriesCourse(final InfoInquiry ii, final InfoInquiriesCourse iic)
            throws ExcepcaoPersistencia {
		
		IExecutionCourse executionCourse = (IExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, ii.getExecutionCourse().getIdInternal());
		IExecutionDegree executionDegreeCourse = (IExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, iic.getExecutionDegreeCourse().getIdInternal());
        IExecutionDegree executionDegreeStudent = (IExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, ii.getExecutionDegreeStudent().getIdInternal());
		IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, ii.getExecutionPeriod().getIdInternal());
		ISchoolClass schoolClass = null;
		
        if (iic.getStudentSchoolClass() != null) {
			schoolClass = (ISchoolClass) persistentObject.readByOID(
                    SchoolClass.class, iic.getStudentSchoolClass().getIdInternal());
        }

		return DomainFactory.makeInquiriesCourse(executionCourse, executionDegreeCourse, executionDegreeStudent, executionPeriod, schoolClass, iic);
    }

    private void writeInquiriesTeacher(final InfoInquiriesTeacher iit, final IInquiriesCourse inquiriesCourse) throws ExcepcaoPersistencia {

        for (ShiftType shiftType : iit.getClassTypes()) {

			InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes = iit
                    .getTeacherOrNonAffiliatedTeacher();
            if (infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getTeacher() != null) {
				ITeacher teacher = (ITeacher) persistentObject.readByOID(Teacher.class,
						infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getIdInternal());
				inquiriesCourse.createInquiriesTeacher(teacher, shiftType, iit);
				

            } else if (infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getNonAffiliatedTeacher() != null) {
				INonAffiliatedTeacher nonAffiliatedTeacher = (INonAffiliatedTeacher) persistentObject.readByOID(
						NonAffiliatedTeacher.class, infoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes.getIdInternal());
				inquiriesCourse.createInquiriesTeacher(nonAffiliatedTeacher, shiftType, iit);
            }

        }

    }

    private void writeInquiriesRoom(final InfoInquiriesRoom iir, final IInquiriesCourse inquiriesCourse) throws ExcepcaoPersistencia {
		IRoom room = (IRoom) persistentObject.readByOID(Room.class, iir.getRoom().getIdInternal());
		inquiriesCourse.createInquiriesRoom(room, iir);

    }

    private IInquiriesRegistry writeInquiriesRegistry(
            final IPersistentInquiriesRegistry inquiriesRegistryDAO,
            final IInquiriesCourse inquiriesCourse, final InfoStudent infoStudent) throws ExcepcaoPersistencia {
		

        IStudent student = (IStudent) persistentObject.readByOID(Student.class, infoStudent
                .getIdInternal());

        return DomainFactory.makeInquiriesRegistry(inquiriesCourse.getExecutionCourse(), inquiriesCourse.getExecutionPeriod(), student);

    }
}
