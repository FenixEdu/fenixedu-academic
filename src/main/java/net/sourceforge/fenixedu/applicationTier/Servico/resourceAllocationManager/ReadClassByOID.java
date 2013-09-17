/*
 * Created on 2003/08/01
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadClassByOID {

    @Atomic
    public static InfoClass run(String oid) throws FenixServiceException {
        InfoClass result = null;
        SchoolClass turma = FenixFramework.getDomainObject(oid);
        if (turma != null) {
            result = InfoClass.newInfoFromDomain(turma);
        }

        return result;
    }
}