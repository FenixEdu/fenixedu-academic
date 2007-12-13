package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlLinkWithPreprendedComment extends HtmlLink {
    
    private String preprendedComment;
    
    public HtmlLinkWithPreprendedComment(String preprendedComment) {
	super();
	setPreprendedComment(preprendedComment);
	setIndented(false);
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
	HtmlTag ownTag = super.getOwnTag(context);
	if(getPreprendedComment() != null) {
	    ownTag.setPreprendedComment(getPreprendedComment());
	}
	return ownTag;
    }


    public String getPreprendedComment() {
        return preprendedComment;
    }


    public void setPreprendedComment(String preprendedComment) {
        this.preprendedComment = preprendedComment;
    }
}
