package net.sourceforge.fenixedu.applicationTier;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * The interface of the service classes.The services classes must also define a
 * static public method called getService that returns an instance of the
 * service.
 */

public interface IServico extends IService {

    /**
     * @return
     */
    String getNome();
}