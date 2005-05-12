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
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRoom;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesTeacher;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRoom;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesCourse;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRoom;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.util.TipoAula;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class WriteInquiry implements IService {

    public void run(final InfoInquiry inquiry, final InfoStudent infoStudent)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (inquiry == null) {
            throw new FenixServiceException("nullInquiry");
        }

        if (infoStudent == null) {
            throw new FenixServiceException("nullInfoStudent");
        }

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Writing the inquiries course
        IPersistentInquiriesCourse inquiriesCourseDAO = sp.getIPersistentInquiriesCourse();
        final InfoInquiriesCourse iic = inquiry.getInquiriesCourse();
        final IInquiriesCourse inquiriesCourse = writeInquiriesCourse(inquiriesCourseDAO, inquiry, iic,
                sp);

        // Writting the inquiries teacher
        IPersistentInquiriesTeacher inquiriesTeacherDAO = sp.getIPersistentInquiriesTeacher();

        List inquiriesTeachersList = inquiry.getInquiriesTeachersList();
        if (inquiriesTeachersList != null) {
            Iterator teacherIter = inquiriesTeachersList.iterator();
            while (teacherIter.hasNext()) {
                InfoInquiriesTeacher iit = (InfoInquiriesTeacher) teacherIter.next();
                writeInquiriesTeacher(inquiriesTeacherDAO, iit, inquiriesCourse, sp);
            }
        }

        // Writting the inquiries room
        IPersistentInquiriesRoom inquiriesRoomDAO = sp.getIPersistentInquiriesRoom();

        List inquiriesRoomsList = inquiry.getInquiriesRoomsList();
        if (inquiriesRoomsList != null) {
            Iterator roomIter = inquiriesRoomsList.iterator();
            while (roomIter.hasNext()) {
                InfoInquiriesRoom iir = (InfoInquiriesRoom) roomIter.next();
                writeInquiriesRoom(inquiriesRoomDAO, iir, inquiriesCourse, sp);
            }
        }

        // updating the registry
        IPersistentInquiriesRegistry inquiriesRegistryDAO = sp.getIPersistentInquiriesRegistry();
        writeInquiriesRegistry(inquiriesRegistryDAO, inquiriesCourse, infoStudent, sp);
    }

    private IInquiriesCourse writeInquiriesCourse(final IPersistentInquiriesCourse inquiriesCourseDAO,
            final InfoInquiry ii, final InfoInquiriesCourse iic, final ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        final IInquiriesCourse inquiriesCourse = new InquiriesCourse();

        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
        inquiriesCourse.setExecutionCourse((IExecutionCourse) executionCourseDAO.readByOID(
                ExecutionCourse.class, ii.getExecutionCourse().getIdInternal()));

        IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
        inquiriesCourse.setExecutionDegreeCourse((IExecutionDegree) executionDegreeDAO.readByOID(
                ExecutionDegree.class, iic.getExecutionDegreeCourse().getIdInternal()));
        inquiriesCourse.setExecutionDegreeStudent((IExecutionDegree) executionDegreeDAO.readByOID(
                ExecutionDegree.class, ii.getExecutionDegreeStudent().getIdInternal()));

        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        inquiriesCourse.setExecutionPeriod((IExecutionPeriod) executionPeriodDAO.readByOID(
                ExecutionPeriod.class, ii.getExecutionPeriod().getIdInternal()));

        if (iic.getStudentSchoolClass() != null) {
            ITurmaPersistente schoolClassDAO = sp.getITurmaPersistente();
            inquiriesCourse.setStudentSchoolClass((ISchoolClass) schoolClassDAO.readByOID(
                    SchoolClass.class, iic.getStudentSchoolClass().getIdInternal()));
        }

        inquiriesCourse.setStudentCurricularYear(iic.getStudentCurricularYear());
        inquiriesCourse.setStudentFirstEnrollment(iic.getStudentFirstEnrollment());
        inquiriesCourse.setClassCoordination(iic.getClassCoordination());
        inquiriesCourse.setStudyElementsContribution(iic.getStudyElementsContribution());
        inquiriesCourse.setPreviousKnowledgeArticulation(iic.getPreviousKnowledgeArticulation());
        inquiriesCourse.setContributionForGraduation(iic.getContributionForGraduation());
        inquiriesCourse.setEvaluationMethodAdequation(iic.getEvaluationMethodAdequation());
        inquiriesCourse.setWeeklySpentHours(iic.getWeeklySpentHours());
        inquiriesCourse.setGlobalAppreciation(iic.getGlobalAppreciation());
        inquiriesCourseDAO.simpleLockWrite(inquiriesCourse);

        return inquiriesCourse;
    }

    private void writeInquiriesTeacher(final IPersistentInquiriesTeacher inquiriesTeacherDAO,
            final InfoInquiriesTeacher iit, final IInquiriesCourse inquiriesCourse,
            final ISuportePersistente sp) throws ExcepcaoPersistencia {

        for (TipoAula classType : iit.getClassTypes()) {
            final IInquiriesTeacher inquiriesTeacher = new InquiriesTeacher();

            inquiriesTeacher.setExecutionCourse(inquiriesCourse.getExecutionCourse());
            inquiriesTeacher.setExecutionDegreeCourse(inquiriesCourse.getExecutionDegreeCourse());
            inquiriesTeacher.setExecutionDegreeStudent(inquiriesCourse.getExecutionDegreeStudent());
            inquiriesTeacher.setExecutionPeriod(inquiriesCourse.getExecutionPeriod());
            inquiriesTeacher.setInquiriesCourse(inquiriesCourse);

            InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacher = iit
                    .getTeacherOrNonAffiliatedTeacher();
            if (teacher.getTeacher() != null) {
                IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
                inquiriesTeacher.setTeacher((ITeacher) teacherDAO.readByOID(Teacher.class, teacher
                        .getIdInternal()));

            } else if (teacher.getNonAffiliatedTeacher() != null) {
                IPersistentNonAffiliatedTeacher nonAffiliatedTeacherDAO = sp
                        .getIPersistentNonAffiliatedTeacher();
                inquiriesTeacher.setNonAffiliatedTeacher((INonAffiliatedTeacher) nonAffiliatedTeacherDAO
                        .readByOID(NonAffiliatedTeacher.class, teacher.getIdInternal()));
            }

            inquiriesTeacher.setClassType(classType.getTipo());
            inquiriesTeacher.setStudentAssiduity(iit.getStudentAssiduity());
            inquiriesTeacher.setTeacherAssiduity(iit.getTeacherAssiduity());
            inquiriesTeacher.setTeacherPunctuality(iit.getTeacherPunctuality());
            inquiriesTeacher.setTeacherClarity(iit.getTeacherClarity());
            inquiriesTeacher.setTeacherAssurance(iit.getTeacherAssurance());
            inquiriesTeacher.setTeacherInterestStimulation(iit.getTeacherInterestStimulation());
            inquiriesTeacher.setTeacherAvailability(iit.getTeacherAvailability());
            inquiriesTeacher.setTeacherReasoningStimulation(iit.getTeacherReasoningStimulation());
            inquiriesTeacher.setGlobalAppreciation(iit.getGlobalAppreciation());

            inquiriesTeacherDAO.simpleLockWrite(inquiriesTeacher);

        }

    }

    private void writeInquiriesRoom(final IPersistentInquiriesRoom inquiriesRoomDAO,
            final InfoInquiriesRoom iir, final IInquiriesCourse inquiriesCourse,
            final ISuportePersistente sp) throws ExcepcaoPersistencia {
        final IInquiriesRoom inquiriesRoom = new InquiriesRoom();

        inquiriesRoom.setExecutionCourse(inquiriesCourse.getExecutionCourse());
        inquiriesRoom.setExecutionDegreeCourse(inquiriesCourse.getExecutionDegreeCourse());
        inquiriesRoom.setExecutionDegreeStudent(inquiriesCourse.getExecutionDegreeStudent());
        inquiriesRoom.setExecutionPeriod(inquiriesCourse.getExecutionPeriod());
        inquiriesRoom.setInquiriesCourse(inquiriesCourse);

        ISalaPersistente roomDAO = sp.getISalaPersistente();
        inquiriesRoom.setRoom((IRoom) roomDAO.readByOID(Room.class, iir.getRoom().getIdInternal()));

        inquiriesRoom.setEnvironmentalConditions(iir.getEnvironmentalConditions());
        inquiriesRoom.setEquipmentQuality(iir.getEquipmentQuality());
        inquiriesRoom.setSpaceAdequation(iir.getSpaceAdequation());

        inquiriesRoomDAO.simpleLockWrite(inquiriesRoom);
    }

    private IInquiriesRegistry writeInquiriesRegistry(
            final IPersistentInquiriesRegistry inquiriesRegistryDAO,
            final IInquiriesCourse inquiriesCourse, final InfoStudent infoStudent,
            final ISuportePersistente sp) throws ExcepcaoPersistencia {
        final IInquiriesRegistry inquiriesRegistry = new InquiriesRegistry();
        inquiriesRegistry.setExecutionCourse(inquiriesCourse.getExecutionCourse());
        inquiriesRegistry.setExecutionDegreeCourse(inquiriesCourse.getExecutionDegreeCourse());
        inquiriesRegistry.setExecutionDegreeStudent(inquiriesCourse.getExecutionDegreeStudent());
        inquiriesRegistry.setExecutionPeriod(inquiriesCourse.getExecutionPeriod());

        IPersistentStudent studentDAO = sp.getIPersistentStudent();
        inquiriesRegistry.setStudent((IStudent) studentDAO.readByOID(Student.class, infoStudent
                .getIdInternal()));

        inquiriesRegistryDAO.simpleLockWrite(inquiriesRegistry);
        return inquiriesRegistry;
    }
}
