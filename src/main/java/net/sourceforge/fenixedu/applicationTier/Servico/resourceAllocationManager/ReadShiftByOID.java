/*
 * Created on 2003/07/30
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadShiftByOID {

    @Service
    public static InfoShift run(final Integer oid) {
        final Shift shift = RootDomainObject.getInstance().readShiftByOID(oid);
        return shift == null ? null : InfoShift.newInfoFromDomain(shift);
    }

}