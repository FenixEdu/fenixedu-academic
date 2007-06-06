package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ExportThesesDA extends LibraryThesisDA {

	private Object setupContext(HttpServletRequest request) {
		List<Integer> ids = getIdInternals(request, "thesesIDs");
		
		if (ids == null) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		for (Integer id : ids) {
			if (builder.length() > 0) {
				builder.append("&amp;");
			}

			builder.append("thesesIDs=" + id);
		}
		
		return builder.toString();
	}
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("theses", getTheses(request));
		request.setAttribute("context", setupContext(request));
		
		return super.execute(mapping, actionForm, request, response);
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("confirmation");
	}

	public ActionForward generate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Thesis> theses = getTheses(request);
		
		Element root = new Element("publications").setAttribute("version", "1.0");
		for (Thesis thesis : theses) {
			root.addContent(generateThesisElement(thesis));
		}
		
		Document document = new Document();
		document.setRootElement(root);
		
        response.setContentType("text/xml");
        response.setHeader("Content-Disposition", "attachment; filename=\"thesis-details.xml\"");
		
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("ISO-8859-1"));
        outputter.output(document, response.getWriter());

		return null;
	}
	
	private Element generateThesisElement(Thesis thesis) {
		Element element = new Element("thesis");

		element
			.addContent(new Element("date").setText(thesis.getDiscussed().toString()))
			.addContent(new Element("author").setText(thesis.getStudent().getPerson().getName()))
			.addContent(new Element("language").setText(thesis.getLanguage().toString()))
			.addContent(new Element("scientific-area").setText(thesis.getScientificArea()));
			
		Element title = new Element("title");
		MultiLanguageString finalTitle = thesis.getFinalTitle();
		for (Language language : finalTitle.getAllLanguages()) {
			Element innerValue = new Element("value").setAttribute("lang", language.toString());
			innerValue.setText(finalTitle.getContent(language));
			
			title.addContent(innerValue);
		}
		
		Element subtitle = new Element("subtitle");
		MultiLanguageString finalSubtitle = thesis.getFinalTitle();
		for (Language language : finalSubtitle.getAllLanguages()) {
			Element innerValue = new Element("value").setAttribute("lang", language.toString());
			innerValue.setText(finalSubtitle.getContent(language));
			
			subtitle.addContent(innerValue);
		}
		
		element.addContent(title).addContent(subtitle);
		
		return element;
	}

	public ActionForward confirm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Thesis> theses = getTheses(request);
		executeService("MarkExportedTheses", theses, true);
		
		request.setAttribute("confirmExported", "true");
		
		return prepare(mapping, actionForm, request, response);
	}
	
}
