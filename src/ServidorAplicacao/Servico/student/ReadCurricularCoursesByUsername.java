/*
 * Created on 3/Ago/2003, 21:37:27
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.student;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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