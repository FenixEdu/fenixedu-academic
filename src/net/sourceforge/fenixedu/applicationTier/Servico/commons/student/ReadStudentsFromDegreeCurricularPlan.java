/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentsFromDegreeCurricularPlan implements IService {

    public List run(Integer degreeCurricularPlanID, DegreeType degreeType) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read the Students
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        List students = degreeCurricularPlan.getStudentCurricularPlans();

        if ((students == null) || (students.isEmpty())) {
            throw new NonExistingServiceException();
        }

        return (List) CollectionUtils.collect(students, new Transformer() {
            public Object transform(Object arg0) {
                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) arg0;
                return InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                        .newInfoFromDomain(studentCurricularPlan);
            }

        });
    }
}