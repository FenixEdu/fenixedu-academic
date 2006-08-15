/*
 * Created on 05/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 19/Dez/2003
 *  
 */
public class ReadProfessorshipsAndResponsibilitiesByDepartment extends Service {

    public List run(Integer departmentId, Integer executionYearId) throws FenixServiceException,
            ExcepcaoPersistencia {

        //Execution Year
        ExecutionYear executionYear = null;
        if (executionYearId != null) {
            executionYear = rootDomainObject.readExecutionYearByOID(
                    executionYearId);
        }
        //Departement
        Department department = rootDomainObject.readDepartmentByOID(departmentId);
        List teachers = department.getAllCurrentTeachers();
        Iterator iter = teachers.iterator();
       
        List<Professorship> professorships = new ArrayList<Professorship>();
        List<Professorship> responsibleFors = new ArrayList<Professorship>();
        while (iter.hasNext()) {
            Teacher teacher = (Teacher) iter.next();
            List<Professorship> teacherProfessorships = null;
            if (executionYear == null) {
                teacherProfessorships = teacher.getProfessorships();
            } else {
                teacherProfessorships = teacher.getProfessorships(executionYear);
            }
            if (teacherProfessorships != null) {
                professorships.addAll(teacherProfessorships);
            }

            List<Professorship> teacherResponsibleFors = null;
            List<Professorship> teacherResponsibleForsAux = null;
            if (executionYear == null) {
                teacherResponsibleFors = teacher.responsibleFors();
            } else {
                teacherResponsibleForsAux = teacher.responsibleFors(); 
                for(Professorship professorship : teacherResponsibleForsAux){
                    if(professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(executionYear));
                        teacherResponsibleFors.add(professorship);
                }                   
            }
            if (teacherResponsibleFors != null) {
                responsibleFors.addAll(teacherResponsibleFors);
            }
        }

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors);

        Collections.sort(detailedProfessorships, new Comparator() {

            public int compare(Object o1, Object o2) {

                DetailedProfessorship detailedProfessorship1 = (DetailedProfessorship) o1;
                DetailedProfessorship detailedProfessorship2 = (DetailedProfessorship) o2;
                int result = detailedProfessorship1.getInfoProfessorship().getInfoExecutionCourse()
                        .getIdInternal().intValue()
                        - detailedProfessorship2.getInfoProfessorship().getInfoExecutionCourse()
                                .getIdInternal().intValue();
                if (result == 0
                        && (detailedProfessorship1.getResponsibleFor().booleanValue() || detailedProfessorship2
                                .getResponsibleFor().booleanValue())) {
                    if (detailedProfessorship1.getResponsibleFor().booleanValue()) {
                        return -1;
                    }
                    if (detailedProfessorship2.getResponsibleFor().booleanValue()) {
                        return 1;
                    }
                }

                return result;
            }

        });

        List result = new ArrayList();
        iter = detailedProfessorships.iterator();
        List<DetailedProfessorship> temp = new ArrayList<DetailedProfessorship>();
        while (iter.hasNext()) {
            DetailedProfessorship detailedProfessorship = (DetailedProfessorship) iter.next();
            if (temp.isEmpty()
                    || ((DetailedProfessorship) temp.get(temp.size() - 1)).getInfoProfessorship()
                            .getInfoExecutionCourse().equals(
                                    detailedProfessorship.getInfoProfessorship()
                                            .getInfoExecutionCourse())) {
                temp.add(detailedProfessorship);
            } else {
                result.add(temp);
                temp = new ArrayList<DetailedProfessorship>();
                temp.add(detailedProfessorship);
            }
        }
        if (!temp.isEmpty()) {
            result.add(temp);
        }
        return result;
    }

    protected List getDetailedProfessorships(List professorships, final List responsibleFors) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Transformer() {

                    public Object transform(Object input) {

                        Professorship professorship = (Professorship) input;

                        InfoProfessorship infoProfessorShip = InfoProfessorshipWithAll
                                .newInfoFromDomain(professorship);

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                .getExecutionCourse());

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        detailedProfessorship.setResponsibleFor(professorship.getResponsibleFor());

                        detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                        detailedProfessorship
                                .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                        return detailedProfessorship;
                    }

                    private List getInfoCurricularCourses(ExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                .getAssociatedCurricularCourses(), new Transformer() {

                            public Object transform(Object input) {

                                CurricularCourse curricularCourse = (CurricularCourse) input;

                                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                                        .newInfoFromDomain(curricularCourse);
                                return infoCurricularCourse;
                            }
                        });
                        return infoCurricularCourses;
                    }
                });

        return detailedProfessorshipList;
    }

}