/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsAbstractService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author jpvl
 */
public class ReadProfessorships extends ReadDetailedTeacherProfessorshipsAbstractService {

    protected List run(IUserView userView, Integer executionPeriodCode) {

        ExecutionSemester executionSemester = null;
        if (executionPeriodCode != null) {
            executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodCode);
        }

        Teacher teacher = Teacher.readTeacherByUsername(userView.getUtilizador());

        List<Professorship> professorships = teacher.getProfessorships();
        List<Professorship> professorshipsList = new ArrayList<Professorship>(professorships);
        if (executionSemester != null) {
            Iterator iterProfessorships = professorships.iterator();
            while (iterProfessorships.hasNext()) {
                Professorship professorship = (Professorship) iterProfessorships.next();
                if (!professorship.getExecutionCourse().getExecutionPeriod().equals(executionSemester)) {
                    professorshipsList.remove(professorship);
                }
            }
        }

        final List<Professorship> responsibleFors = teacher.responsibleFors();
        List<Professorship> responsibleForsList = new ArrayList<Professorship>(responsibleFors);
        if (executionSemester != null) {
            Iterator iterResponsibleFors = responsibleFors.iterator();
            while (iterResponsibleFors.hasNext()) {
                Professorship responsibleFor = (Professorship) iterResponsibleFors.next();
                if (!responsibleFor.getExecutionCourse().getExecutionPeriod().equals(executionSemester)) {
                    responsibleForsList.remove(responsibleFor);
                }
            }
        }

        List detailedProfessorshipList = getDetailedProfessorships(professorshipsList, responsibleForsList);
        return detailedProfessorshipList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadProfessorships serviceInstance = new ReadProfessorships();

    @Service
    public static List runReadProfessorships(IUserView userView, Integer executionPeriodCode) throws NotAuthorizedException {
        TeacherAuthorizationFilter.instance.execute();
        return serviceInstance.run(userView, executionPeriodCode);
    }

}