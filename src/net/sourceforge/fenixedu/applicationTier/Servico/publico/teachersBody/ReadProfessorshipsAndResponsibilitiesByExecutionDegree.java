/*
 * Created on 19/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 19/Dez/2003
 *  
 */
public class ReadProfessorshipsAndResponsibilitiesByExecutionDegree implements IService {

    /**
     *  
     */
    public ReadProfessorshipsAndResponsibilitiesByExecutionDegree() {
    }

    public List run(Integer executionDegreeId) throws FenixServiceException {
        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree persistentExecutionDegree = ps.getIPersistentExecutionDegree();
            IPersistentProfessorship persistentProfessorship = ps.getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = ps.getIPersistentResponsibleFor();
            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeId);

            List professorships = persistentProfessorship.readByExecutionDegree(executionDegree);
            List responsibleFors = persistentResponsibleFor.readByExecutionDegree(executionDegree);
            List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors, ps);

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
            Iterator iter = detailedProfessorships.iterator();
            List temp = new ArrayList();
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

    protected List getDetailedProfessorships(List professorships, final List responsibleFors,
            ISuportePersistente sp) {
        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Transformer() {

                    public Object transform(Object input) {
                        IProfessorship professorship = (IProfessorship) input;
                        //CLONER
                        //InfoProfessorship infoProfessorShip = Cloner
                        //.copyIProfessorship2InfoProfessorship(professorship);
                        InfoProfessorship infoProfessorShip = InfoProfessorshipWithAll
                                .newInfoFromDomain(professorship);

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                .getExecutionCourse());

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        IResponsibleFor responsibleFor = new ResponsibleFor();
                        responsibleFor.setExecutionCourse(professorship.getExecutionCourse());
                        responsibleFor.setTeacher(professorship.getTeacher());
                        detailedProfessorship.setResponsibleFor(Boolean.valueOf(responsibleFors
                                .contains(responsibleFor)));

                        detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                        detailedProfessorship
                                .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                        return detailedProfessorship;
                    }

                    private List getInfoCurricularCourses(IExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                .getAssociatedCurricularCourses(), new Transformer() {

                            public Object transform(Object input) {
                                ICurricularCourse curricularCourse = (ICurricularCourse) input;

                                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
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