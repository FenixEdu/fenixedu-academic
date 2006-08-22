/*
 * Created on 2003/07/30
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadShiftByOID extends Service {

    public InfoShift run(final Integer oid) throws ExcepcaoPersistencia {
        final Shift shift = rootDomainObject.readShiftByOID(oid);
        return shift == null ? null : InfoShift.newInfoFromDomain(shift);
    }

}