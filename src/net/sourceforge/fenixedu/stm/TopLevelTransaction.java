package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.LookupException;

import jvstm.VBoxBody;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TopLevelTransaction extends jvstm.TopLevelTransaction implements FenixTransaction {

    private DBChanges dbChanges = null;
    private ServiceInfo serviceInfo = null;

    TopLevelTransaction(int number) {
        super(number);
	this.serviceInfo = ServiceInfo.getCurrentServiceInfo();
	this.dbChanges = new DBChanges();

	// open a connection to the database and set this tx number to the number that
	// corresponds to that connection number.  The connection number should always be 
	// greater than the current number, because the current number is obtained from
	// Transaction.getCommitted, which is set only after the commit to the database
	setNumber(updateFromTxLogsOnDatabase(number));
    }


    Transaction makeNestedTransaction() {
	throw new Error("Nested transactions not supported yet...");
    }


    private int updateFromTxLogsOnDatabase(int currentNumber) {
	try {
	    return TransactionChangeLogs.updateFromTxLogsOnDatabase(getOJBBroker(), currentNumber);
	} catch (Exception sqle) {
	    sqle.printStackTrace();
	    throw new Error("Error while updating from TX_CHANGE_LOGS: Cannot proceed.");
	}
    }

    public void logServiceInfo() {
	System.out.println("Transaction " + this 
			   + " created for service: " + serviceInfo.serviceName 
			   + ", username = " + serviceInfo.username 
			   + ", args = " + serviceInfo.getArgumentsAsString());
    }

    protected void finish() {
	super.finish();
	getDBChanges().finish();
	dbChanges = null;
    }


    public <T> VBoxBody<T> getBodyForRead(VBox<T> vbox, Object obj, String attr) {
        VBoxBody<T> body = getBodyInTx(vbox);

        if (body == null) {
            body = vbox.body.getBody(number);
	    if (body.value == VBox.NOT_LOADED_VALUE) {
		if (vbox.reload(obj, attr)) {
		    return getBodyForRead(vbox, obj, attr);
		} else {
		    throw new Error("Couldn't load the attribute " + attr + " for class " + obj.getClass());
		}
	    } else {
		bodiesRead.put(vbox, body);
	    }
        }
        return body;
    }

    public <T> VBoxBody<T> getBodyInTx(VBox<T> vbox) {
        VBoxBody<T> body = getBodyWritten(vbox);
        if (body == null) {
            body = getBodyRead(vbox);
        }
	return body;
    }

    public DBChanges getDBChanges() {
	return dbChanges;
    }

    public PersistenceBroker getOJBBroker() {
	return getDBChanges().getOJBBroker();
    }

    protected boolean isWriteTransaction() {
	return dbChanges.needsWrite() || super.isWriteTransaction();
    }

    protected int performValidCommit() {
	// in memory everything is ok, but we need to check against the db
	PersistenceBroker pb = getOJBBroker();

	try {
	    if (! pb.isInTransaction()) {
		pb.beginTransaction();
	    }
	    Connection conn = pb.serviceConnectionManager().getConnection();
	    Statement stmt = conn.createStatement();

	    int txNumber = getNumber();

	    try {
		// obtain exclusive lock on db
		ResultSet rs = stmt.executeQuery("SELECT GET_LOCK('ciapl.commitlock',100)");
		
		if (rs.next() && (rs.getInt(1) == 1)) {
		    // ensure that we will get the last data in the database
		    conn.commit();
		    
		    if (TransactionChangeLogs.updateFromTxLogsOnDatabase(pb, txNumber) != txNumber) {
			// the cache may have been updated, so perform the tx-validation again
			if (! validateCommit()) {
			    throw new jvstm.CommitException();
			}
		    }
		    
		    txNumber = super.performValidCommit();
		    
		    // ensure that changes are visible to other TXs before releasing lock
		    conn.commit();
		} else {
		    throw new Error("Couldn't get exclusive commit lock on the database");
		}
	    } finally {
		// release exclusive lock on db
		// if the lock was not gained, calling RELEASE_LOCK has no effect
		stmt.executeUpdate("DO RELEASE_LOCK('ciapl.commitlock')");
	    }

	    pb.commitTransaction();
	    pb = null;

	    return txNumber;
	} catch (SQLException sqle) {
	    throw new Error("Error while accessing database");
	} catch (LookupException le) {
	    throw new Error("Error while obtaining database connection");
	} finally {
	    if (pb != null) {
		pb.abortTransaction();
	    }
	}
    }


    protected void doCommit(int newTxNumber) {
	try {
	    dbChanges.makePersistent(newTxNumber);
	    dbChanges.cache();
	    super.doCommit(newTxNumber);
	} catch (SQLException sqle) {
	    throw new Error("Error while accessing database");
	} catch (LookupException le) {
	    throw new Error("Error while obtaining database connection");
	}
    }
}
