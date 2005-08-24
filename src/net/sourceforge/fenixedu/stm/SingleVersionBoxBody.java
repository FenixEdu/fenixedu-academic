package net.sourceforge.fenixedu.stm;

class SingleVersionBoxBody<E> extends jvstm.VBoxBody<E> {

    SingleVersionBoxBody() {
	super();
    }

    // used when loading a value from the DB
    SingleVersionBoxBody(E value, int version) {
	this.value = value;
	this.version = version;
    }

    public jvstm.VBoxBody<E> getBody(int maxVersion) {
	if (version > maxVersion) {
	    throw new VersionNotAvailableException();
	} else {
	    return this;
	}
    }

    public void commit(jvstm.VBoxBody<E> previous) {
	// do nothing
    }
}
