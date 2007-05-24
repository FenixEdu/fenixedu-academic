package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;

public interface FenixTransaction {
    public void setReadOnly();
    public DBChanges getDBChanges();
    public PersistenceBroker getOJBBroker();
    public <T> T getBoxValue(VBox<T> vbox, Object obj, String attr);
    public boolean isBoxValueLoaded(VBox vbox);
}
