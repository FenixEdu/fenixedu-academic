package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.tagext.Tag;

import net.sourceforge.fenixedu.renderers.taglib.DefaultValueTag;

public class FenixDefaultValueTag extends DefaultValueTag {

    @Override
    protected Tag getParentCreateTag() {
        return findAncestorWithClass(this, FenixCreateObjectTag.class);
    }

}
