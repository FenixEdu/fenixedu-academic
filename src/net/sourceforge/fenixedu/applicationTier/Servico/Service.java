package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

public abstract class Service {
    /**
     * This method converts this service DataBeans input objects to their
     * respective Domain objects. These Domain objects are to be used by the
     * service's logic.
     */
    protected abstract Object convertDataInput(Object object);

    /**
     * This method converts this service output Domain objects to their
     * respective DataBeans. These DataBeans are the result of executing this
     * service logic and are to be passed on to the uper layer of the
     * architecture.
     */
    protected abstract Object convertDataOutput(Object object);

    /**
     * This method implements the buisiness logic of this service.
     */
    protected abstract Object execute(Object object) throws FenixServiceException;
}