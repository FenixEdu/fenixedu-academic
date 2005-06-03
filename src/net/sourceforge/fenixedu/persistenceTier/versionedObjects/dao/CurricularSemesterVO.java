package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class CurricularSemesterVO extends VersionedObjectsBase implements
		IPersistentCurricularSemester {

	public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(
			final Integer semester, final Integer year) throws ExcepcaoPersistencia {

		List<ICurricularSemester> curricularSemesters = (List<ICurricularSemester>) readAll(CurricularSemester.class);
		return (ICurricularSemester) CollectionUtils.find(curricularSemesters,new Predicate() {
			public boolean evaluate (Object o) {
				ICurricularSemester curricularSemester = (ICurricularSemester)o;
				return	curricularSemester.getSemester().equals(semester) && 
						curricularSemester.getCurricularYear().getYear().equals(year);
			}
		});
	}

	public List readAll() throws ExcepcaoPersistencia {
		return (List) readAll(CurricularSemester.class);
	}

}
