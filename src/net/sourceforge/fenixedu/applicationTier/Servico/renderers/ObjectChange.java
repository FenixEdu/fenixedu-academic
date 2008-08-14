package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ObjectChange {

    public final ObjectKey key;

    public final String slot;

    public final Object value;

    public final Method setter;

    public final Constructor constructor;

    public final Object[] values;

    public ObjectChange(ObjectKey key, String slot, Object value) {
	this.key = key;
	this.slot = slot;
	this.value = value;

	this.setter = null;
	this.constructor = null;
	this.values = null;
    }

    public ObjectChange(ObjectKey key, Method setter, Object[] values) {
	this.key = key;
	this.setter = setter;
	this.values = values;

	this.slot = null;
	this.constructor = null;
	this.value = null;
    }

    public ObjectChange(ObjectKey key, Constructor constructor, Object[] values) {
	this.key = key;
	this.constructor = constructor;
	this.values = values;

	this.slot = null;
	this.setter = null;
	this.value = null;
    }
}