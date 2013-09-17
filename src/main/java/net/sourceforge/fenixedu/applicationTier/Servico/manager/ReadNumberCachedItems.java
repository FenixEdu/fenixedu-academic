/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ReadNumberCachedItems {

    @Atomic
    public static Integer run() throws FenixServiceException {
        Integer numberCachedObjects = null;

        // this is no longer available
        // numberCachedObjects =
        // Transaction.getCache().getNumberOfCachedItems();
        numberCachedObjects = 0;

        return numberCachedObjects;
    }

}
