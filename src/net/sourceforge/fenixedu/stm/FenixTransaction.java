package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;

import net.sourceforge.fenixedu.domain.DomainObject;

public interface FenixTransaction {
    public void setReadOnly();
    public DBChanges getDBChanges();
    public PersistenceBroker getOJBBroker();
    public DomainObject getDomainObject(String classname, int oid);
    public <T> T getBoxValue(VBox<T> vbox, Object obj, String attr);
    public boolean isBoxValueLoaded(VBox vbox);
}
