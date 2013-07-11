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
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class SetBasicCurricularCoursesService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static Boolean run(List<String> curricularCoursesIds, String degreeCurricularPlanId) throws FenixServiceException {

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        List<CurricularCourse> basicCurricularCourses = degreeCurricularPlan.getCurricularCoursesByBasicAttribute(Boolean.TRUE);

        Iterator itBCCourses = basicCurricularCourses.iterator();
        CurricularCourse basicCourse;

        while (itBCCourses.hasNext()) {

            basicCourse = (CurricularCourse) itBCCourses.next();
            basicCourse.setBasic(new Boolean(false));
        }

        Iterator<String> itId = curricularCoursesIds.iterator();

        while (itId.hasNext()) {

            CurricularCourse curricularCourseBasic = (CurricularCourse) FenixFramework.getDomainObject(itId.next());
            curricularCourseBasic.setBasic(new Boolean(true));

        }

        return true;
    }

}