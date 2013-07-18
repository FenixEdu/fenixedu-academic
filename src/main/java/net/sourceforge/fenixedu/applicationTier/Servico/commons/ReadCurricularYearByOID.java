/*
 * Created on 2003/07/29
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadCurricularYearByOID {

    @Service
    public static InfoCurricularYear run(Integer oid) throws FenixServiceException {
        InfoCurricularYear result = null;

        CurricularYear curricularYear = RootDomainObject.getInstance().readCurricularYearByOID(oid);
        if (curricularYear != null) {
            result = InfoCurricularYear.newInfoFromDomain(curricularYear);
        } else {
            throw new UnexistingCurricularYearException();
        }

        return result;
    }

    public static class UnexistingCurricularYearException extends FenixServiceException {
        public UnexistingCurricularYearException() {
            super();
        }
    }

}