package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.lang.reflect.Method;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.core.proxy.IndirectionHandlerDefaultImpl;

public class IndirectionHandlerFenixImpl extends IndirectionHandlerDefaultImpl {

    public IndirectionHandlerFenixImpl(PBKey brokerKey, Identity id) {
        super(brokerKey, id);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            return super.invoke(proxy, method, args);
        } catch (PersistenceBrokerException pbex) {
            final Throwable cause = pbex.getCause();
            if (cause != null) {
                final Throwable initialCause = cause.getCause();
                if (initialCause != null && initialCause instanceof DomainException) {
                    throw (DomainException) initialCause;
                }
            }
            throw pbex;
        }
    }

    

}
