/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class UpdateProfessorshipsHours implements IService {

    /**
     *  
     */
    public UpdateProfessorshipsHours() {
        super();
    }

    public Boolean run(Integer teacherId, Integer executionYearId, final HashMap hours)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherId);
            Iterator entries = hours.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry entry = (Entry) entries.next();

                String key = entry.getKey().toString();
                Integer executionCourseId = Integer.valueOf(key);
                String value = (String) entry.getValue();
                if (value != null) {
                    try {
                        Double ecHours = Double.valueOf(value);

                        IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO
                                .readByOID(ExecutionCourse.class, executionCourseId);
                        IProfessorship professorship = professorshipDAO.readByTeacherAndExecutionCourse(
                                teacher, executionCourse);

                        professorshipDAO.simpleLockWrite(professorship);

                        professorship.setHours(ecHours);

                    } catch (NumberFormatException e1) {
                        // ignored
                    }
                }
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("Problems on database!", e);
        }

        return Boolean.TRUE;
    }
}