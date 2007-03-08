package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class StringInputRendererWithComment extends StringInputRenderer {

    private String bundle;

    private String comment;

    private String commentClasses;

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getCommentClasses() {
	return commentClasses;
    }

    public void setCommentClasses(String commentClasses) {
	this.commentClasses = commentClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new CommentLayout();
    }
    
    class CommentLayout extends TextFieldLayout {

	@Override
	public HtmlComponent createLayout(Object object, Class type) {
	    	HtmlComponent component = super.createLayout(object, type);
	    
		HtmlText text = new HtmlText((getBundle() != null) ? RenderUtils.getResourceString(
			getBundle(), getComment()) : getComment());

		text.setClasses(getCommentClasses());
		HtmlContainer container = new HtmlBlockContainer();
		container.addChild(component);
		container.addChild(new HtmlText("<br/>", false));
		container.addChild(text);

		return container;

	}
	
    }

}
