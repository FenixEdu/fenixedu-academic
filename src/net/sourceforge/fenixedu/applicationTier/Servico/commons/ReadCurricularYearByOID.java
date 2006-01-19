/*
 * Created on 2003/07/29
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadCurricularYearByOID extends Service {

    public InfoCurricularYear run(Integer oid) throws FenixServiceException, ExcepcaoPersistencia {

        InfoCurricularYear result = null;

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            CurricularYear curricularYear = (CurricularYear) persistentObject.readByOID(
                    CurricularYear.class, oid);
            if (curricularYear != null) {
                result = InfoCurricularYear.newInfoFromDomain(curricularYear);
            } else {
                throw new UnexistingCurricularYearException();
            }

        return result;
    }

    public class UnexistingCurricularYearException extends FenixServiceException {

        public UnexistingCurricularYearException() {
            super();
        }

    }

}