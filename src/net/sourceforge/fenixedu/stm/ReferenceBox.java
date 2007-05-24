package net.sourceforge.fenixedu.stm;

import jvstm.VBoxBody;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.metadata.ClassDescriptor;

class ReferenceBox<E> extends VBox<E> {

    ReferenceBox() {
        super();
    }

    ReferenceBox(VBoxBody<E> body) {
	super(body);
    }

    public ReferenceBox(E value) {
        super(value);
    }

    protected void doReload(Object obj, String attr) {
	PersistenceBroker pb = Transaction.getOJBBroker();
	try {
	    pb.retrieveReference(obj, attr);
	} catch (PersistenceBrokerException pbe) {
	    // the attr may be missing from the OJB mapping...
	    ClassDescriptor cld = pb.getClassDescriptor(obj.getClass());
	    if ((cld.getObjectReferenceDescriptorByName(attr) == null)
		&& (cld.getCollectionDescriptorByName(attr) == null)) {
		// assume a null value...
		setFromOJB(obj, attr, null);
	    } else {
		pbe.printStackTrace();
		throw new Error("Couldn't retrieve " + attr + " for class " + obj.getClass() + " which is mapped in OJB: " + pbe);
	    }
	}
    }
}
