package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import net.sourceforge.fenixedu.renderers.taglib.CreateObjectTag;
import net.sourceforge.fenixedu.renderers.taglib.DefaultValueTag;

public class FenixDefaultValueTag extends DefaultValueTag {

    @Override
    protected CreateObjectTag getParentCreateTag() {
        return (CreateObjectTag) findAncestorWithClass(this, FenixCreateObjectTag.class);
    }

}
