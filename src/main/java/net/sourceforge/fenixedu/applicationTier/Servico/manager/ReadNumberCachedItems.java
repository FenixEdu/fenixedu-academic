/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ReadNumberCachedItems {

    @Service
    public static Integer run() throws FenixServiceException {
        Integer numberCachedObjects = null;

        // this is no longer available
        // numberCachedObjects =
        // Transaction.getCache().getNumberOfCachedItems();
        numberCachedObjects = 0;

        return numberCachedObjects;
    }

}
