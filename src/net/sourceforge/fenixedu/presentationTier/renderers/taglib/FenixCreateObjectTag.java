package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.taglib.CreateObjectTag;

public class FenixCreateObjectTag extends CreateObjectTag {

	@Override
	protected MetaObject createMetaObject(Object targetObject, Schema schema) {
		MetaObject metaObject = super.createMetaObject(targetObject, schema);

		return metaObject;
	}

	@Override
	public void release() {
		super.release();
	}

}
