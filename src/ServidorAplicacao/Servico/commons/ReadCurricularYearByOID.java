/*
 * Created on 2003/07/29
 *
 *
 */
package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularYear;
import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadCurricularYearByOID implements IService {

    /**
     *  
     */
    public ReadCurricularYearByOID() {
    }

    public InfoCurricularYear run(Integer oid) throws FenixServiceException {

        InfoCurricularYear result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            ICurricularYear curricularYear = (ICurricularYear) persistentObject.readByOID(
                    CurricularYear.class, oid);
            if (curricularYear != null) {
                result = InfoCurricularYear.newInfoFromDomain(curricularYear);
            } else {
                throw new UnexistingCurricularYearException();
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }

    public class UnexistingCurricularYearException extends FenixServiceException {

        /**
         *  
         */
        private UnexistingCurricularYearException() {
            super();
        }

    }

}