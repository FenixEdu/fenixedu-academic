/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class SetBasicCurricularCoursesService extends Service {

	public boolean run(List curricularCoursesIds, Integer degreeCurricularPlanId)
			throws FenixServiceException, ExcepcaoPersistencia {

		DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

		List<CurricularCourse> basicCurricularCourses = degreeCurricularPlan.getCurricularCoursesByBasicAttribute(Boolean.TRUE);

		Iterator itBCCourses = basicCurricularCourses.iterator();
		CurricularCourse basicCourse;

		while (itBCCourses.hasNext()) {

			basicCourse = (CurricularCourse) itBCCourses.next();
			basicCourse.setBasic(new Boolean(false));
		}

		Iterator itId = curricularCoursesIds.iterator();

		while (itId.hasNext()) {

			CurricularCourse curricularCourseBasic = (CurricularCourse) rootDomainObject.readDegreeModuleByOID((Integer) itId.next());
			curricularCourseBasic.setBasic(new Boolean(true));

		}

		return true;
	}

}