package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;

import jvstm.VBoxBody;

public interface FenixTransaction {
    public void setReadOnly();
    public DBChanges getDBChanges();
    public PersistenceBroker getOJBBroker();
    <T> VBoxBody<T> getBodyForRead(VBox<T> vbox, Object obj, String attr);
    <T> VBoxBody<T> getBodyInTx(VBox<T> vbox);
}
