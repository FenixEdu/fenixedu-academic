/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import DataBeans.InfoExamsMap;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class RenderExamsMapTag extends TagSupport {

	// Name of atribute containing ExamMap
	private String name;


	public int doStartTag() throws JspException {
		// Obtain InfoExamMap
		InfoExamsMap infoExamsMap = null;
		try {
			infoExamsMap = (InfoExamsMap) pageContext.findAttribute(name);
		} catch (ClassCastException e) {
			infoExamsMap = null;
		}		
		if (infoExamsMap == null) {
			throw new JspException(
				messages.getMessage("generateExamsMap.infoExamsMap.notFound", name));
		}
		
		// Generate Map from infoExamsMap
		JspWriter writer = pageContext.getOut();
		ExamsMap examsMap = new ExamsMap(infoExamsMap);

		// TODO : ExamsMapRenderer ...

		// TODO : write everything ...

		return (SKIP_BODY);
	}


	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
	}

	// Error Messages
	protected static MessageResources messages =
		MessageResources.getMessageResources("ApplicationResources");

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

}
