package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CurricularCourseEquivalenceVO extends VersionedObjectsBase
		implements IPersistentCurricularCourseEquivalence {

	public CurricularCourseEquivalence readByEquivalence(
			final Integer oldCurricularCourseId,
			Integer equivalentCurricularCourseId, final Integer degreeCurricularPlanId)
			throws ExcepcaoPersistencia {
		
		CurricularCourse equivalentCurricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,equivalentCurricularCourseId);
		
		if (equivalentCurricularCourse != null) {
			List equivalences = equivalentCurricularCourse.getCurricularCourseEquivalences();
			
			return (CurricularCourseEquivalence) CollectionUtils.find(equivalences,new Predicate() {
				public boolean evaluate (Object o) {
					CurricularCourseEquivalence equivalence = (CurricularCourseEquivalence)o;
					
					return	(equivalence.getOldCurricularCourse().getIdInternal().equals(oldCurricularCourseId)) && 
							(equivalence.getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId));
				}
			});
			
		}
        
        return null;
	}

}
