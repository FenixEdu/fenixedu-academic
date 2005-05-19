package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class RestrictionVO extends VersionedObjectsBase implements IPersistentRestriction {

	
	
    public List readByCurricularCourseAndRestrictionClass(Integer curricularCourseKey, final Class clazz)
    	throws ExcepcaoPersistencia {
		
		ICurricularCourse curricularCourse = (ICurricularCourse) readByOID(CurricularCourse.class, curricularCourseKey);
		List restrictions = curricularCourse.getRestrictionsByCurricularCourse();
		
		return (List)CollectionUtils.select(restrictions, new Predicate(){
			public boolean evaluate(Object o) {
				return ((IRestriction)o).getClass().getName().equals(clazz);
			}
		});

//Criteria crit = new Criteria();
//
//crit.addEqualTo("precedentCurricularCourse.idInternal", curricularCourse.getIdInternal());
//crit.addEqualTo("ojbConcreteClass", clazz.getName());
//
//return queryList(RestrictionByCurricularCourse.class, crit);
	}
}
