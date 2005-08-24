package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;

public abstract class Transaction extends jvstm.Transaction {

    private Transaction() {
	// this is never to be used!!!
	super(0);
    }

    public static jvstm.Transaction begin() {
        return begin(-1);
    }

    protected static jvstm.Transaction begin(int txNumber) {
        jvstm.Transaction parent = current.get();
        jvstm.Transaction tx = null;
        if (parent == null) {
            int num = ((txNumber == -1) ? getCommitted() : txNumber);
            tx = new TopLevelTransaction(num);
        } else {
            //tx = new NestedTransaction(parent);
	    throw new Error("Nested transactions not supported yet...");
        }        
        current.set(tx);
        return tx;
    }

    public static FenixTransaction currentFenixTransaction() {
	return (FenixTransaction)current();
    }

    protected static DBChanges currentDBChanges() {
	return currentFenixTransaction().getDBChanges();
    }

    public static void storeNewObject(Object obj) {
	currentDBChanges().storeNewObject(obj);
    }

    public static void storeObject(Object obj) {
	currentDBChanges().storeObject(obj);
    }

    public static void deleteObject(Object obj) {
	currentDBChanges().deleteObject(obj);
    }

    public static void addRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	currentDBChanges().addRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2);
    }

    public static void removeRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	currentDBChanges().removeRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2);
    }

    public static PersistenceBroker getOJBBroker() {
	return currentFenixTransaction().getOJBBroker();
    }
}
