/*
 * Created on 3/Ago/2003, 21:37:27
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * This service reads all the Curricular Courses for this student's curricular
 * plans, and not only the Curricular Courses currently in execution. For this
 * case use ReadDisciplinesByStudent Created at 3/Ago/2003, 21:37:27
 *  
 */
public class ReadCurricularCoursesByUsername implements IServico {
    private static ReadCurricularCoursesByUsername service = new ReadCurricularCoursesByUsername();

    /**
     * The singleton access method of this class.
     */
    public static ReadCurricularCoursesByUsername getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private ReadCurricularCoursesByUsername() {
    }

    /**
     * Returns The Service Name
     */
    public final String getNome() {
        return "student.ReadCurricularCoursesByUsername";
    }

    public List run(String username) throws BDException {
        List curricularCourses = new LinkedList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List curricularPlans = sp.getIStudentCurricularPlanPersistente().readByUsername(username);
            for (Iterator iterator = curricularPlans.iterator(); iterator.hasNext();) {
                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
                for (Iterator curricularCoursesIterator = studentCurricularPlan
                        .getDegreeCurricularPlan().getCurricularCourses().iterator(); curricularCoursesIterator
                        .hasNext();) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesIterator
                            .next();
                    curricularCourses.add(Cloner
                            .copyCurricularCourse2InfoCurricularCourse(curricularCourse));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve a student's curricular courses from the database",
                    ex);
        }
        return curricularCourses;
    }
}