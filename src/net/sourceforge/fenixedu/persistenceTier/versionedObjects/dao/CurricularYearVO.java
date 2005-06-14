package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularYear;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CurricularYearVO extends VersionedObjectsBase implements
		IPersistentCurricularYear {

	public ICurricularYear readCurricularYearByYear(final Integer year)
			throws ExcepcaoPersistencia {

		List curricularYears = (List) readAll(CurricularYear.class);
		
		return (ICurricularYear) CollectionUtils.find(curricularYears,new Predicate() {
			public boolean evaluate (Object o) {
				
				return ((ICurricularYear)o).getYear().equals(year);
			}
		});
	}

}
