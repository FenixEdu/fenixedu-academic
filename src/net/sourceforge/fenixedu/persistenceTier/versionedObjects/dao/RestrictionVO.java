package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class RestrictionVO extends VersionedObjectsBase implements IPersistentRestriction {

	
	
    public List readByCurricularCourseAndRestrictionClass(Integer curricularCourseKey, final Class clazz)
    	throws ExcepcaoPersistencia {
		
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class, curricularCourseKey);
		List restrictions = curricularCourse.getRestrictionsByCurricularCourse();
		
		return (List)CollectionUtils.select(restrictions, new Predicate(){
			public boolean evaluate(Object o) {
				return ((Restriction)o).getClass().getName().equals(clazz);
			}
		});

	}
}
