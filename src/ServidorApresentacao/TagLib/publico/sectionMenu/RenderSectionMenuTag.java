/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.TagLib.publico.sectionMenu;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import ServidorApresentacao
	.TagLib
	.publico
	.sectionMenu
	.renderers
	.SectionMenuContentRenderer;
import ServidorApresentacao
	.TagLib
	.publico
	.sectionMenu
	.renderers
	.SectionMenuSlotContentRenderer;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RenderSectionMenuTag extends TagSupport {

	private String name;
	private SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer =
		new SectionMenuContentRenderer();

	public int doStartTag() throws JspException {
		//		Obtain InfoSection
		List sections = null;
		try {
			sections = (List) pageContext.findAttribute(name);
		} catch (ClassCastException e) {
			sections = null;
		}
		//TODO: change the message
		if (sections == null) {
			throw new JspException(
				messages.getMessage(
					"generateExamsMap.infoExamsMap.notFound",
					name));
		}

		//		Generate Map from sections
		JspWriter writer = pageContext.getOut();
		SectionMenuMap sectionMenuMap = new SectionMenuMap(sections);

		SectionMenuMapRenderer renderer =
			new SectionMenuMapRenderer(
				sectionMenuMap,
				this.sectionMenuSlotContentRenderer);

		try {
			writer.print(renderer.render());
		} catch (IOException e) {
			throw new JspException(
				messages.getMessage("generateExamsMap.io", e.toString()));
		}

		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
	}

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}
	//	Error Messages
	protected static MessageResources messages =
		MessageResources.getMessageResources("ApplicationResources");
}
