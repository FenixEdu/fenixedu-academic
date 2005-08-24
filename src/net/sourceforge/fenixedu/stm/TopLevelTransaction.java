package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;

import jvstm.VBoxBody;

public class TopLevelTransaction extends jvstm.TopLevelTransaction implements FenixTransaction {

    private DBChanges dbChanges = null;

    TopLevelTransaction(int number) {
        super(number);
    }

    public <T> VBoxBody<T> getBodyForRead(VBox<T> vbox, Object obj, String attr) {
        VBoxBody<T> body = getBodyWritten(vbox);
        if (body == null) {
            body = getBodyRead(vbox);
        }
        if (body == null) {
	    if (vbox.body == VBox.NOT_LOADED_YET) {
		//System.out.println("Retrieving attr " + attr + " of class " + obj.getClass());
		boolean loading = VBox.setLoading(true);
		try {
		    getOJBBroker().retrieveReference(obj, attr);
		} catch (Exception e) {
		    //System.out.println("Couldn't retrieve it!!!!");
		    //e.printStackTrace();
		    // what to do?
		} finally {
		    VBox.setLoading(loading);
		}
	    }
            body = vbox.body.getBody(number);
            bodiesRead.put(vbox, body);
        }
        return body;	
    }

    VBoxBody getBodyWrittenForBox(VBox box) {
	return getBodyWritten(box);
    }


    VBoxBody getBodyReadForBox(VBox box) {
	return getBodyRead(box);
    }
    

    public DBChanges getDBChanges() {
	if (dbChanges == null) {
	    dbChanges = new DBChanges();
	}
	return dbChanges;
    }

    public PersistenceBroker getOJBBroker() {
	return getDBChanges().getOJBBroker();
    }

    protected boolean isWriteTransaction() {
	return ((dbChanges != null) && (dbChanges.needsWrite()))
	    || super.isWriteTransaction();
    }

    protected void validateAndCommit() {
	// obtain exclusive lock on db
	// read db logs and update last committed number

	super.validateAndCommit();

	// release exclusive lock on db
    }

    protected boolean validateCommit() {
	if (super.validateCommit()) {
	    // in memory everything is ok, but we need to check against the db
	    return true;
	} else {
	    return false;
	}
    }

    protected void doCommit(int newTxNumber) {
	if (dbChanges != null) {
	    dbChanges.makePersistent(newTxNumber);
	}
	super.doCommit(newTxNumber);
    }
}
