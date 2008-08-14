package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.ist.fenixWebFramework.renderers.ValuesRenderer;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;

public class NonNullValuesRenderer extends ValuesRenderer {

    @Override
    protected Layout getLayout(final Object object, Class type) {
	return new NonNullValuesLayout(getContext().getMetaObject());
    }

    class NonNullValuesLayout extends ValuesLayout {

	public NonNullValuesLayout(MetaObject metaObject) {
	    super(metaObject);
	}

	@Override
	protected boolean hasMoreComponents() {
	    for (int pos = this.index; pos < this.slots.size(); pos++) {
		MetaSlot slot = slots.get(pos);
		Object object = slot.getObject();

		if (object instanceof String) {
		    String string = (String) object;
		    object = (string.length() == 0) ? null : object;
		}

		if (object != null) {
		    return true;
		}
	    }

	    return false;
	}

	@Override
	protected MetaSlot getNextSlot() {
	    for (int pos = this.index; pos < this.slots.size(); pos++) {
		MetaSlot slot = slots.get(pos);
		Object object = slot.getObject();

		if (object instanceof String) {
		    String string = (String) object;
		    object = (string.length() == 0) ? null : object;
		}

		if (object != null) {
		    this.index = pos + 1;
		    return slot;
		}
	    }

	    return null;
	}

    }
}
