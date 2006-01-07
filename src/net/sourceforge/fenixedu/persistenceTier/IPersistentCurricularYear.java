package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.CurricularYear;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */
public interface IPersistentCurricularYear extends IPersistentObject {

    public CurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia;
}