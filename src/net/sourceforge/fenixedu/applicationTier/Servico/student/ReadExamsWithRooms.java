/*
 * ReadExamsWithRooms.java
 *
 * Created on 2003/05/28
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author João Mota
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentExamDistributions;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class ReadExamsWithRooms implements IServico {

    private static ReadExamsWithRooms _servico = new ReadExamsWithRooms();

    /**
     * The singleton access method of this class.
     */
    public static ReadExamsWithRooms getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsWithRooms() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExamsWithRooms";
    }

    public Object run(String username) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();

            IExecutionPeriod currentPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            IStudent student = persistentStudent.readByUsername(username);
            if (student == null) {
                throw new InvalidArgumentsServiceException();
            }
            List examsRoomDistribution = persistentExamStudentRoom.readByStudentOID(student.getIdInternal());
            Iterator iter = examsRoomDistribution.iterator();
            List validDistributions = new ArrayList();
            while (iter.hasNext()) {
                IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();
                IExecutionCourse executionCourse = examStudentRoom.getExam()
                        .getAssociatedExecutionCourses().get(0);
                if (currentPeriod != null && executionCourse != null
                        && executionCourse.getExecutionPeriod() != null
                        && currentPeriod.equals(executionCourse.getExecutionPeriod())) {
                    validDistributions.add(Cloner
                            .copyIExamStudentRoom2InfoExamStudentRoom(examStudentRoom));
                }
            }
            ISiteComponent component = new InfoSiteStudentExamDistributions(validDistributions);
            SiteView siteView = new SiteView(component);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}