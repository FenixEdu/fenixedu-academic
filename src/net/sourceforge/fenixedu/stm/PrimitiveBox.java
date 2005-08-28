package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.fieldaccess.PersistentField;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;

import jvstm.VBoxBody;

class PrimitiveBox<E> extends VBox<E> {

    PrimitiveBox() {
        super();
    }

    PrimitiveBox(VBoxBody<E> body) {
	super(body);
    }

    protected void doReload(Object obj, String attr) {
	PersistenceBroker pb = Transaction.getOJBBroker();
	Identity oid = new Identity(obj, pb);
	ClassDescriptor cld = pb.getClassDescriptor(obj.getClass());
	FieldDescriptor fldDesc = cld.getFieldDescriptorByName(attr);

	if (fldDesc != null) {
	    Object freshInstance = pb.serviceJdbcAccess().materializeObject(cld, oid);
	    
	    PersistentField fld = fldDesc.getPersistentField();
	    fld.set(obj, fld.get(freshInstance));
	} else {
	    setFromOJB(obj, attr, null);
	    System.err.println("WARNING: Couldn't load attribute " 
			       + attr 
			       + " for class " 
			       + obj.getClass().getName() 
				   + " because it is not mapped in OJB");
	}
    }
}
