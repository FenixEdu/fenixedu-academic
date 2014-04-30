package net.sourceforge.fenixedu.presentationTier.Action.person;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.cms.CmsContent;

public class ModifiedContentBean {

    private final Section newParent;
    private final CmsContent content;
    private final int order;

    public ModifiedContentBean(Section newParent, CmsContent content, int order) {
        super();
        this.newParent = newParent;
        this.content = content;
        this.order = order;
    }

    public Section getNewParent() {
        return newParent;
    }

    public CmsContent getContent() {
        return content;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return String.valueOf(newParent) + ":" + content.toString() + ":" + order;
    }

}