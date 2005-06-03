package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.ICurricularYear;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */
public interface IPersistentCurricularYear extends IPersistentObject {

    public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia;
}