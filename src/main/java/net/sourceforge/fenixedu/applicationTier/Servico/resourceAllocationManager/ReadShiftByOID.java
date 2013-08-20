/*
 * Created on 2003/07/30
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadShiftByOID {

    @Service
    public static InfoShift run(final String oid) {
        final Shift shift = AbstractDomainObject.fromExternalId(oid);
        return shift == null ? null : InfoShift.newInfoFromDomain(shift);
    }

}