package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CurricularSemesterVO extends VersionedObjectsBase implements
		IPersistentCurricularSemester {

	public CurricularSemester readCurricularSemesterBySemesterAndCurricularYear(
			final Integer semester, final Integer year) throws ExcepcaoPersistencia {

		List<CurricularSemester> curricularSemesters = (List<CurricularSemester>) readAll(CurricularSemester.class);
		return (CurricularSemester) CollectionUtils.find(curricularSemesters,new Predicate() {
			public boolean evaluate (Object o) {
				CurricularSemester curricularSemester = (CurricularSemester)o;
				return	curricularSemester.getSemester().equals(semester) && 
						curricularSemester.getCurricularYear().getYear().equals(year);
			}
		});
	}

	public List readAll() throws ExcepcaoPersistencia {
		return (List) readAll(CurricularSemester.class);
	}

}
