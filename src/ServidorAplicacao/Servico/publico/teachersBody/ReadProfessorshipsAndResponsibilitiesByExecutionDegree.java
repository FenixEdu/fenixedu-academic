/*
 * Created on 19/Dez/2003
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
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ResponsibleFor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 19/Dez/2003
 *  
 */
public class ReadProfessorshipsAndResponsibilitiesByExecutionDegree implements
        IService {

    /**
     *  
     */
    public ReadProfessorshipsAndResponsibilitiesByExecutionDegree() {
    }

    public List run(Integer executionDegreeId) throws FenixServiceException {
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree = ps
                    .getICursoExecucaoPersistente();
            IPersistentProfessorship persistentProfessorship = ps
                    .getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = ps
                    .getIPersistentResponsibleFor();
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOID(CursoExecucao.class, executionDegreeId);

            List professorships = persistentProfessorship
                    .readByExecutionDegree(executionDegree);
            List responsibleFors = persistentResponsibleFor
                    .readByExecutionDegree(executionDegree);
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
            Iterator iter = detailedProfessorships.iterator();
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
                        InfoProfessorship infoProfessorShip = Cloner
                                .copyIProfessorship2InfoProfessorship(professorship);

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
                                                InfoCurricularCourse infoCurricularCourse = Cloner
                                                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                                                return infoCurricularCourse;
                                            }
                                        });
                        return infoCurricularCourses;
                    }
                });

        return detailedProfessorshipList;
    }

}