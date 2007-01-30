package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer extends the {@link MultiLanguageTextInputRenderer}. The only difference
 * is that a comment is added on top of the first text area. 
 * @author pcma
 *
 */
public class MultiLanguageTextInputRendererWithComment extends MultiLanguageTextInputRenderer {

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

		return new MultiLanguageStringLayoutWithComment();
	}

	protected class MultiLanguageStringLayoutWithComment extends MultiLanguageStringInputLayout {

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			HtmlComponent component = super.createComponent(object, type);
			HtmlBlockContainer block = new HtmlBlockContainer();
			HtmlText text = new HtmlText((getBundle() != null) ? RenderUtils.getResourceString(getBundle(),
					getComment()) : getComment());
			text.setClasses(getCommentClasses());
			block.addChild(component);
			block.addChild(text);
			return block;
		}
	}
}
