package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

public class ObjectKey {
    private final int oid;
    private final Class type;

    public ObjectKey(int oid, Class type) {
	super();

	this.oid = oid;
	this.type = type;
    }

    public int getOid() {
	return oid;
    }

    public Class getType() {
	return type;
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof ObjectKey)) {
	    return false;
	}

	ObjectKey otherKey = (ObjectKey) other;

	if (this.type == null && otherKey.type != null) {
	    return false;
	}

	if (this.type != null && !this.type.equals(otherKey.type)) {
	    return false;
	}

	return this.oid == otherKey.oid;
    }

    @Override
    public int hashCode() {
	return this.oid + (this.type == null ? 0 : this.type.hashCode());
    }
}
