

package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 21/Mar/2003
 */
public interface IPersistentCurricularYear extends IPersistentObject {

    public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia;
    public ArrayList readAll() throws ExcepcaoPersistencia;
    public void lockWrite(ICurricularYear CurricularYear) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void delete(ICurricularYear CurricularYear) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
