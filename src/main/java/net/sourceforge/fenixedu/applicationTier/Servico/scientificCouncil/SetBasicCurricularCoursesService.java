/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class SetBasicCurricularCoursesService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static Boolean run(List curricularCoursesIds, Integer degreeCurricularPlanId) throws FenixServiceException {

        DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanId);

        List<CurricularCourse> basicCurricularCourses = degreeCurricularPlan.getCurricularCoursesByBasicAttribute(Boolean.TRUE);

        Iterator itBCCourses = basicCurricularCourses.iterator();
        CurricularCourse basicCourse;

        while (itBCCourses.hasNext()) {

            basicCourse = (CurricularCourse) itBCCourses.next();
            basicCourse.setBasic(new Boolean(false));
        }

        Iterator itId = curricularCoursesIds.iterator();

        while (itId.hasNext()) {

            CurricularCourse curricularCourseBasic =
                    (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID((Integer) itId.next());
            curricularCourseBasic.setBasic(new Boolean(true));

        }

        return true;
    }

}