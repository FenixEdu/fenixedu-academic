/**
 * Jul 27, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadActiveStudentCurricularPlanByNumberAndType implements IService {

    public InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan run(
            Integer studentNumber, DegreeType degreeType) throws ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = suportePersistente
                .getIStudentCurricularPlanPersistente();

        IStudentCurricularPlan scp = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(studentNumber, degreeType);

        InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan infoSCP = 
            InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan.newInfoFromDomain(scp);

        List infoCurricularCourses = (List) CollectionUtils.collect(scp.getDegreeCurricularPlan()
                .getCurricularCourses(), new Transformer() {

            public Object transform(Object arg0) {
                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
            }
        });

        infoSCP.getInfoDegreeCurricularPlan().setCurricularCourses(infoCurricularCourses);
        return infoSCP;
    }

}
