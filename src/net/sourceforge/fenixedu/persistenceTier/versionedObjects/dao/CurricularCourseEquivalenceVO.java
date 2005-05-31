package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class CurricularCourseEquivalenceVO extends VersionedObjectsBase
		implements IPersistentCurricularCourseEquivalence {

	public ICurricularCourseEquivalence readByEquivalence(
			final Integer oldCurricularCourseId,
			Integer equivalentCurricularCourseId, final Integer degreeCurricularPlanId)
			throws ExcepcaoPersistencia {
		
		ICurricularCourse equivalentCurricularCourse = (ICurricularCourse) readByOID(CurricularCourse.class,equivalentCurricularCourseId);
		
		if (equivalentCurricularCourse != null) {
			List equivalences = equivalentCurricularCourse.getCurricularCourseEquivalences();
			
			return (ICurricularCourseEquivalence) CollectionUtils.find(equivalences,new Predicate() {
				public boolean evaluate (Object o) {
					ICurricularCourseEquivalence equivalence = (ICurricularCourseEquivalence)o;
					
					return	(equivalence.getOldCurricularCourse().getIdInternal().equals(oldCurricularCourseId)) && 
							(equivalence.getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId));
				}
			});
			
		}
		else
			return null;
	}

}
