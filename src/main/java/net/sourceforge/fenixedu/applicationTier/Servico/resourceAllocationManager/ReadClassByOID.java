/*
 * Created on 2003/08/01
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadClassByOID {

    @Service
    public static InfoClass run(Integer oid) throws FenixServiceException {
        InfoClass result = null;
        SchoolClass turma = AbstractDomainObject.fromExternalId(oid);
        if (turma != null) {
            result = InfoClass.newInfoFromDomain(turma);
        }

        return result;
    }
}