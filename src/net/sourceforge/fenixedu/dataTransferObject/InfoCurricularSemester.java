package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;

/**
 * @author dcs-rjao
 * 
 *         19/Mar/2003
 */

public class InfoCurricularSemester extends InfoObject {

	private final CurricularSemester curricularSemester;

	public InfoCurricularSemester(final CurricularSemester curricularSemester) {
		this.curricularSemester = curricularSemester;
	}

	public CurricularSemester getCurricularSemester() {
		return curricularSemester;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof InfoCurricularSemester
				&& getCurricularSemester() == ((InfoCurricularSemester) obj).getCurricularSemester();
	}

	@Override
	public String toString() {
		return getCurricularSemester().toString();
	}

	public InfoCurricularYear getInfoCurricularYear() {
		return InfoCurricularYear.newInfoFromDomain(getCurricularSemester().getCurricularYear());
	}

	public Integer getSemester() {
		return getCurricularSemester().getSemester();
	}

	public List getInfoScopes() {
		final List<InfoCurricularCourseScope> scopes = new ArrayList<InfoCurricularCourseScope>();
		for (final CurricularCourseScope scope : getCurricularSemester().getScopesSet()) {
			scopes.add(InfoCurricularCourseScope.newInfoFromDomain(scope));
		}
		return scopes;
	}

	public static InfoCurricularSemester newInfoFromDomain(final CurricularSemester curricularSemester) {
		return curricularSemester == null ? null : new InfoCurricularSemester(curricularSemester);
	}

	@Override
	public Integer getIdInternal() {
		return getCurricularSemester().getIdInternal();
	}

	@Override
	public void setIdInternal(Integer integer) {
		throw new Error("Method should not be called!");
	}

}
