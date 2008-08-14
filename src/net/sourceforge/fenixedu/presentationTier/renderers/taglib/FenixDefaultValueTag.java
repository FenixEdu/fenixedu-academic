package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import pt.ist.fenixWebFramework.renderers.taglib.CreateObjectTag;
import pt.ist.fenixWebFramework.renderers.taglib.DefaultValueTag;

public class FenixDefaultValueTag extends DefaultValueTag {

    @Override
    protected CreateObjectTag getParentCreateTag() {
	return (CreateObjectTag) findAncestorWithClass(this, FenixCreateObjectTag.class);
    }

}
