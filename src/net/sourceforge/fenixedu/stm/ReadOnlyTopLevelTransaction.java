package net.sourceforge.fenixedu.stm;

import jvstm.VBoxBody;


class ReadOnlyTopLevelTransaction extends TopLevelTransaction {

    ReadOnlyTopLevelTransaction(int number) {
        super(number);
    }

    protected void initDbChanges() {
        // do nothing
    }

    public ReadSet getReadSet() {
        throw new Error("ReadOnly txs don't record their read sets...");
    }

    public <T> VBoxBody<T> getBodyForRead(VBox<T> vbox, Object obj, String attr) {
        VBoxBody<T> body = vbox.body.getBody(number);
        if (body.value == VBox.NOT_LOADED_VALUE) {
            vbox.reload(obj, attr);
            // after the reload, the same body should have a new value
            // if not, then something gone wrong and its better to abort
            if (body.value == VBox.NOT_LOADED_VALUE) {
                System.out.println("Couldn't load the attribute " + attr + " for class " + obj.getClass());
                throw new VersionNotAvailableException();
            }
        }

        return body;
    }
    
    protected boolean isWriteTransaction() {
        return false;
    }
}
