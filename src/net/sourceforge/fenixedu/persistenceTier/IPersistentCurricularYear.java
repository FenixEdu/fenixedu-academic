package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularYear;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */
public interface IPersistentCurricularYear extends IPersistentObject {

    public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(ICurricularYear CurricularYear) throws ExcepcaoPersistencia;

}