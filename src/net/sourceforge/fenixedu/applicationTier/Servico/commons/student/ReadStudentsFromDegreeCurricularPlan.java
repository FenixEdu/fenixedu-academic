/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadStudentsFromDegreeCurricularPlan extends Service {

    public List run(Integer degreeCurricularPlanID, DegreeType degreeType) throws FenixServiceException,
            ExcepcaoPersistencia {
        // Read the Students
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(
                        degreeCurricularPlanID);

        List students = degreeCurricularPlan.getStudentCurricularPlans();

        if ((students == null) || (students.isEmpty())) {
            throw new NonExistingServiceException();
        }

        return (List) CollectionUtils.collect(students, new Transformer() {
            public Object transform(Object arg0) {
                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
                return InfoStudentCurricularPlan
                        .newInfoFromDomain(studentCurricularPlan);
            }

        });
    }
}