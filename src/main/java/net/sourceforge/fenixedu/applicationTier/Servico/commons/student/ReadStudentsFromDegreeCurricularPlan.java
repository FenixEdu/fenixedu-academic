/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.StudentListByDegreeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentsFromDegreeCurricularPlan {

    protected List run(Integer degreeCurricularPlanID, DegreeType degreeType) throws FenixServiceException {
        // Read the Students
        DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);

        List students = degreeCurricularPlan.getStudentCurricularPlans();

        if ((students == null) || (students.isEmpty())) {
            throw new NonExistingServiceException();
        }

        return (List) CollectionUtils.collect(students, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
                return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
            }

        });
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsFromDegreeCurricularPlan serviceInstance = new ReadStudentsFromDegreeCurricularPlan();

    @Service
    public static List runReadStudentsFromDegreeCurricularPlan(Integer degreeCurricularPlanID, DegreeType degreeType)
            throws FenixServiceException, NotAuthorizedException {
        try {
            StudentListByDegreeAuthorizationFilter.instance.execute(degreeCurricularPlanID, degreeType);
            return serviceInstance.run(degreeCurricularPlanID, degreeType);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                return serviceInstance.run(degreeCurricularPlanID, degreeType);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}