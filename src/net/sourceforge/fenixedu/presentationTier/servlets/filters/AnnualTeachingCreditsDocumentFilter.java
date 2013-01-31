package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.StringUtils;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.ResponseWrapper;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

import com.lowagie.text.DocumentException;

public class AnnualTeachingCreditsDocumentFilter implements Filter {
	private ServletContext servletContext;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		servletContext = arg0.getServletContext();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		ResponseWrapper response = (ResponseWrapper) arg1;

		Teacher teacher = AbstractDomainObject.fromExternalId(request.getParameter("teacherOid"));
		ExecutionYear executionYear = AbstractDomainObject.fromExternalId(request.getParameter("executionYearOid"));
		ByteArrayOutputStream pdfStreamToReturn = null;

		try {
			request.setAttribute("teacher", teacher);
			request.setAttribute("executionYear", executionYear);
			arg2.doFilter(arg0, arg1);
			String responseHtml = clean(response.getContent());
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(responseHtml.getBytes()));
			patchLinks(doc, request);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(doc, "");
			renderer.layout();
			ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
			renderer.createPDF(pdfStream);
			pdfStreamToReturn = pdfStream;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				response.reset();
				response.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		response.getOutputStream().write(pdfStreamToReturn.toByteArray());
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition",
				"attachment; filename=" + teacher.getTeacherId() + "_" + executionYear.getName() + ".pdf");
		response.getOutputStream().close();
	}

	private String clean(String dirtyHtml) {
		try {
			HtmlCleaner cleaner = new HtmlCleaner();

			TagNode root = cleaner.clean(dirtyHtml);

			return new SimpleHtmlSerializer(cleaner.getProperties()).getAsString(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	private void patchLinks(Document doc, HttpServletRequest request) {
		// build basePath
		String appContext = FenixWebFramework.getConfig().getAppContext();

		// patch css link nodes
		NodeList linkNodes = doc.getElementsByTagName("link");
		for (int i = 0; i < linkNodes.getLength(); i++) {
			Element link = (Element) linkNodes.item(i);
			String href = link.getAttribute("href");

			if (appContext != null && appContext.length() > 0 && href.contains(appContext)) {
				href = href.substring(appContext.length() + 1);
			}

			try {
				String realPath = servletContext.getResource(href).toString();
				link.setAttribute("href", realPath);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}

		// patch image nodes
		NodeList imageNodes = doc.getElementsByTagName("img");
		for (int i = 0; i < imageNodes.getLength(); i++) {
			Element img = (Element) imageNodes.item(i);
			String src = img.getAttribute("src");

			if (appContext != null && appContext.length() > 0 && src.contains(appContext)) {
				src = src.substring(appContext.length() + 1);
			}

			try {
				String realPath = servletContext.getResource(src).toString();
				img.setAttribute("src", realPath);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
}
