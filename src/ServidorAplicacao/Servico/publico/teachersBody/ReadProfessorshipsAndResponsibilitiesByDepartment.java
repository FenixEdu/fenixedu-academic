/*
 * Created on 05/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoProfessorship;
import DataBeans.teacher.professorship.DetailedProfessorship;
import Dominio.Department;
import Dominio.ExecutionYear;
import Dominio.ICurricularCourse;
import Dominio.IDepartment;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 19/Dez/2003
 *  
 */
public class ReadProfessorshipsAndResponsibilitiesByDepartment implements
        IService {

    /**
     *  
     */
    public ReadProfessorshipsAndResponsibilitiesByDepartment() {

    }

    public List run(Integer departmentId, Integer executionYearId)
            throws FenixServiceException {

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();

            //Execution Year
            IExecutionYear executionYear = null;
            if (executionYearId != null) {
                IPersistentExecutionYear persistentExecutionYear = ps
                        .getIPersistentExecutionYear();

                executionYear = (IExecutionYear) persistentExecutionYear
                        .readByOID(ExecutionYear.class, executionYearId);
            }

            //Departement
            IPersistentDepartment persistentDepartment = ps
                    .getIDepartamentoPersistente();
            IDepartment department = (IDepartment) persistentDepartment
                    .readByOID(Department.class, departmentId);

            IPersistentTeacher persistentTeacher = ps.getIPersistentTeacher();
            List teachers = persistentTeacher.readByDepartment(department);

            Iterator iter = teachers.iterator();
            IPersistentProfessorship persistentProfessorship = ps
                    .getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = ps
                    .getIPersistentResponsibleFor();
            List professorships = new ArrayList();
            List responsibleFors = new ArrayList();
            while (iter.hasNext()) {
                ITeacher teacher = (ITeacher) iter.next();
                List teacherProfessorships = null;
                if (executionYear == null) {
                    teacherProfessorships = persistentProfessorship
                            .readByTeacher(teacher);
                } else {
                    teacherProfessorships = persistentProfessorship
                            .readByTeacherAndExecutionYear(teacher,
                                    executionYear);
                }
                if (teacherProfessorships != null) {
                    professorships.addAll(teacherProfessorships);
                }

                List teacherResponsibleFors = null;
                if (executionYear == null) {
                    teacherResponsibleFors = persistentResponsibleFor
                            .readByTeacher(teacher);
                } else {
                    teacherResponsibleFors = persistentResponsibleFor
                            .readByTeacherAndExecutionYear(teacher,
                                    executionYear);
                }
                if (teacherResponsibleFors != null) {
                    responsibleFors.addAll(teacherResponsibleFors);
                }
            }

            List detailedProfessorships = getDetailedProfessorships(
                    professorships, responsibleFors, ps);

            Collections.sort(detailedProfessorships, new Comparator() {

                public int compare(Object o1, Object o2) {

                    DetailedProfessorship detailedProfessorship1 = (DetailedProfessorship) o1;
                    DetailedProfessorship detailedProfessorship2 = (DetailedProfessorship) o2;
                    int result = detailedProfessorship1.getInfoProfessorship()
                            .getInfoExecutionCourse().getIdInternal()
                            .intValue()
                            - detailedProfessorship2.getInfoProfessorship()
                                    .getInfoExecutionCourse().getIdInternal()
                                    .intValue();
                    if (result == 0
                            && (detailedProfessorship1.getResponsibleFor()
                                    .booleanValue() || detailedProfessorship2
                                    .getResponsibleFor().booleanValue())) {
                        if (detailedProfessorship1.getResponsibleFor()
                                .booleanValue()) {
                            return -1;
                        }
                        if (detailedProfessorship2.getResponsibleFor()
                                .booleanValue()) {
                            return 1;
                        }
                    }

                    return result;
                }

            });

            List result = new ArrayList();
            iter = detailedProfessorships.iterator();
            List temp = new ArrayList();
            while (iter.hasNext()) {
                DetailedProfessorship detailedProfessorship = (DetailedProfessorship) iter
                        .next();
                if (temp.isEmpty()
                        || ((DetailedProfessorship) temp.get(temp.size() - 1))
                                .getInfoProfessorship()
                                .getInfoExecutionCourse().equals(
                                        detailedProfessorship
                                                .getInfoProfessorship()
                                                .getInfoExecutionCourse())) {
                    temp.add(detailedProfessorship);
                } else {
                    result.add(temp);
                    temp = new ArrayList();
                    temp.add(detailedProfessorship);
                }
            }
            if (!temp.isEmpty()) {
                result.add(temp);
            }
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);

        }

    }

    protected List getDetailedProfessorships(List professorships,
            final List responsibleFors, ISuportePersistente sp) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(
                professorships, new Transformer() {

                    public Object transform(Object input) {

                        IProfessorship professorship = (IProfessorship) input;
                        //CLONER
                        //InfoProfessorship infoProfessorShip = Cloner
                                //.copyIProfessorship2InfoProfessorship(professorship);
                        InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                .getExecutionCourse());

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        IResponsibleFor responsibleFor = new ResponsibleFor();
                        responsibleFor.setExecutionCourse(professorship
                                .getExecutionCourse());
                        responsibleFor.setTeacher(professorship.getTeacher());
                        detailedProfessorship.setResponsibleFor(Boolean
                                .valueOf(responsibleFors
                                        .contains(responsibleFor)));

                        detailedProfessorship
                                .setInfoProfessorship(infoProfessorShip);
                        detailedProfessorship
                                .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                        return detailedProfessorship;
                    }

                    private List getInfoCurricularCourses(
                            IExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils
                                .collect(executionCourse
                                        .getAssociatedCurricularCourses(),
                                        new Transformer() {

                                            public Object transform(Object input) {

                                                ICurricularCourse curricularCourse = (ICurricularCourse) input;
                                                //CLONER
                                                //InfoCurricularCourse infoCurricularCourse = Cloner
                                                        //.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                                                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                                                return infoCurricularCourse;
                                            }
                                        });
                        return infoCurricularCourses;
                    }
                });

        return detailedProfessorshipList;
    }

}