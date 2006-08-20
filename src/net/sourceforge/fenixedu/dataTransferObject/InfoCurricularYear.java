package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularYear;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class InfoCurricularYear extends InfoObject {

	private final CurricularYear curricularYear;

    public InfoCurricularYear(final CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoCurricularYear && curricularYear == ((InfoCurricularYear) obj).curricularYear;
    }

    public String toString() {
    	return curricularYear.toString();
    }

    public Integer getYear() {
    	return curricularYear.getYear();
    }

    public static InfoCurricularYear newInfoFromDomain(final CurricularYear curricularYear) {
    	return curricularYear == null ? null : new InfoCurricularYear(curricularYear);
    }

	@Override
	public Integer getIdInternal() {
		return curricularYear.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}