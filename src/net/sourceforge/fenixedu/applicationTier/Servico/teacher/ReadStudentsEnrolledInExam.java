package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoomWithInfoStudentAndInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeacherStudentsEnrolledList;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class ReadStudentsEnrolledInExam implements IService {

    public Object run(Integer executionCourseCode, Integer examCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExam persistentExam = sp.getIPersistentExam();
        IPersistentSite persistentSite = sp.getIPersistentSite();
        IPersistentExamStudentRoom examStudentRoomDAO = sp.getIPersistentExamStudentRoom();

        ISite site = persistentSite.readByExecutionCourse(executionCourseCode);
        IExam exam = (IExam) persistentExam.readByOID(Exam.class, examCode);
        List examStudentRoomList = examStudentRoomDAO.readByExamOID(exam.getIdInternal());
        List infoExamStudentRoomList = (List) CollectionUtils.collect(examStudentRoomList,
                new Transformer() {

                    public Object transform(Object input) {
                        ExamStudentRoom examStudentRoom = (ExamStudentRoom) input;

                        return InfoExamStudentRoomWithInfoStudentAndInfoRoom
                                .newInfoFromDomain(examStudentRoom);

                    }
                });

                   List infoStudents = new ArrayList();
        Iterator iter = examStudentRoomList.iterator();
        while (iter.hasNext()) {
            IStudent student = ((IExamStudentRoom) iter.next()).getStudent();

            infoStudents.add(InfoStudent.newInfoFromDomain(student));
        }

        InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
        ISiteComponent component = new InfoSiteTeacherStudentsEnrolledList(infoStudents, infoExam,
                infoExamStudentRoomList);

        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);
        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                component);
        return siteView;
    }
}