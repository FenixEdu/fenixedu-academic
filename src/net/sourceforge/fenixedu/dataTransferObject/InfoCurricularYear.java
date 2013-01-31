package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularYear;

/**
 * @author dcs-rjao
 * 
 *         21/Mar/2003
 */

public class InfoCurricularYear extends InfoObject {

	private final CurricularYear curricularYear;

	public InfoCurricularYear(final CurricularYear curricularYear) {
		this.curricularYear = curricularYear;
	}

	public CurricularYear getCurricularYear() {
		return curricularYear;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof InfoCurricularYear && curricularYear == ((InfoCurricularYear) obj).curricularYear;
	}

	@Override
	public String toString() {
		return curricularYear == null ? null : curricularYear.toString();
	}

	public Integer getYear() {
		return curricularYear == null ? null : curricularYear.getYear();
	}

	public static InfoCurricularYear newInfoFromDomain(final CurricularYear curricularYear) {
		return curricularYear == null ? null : new InfoCurricularYear(curricularYear);
	}

	@Override
	public Integer getIdInternal() {
		return curricularYear == null ? null : curricularYear.getIdInternal();
	}

	@Override
	public void setIdInternal(Integer integer) {
		throw new Error("Method should not be called!");
	}

}
