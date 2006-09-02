package net.sourceforge.fenixedu.stm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jvstm.VBoxBody;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;

import pt.utl.ist.fenix.tools.util.StringAppender;


public class TopLevelTransaction extends jvstm.TopLevelTransaction implements FenixTransaction {

    private static final ArrayList<CommitListener> COMMIT_LISTENERS = new ArrayList<CommitListener>();

    public static void addCommitListener(CommitListener listener) {
	synchronized (COMMIT_LISTENERS) {
	    COMMIT_LISTENERS.add(listener);
	}
    }

    public static void removeCommitListener(CommitListener listener) {
	synchronized (COMMIT_LISTENERS) {
	    COMMIT_LISTENERS.remove(listener);
	}
    }

    private static void notifyBeforeCommit(TopLevelTransaction tx) {
	for (CommitListener cl : COMMIT_LISTENERS) {
	    cl.beforeCommit(tx);
	}
    }

    private static void notifyAfterCommit(TopLevelTransaction tx) {
	for (CommitListener cl : COMMIT_LISTENERS) {
	    cl.afterCommit(tx);
	}
    }


    // Each TopLevelTx has its DBChanges 
    // If this slot is changed to null, it is an indication that the
    // transaction does not allow more changes
    private DBChanges dbChanges = new DBChanges();
    private ServiceInfo serviceInfo = ServiceInfo.getCurrentServiceInfo();
    private PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

    private Thread executingThread = Thread.currentThread();

    TopLevelTransaction(int number) {
        super(number);

	// open a connection to the database and set this tx number to the number that
	// corresponds to that connection number.  The connection number should always be 
	// greater than the current number, because the current number is obtained from
	// Transaction.getCommitted, which is set only after the commit to the database
	setNumber(updateFromTxLogsOnDatabase(number));
    }

    public PersistenceBroker getOJBBroker() {
	return broker;
    }

    public void setReadOnly() {
	// a null dbChanges indicates a read-only tx
	this.dbChanges = null;
    }

    protected Transaction makeNestedTransaction() {
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
			   //+ ", args = " + serviceInfo.getArgumentsAsString()
			   );
	System.out.println("Currently executing:");
	StackTraceElement[] txStack = executingThread.getStackTrace();
	for (int i = 0; (i < txStack.length) && (i < 5); i++) {
	    System.out.println("-----> " + txStack[i]);
	}
    }

    protected void finish() {
	super.finish();
	if (broker != null) {
	    broker.close();
	    broker = null;
	}
	dbChanges = null;
    }

    protected void doCommit() {
	notifyBeforeCommit(this);
	super.doCommit();
	notifyAfterCommit(this);
    }

    public ReadSet getReadSet() {
	return new ReadSet(bodiesRead);
    }

    protected <T> jvstm.VBoxBody<T> getBodyForWrite(jvstm.VBox<T> vbox) {
	if (dbChanges == null) {
	    throw new IllegalWriteException();
	} else {
	    return super.getBodyForWrite(vbox);
	}
    }

    protected <T> void setPerTxValue(jvstm.PerTxBox<T> box, T value) {
	if (dbChanges == null) {
	    throw new IllegalWriteException();
	} else {
	    super.setPerTxValue(box, value);
	}
    }

    public <T> VBoxBody<T> getBodyForRead(VBox<T> vbox, Object obj, String attr) {
       VBoxBody<T> body = getBodyInTx(vbox);

        if (body == null) {
            body = vbox.body.getBody(number);
	    if (body.value == VBox.NOT_LOADED_VALUE) {
		vbox.reload(obj, attr);
		// after the reload, the same body should have a new value
		// if not, then something gone wrong and its better to abort
		if (body.value == VBox.NOT_LOADED_VALUE) {
		    System.out.println("Couldn't load the attribute " + attr + " for class " + obj.getClass());
		    throw new VersionNotAvailableException();
		}
	    }
	    
	    bodiesRead.put(vbox, body);
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
	if (dbChanges == null) {
	    // if it is null, it means that the transaction is a read-only transaction
	    throw new IllegalWriteException();
	} else {
	    return dbChanges;
	}
    }

    protected boolean isWriteTransaction() {
	return ((dbChanges != null) && dbChanges.needsWrite())
	    || super.isWriteTransaction();
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
		//ResultSet rs = stmt.executeQuery("SELECT GET_LOCK('ciapl.commitlock',10)");
		//if (rs.next() && (rs.getInt(1) == 1)) {
		    // ensure that we will get the last data in the database
		if(true)
		{		   
		    //conn.commit();
		    /*if (TransactionChangeLogs.updateFromTxLogsOnDatabase(pb, txNumber) != txNumber) {
			// the cache may have been updated, so perform the tx-validation again
			if (! validateCommit()) {
			    throw new jvstm.CommitException();
			}
		    }*/
		    txNumber = super.performValidCommit();
		    // ensure that changes are visible to other TXs before releasing lock
		    conn.commit();
		} else {
		    throw new Error("Couldn't get exclusive commit lock on the database");
		}
	    } finally {
		// release exclusive lock on db
		// if the lock was not gained, calling RELEASE_LOCK has no effect
		//stmt.executeUpdate("DO RELEASE_LOCK('ciapl.commitlock')");
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
	    dbChanges.makePersistent(getOJBBroker(), newTxNumber);
	    dbChanges.cache();
	    super.doCommit(newTxNumber);
	} catch (SQLException sqle) {
	    throw new Error("Error while accessing database");
	} catch (LookupException le) {
	    throw new Error("Error while obtaining database connection");
	}
    }
}
