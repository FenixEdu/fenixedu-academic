/*
 * Created on 2003/07/29
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadCurricularYearByOID implements IService {

    public InfoCurricularYear run(Integer oid) throws FenixServiceException {

        InfoCurricularYear result = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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

        public UnexistingCurricularYearException() {
            super();
        }

    }

}