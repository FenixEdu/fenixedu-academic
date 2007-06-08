package net.sourceforge.fenixedu.stm;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;


import jvstm.Transaction;
import jvstm.ReadTransaction;

import jvstm.cps.ConsistencyCheckTransaction;
import jvstm.cps.Depended;

import org.apache.ojb.broker.PersistenceBroker;

import net.sourceforge.fenixedu.domain.DomainObject;

public class FenixConsistencyCheckTransaction extends ReadTransaction 
    implements ConsistencyCheckTransaction,FenixTransaction {

    private FenixTransaction parent;
    private Object checkedObj;

    public FenixConsistencyCheckTransaction(FenixTransaction parent, Object checkedObj) {
        super((Transaction)parent);
        this.parent = parent;
        this.checkedObj = checkedObj;
    }

    @Override
    public Transaction makeNestedTransaction() {
        throw new Error("Nested transactions not supported yet...");
    }

    @Override
    public <T> T getBoxValue(jvstm.VBox<T> vbox) {
        throw new Error("In a FenixConsistencyCheckTransaction we must call the three-arg getBoxValue method");
    }

    private static final Set<Depended> EMPTY_SET = Collections.unmodifiableSet(new HashSet<Depended>());

    public Set<Depended> getDepended() {
        return EMPTY_SET;
    }

    public void setReadOnly() {
        throw new Error("It doesn't make sense to call setReadOnly for a FenixConsistencyCheckTransaction");
    }

    public DBChanges getDBChanges() {
        throw new Error("A FenixConsistencyCheckTransaction is read-only");
    }

    public PersistenceBroker getOJBBroker() {
        return parent.getOJBBroker();
    }

    public DomainObject getDomainObject(String classname, int oid) {
        return parent.getDomainObject(classname, oid);
    }

    public <T> T getBoxValue(VBox<T> vbox, Object obj, String attr) {
        if (obj != checkedObj) {
            throw new Error("We currently do not support consistency-predicates that access other objects");
        }

        // ask the parent transaction (a RW tx) for the value of the box
        return parent.getBoxValue(vbox, obj, attr);
    }

    public boolean isBoxValueLoaded(VBox vbox) {
        throw new Error("It doesn't make sense to call isBoxValueLoaded for a FenixConsistencyCheckTransaction");
    }
}
