/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.servlet.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.AnnualCreditsState;
import org.fenixedu.academic.domain.credits.AnnualTeachingCredits;
import org.fenixedu.academic.domain.credits.AnnualTeachingCreditsDocument;
import org.fenixedu.academic.domain.person.RoleType;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.ResponseWrapper;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.lowagie.text.DocumentException;

public class AnnualTeachingCreditsDocumentFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AnnualTeachingCreditsDocumentFilter.class);

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
        ResponseWrapper response = (ResponseWrapper) arg1;

        ExecutionYear executionYear = FenixFramework.getDomainObject(arg0.getParameter("executionYearOid"));
        Teacher teacher = FenixFramework.getDomainObject(arg0.getParameter("teacherOid"));
        if (teacher != null) {
            ByteArrayOutputStream pdfStreamToReturn = null;
            try {
                pdfStreamToReturn = getAnnualTeacherCreditsDocument(arg0, arg1, arg2, teacher, executionYear, null);
            } finally {
                if (!response.isCommitted()) {
                    response.reset();
                }
            }
            response.getOutputStream().write(pdfStreamToReturn.toByteArray());
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + teacher.getTeacherId() + "_" + executionYear.getName() + ".pdf");
            response.getOutputStream().close();
        } else {
            AnnualCreditsState annualCreditsState = AnnualCreditsState.getAnnualCreditsState(executionYear);
            if (annualCreditsState != null && annualCreditsState.getIsFinalCreditsCalculated()
                    && !annualCreditsState.getIsCreditsClosed()) {
                Map<AnnualTeachingCredits, ByteArrayOutputStream> documentsWithConfidentionalInformation =
                        new HashMap<AnnualTeachingCredits, ByteArrayOutputStream>();
                Map<AnnualTeachingCredits, ByteArrayOutputStream> documentsWithoutConfidentionalInformation =
                        new HashMap<AnnualTeachingCredits, ByteArrayOutputStream>();
                for (AnnualTeachingCredits annualTeachingCredits : annualCreditsState.getAnnualTeachingCreditsSet()) {
                    try {
                        ByteArrayOutputStream pdfStreamToReturn;
                        pdfStreamToReturn =
                                getAnnualTeacherCreditsDocument(arg0, arg1, arg2, annualTeachingCredits.getTeacher(),
                                        executionYear, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
                        documentsWithoutConfidentionalInformation.put(annualTeachingCredits, pdfStreamToReturn);
                    } finally {
                        if (!response.isCommitted()) {
                            response.resetBuffer();
                        }
                    }
                    try {
                        ByteArrayOutputStream pdfStreamToReturn =
                                getAnnualTeacherCreditsDocument(arg0, arg1, arg2, annualTeachingCredits.getTeacher(),
                                        executionYear, RoleType.SCIENTIFIC_COUNCIL);
                        documentsWithConfidentionalInformation.put(annualTeachingCredits, pdfStreamToReturn);
                    } finally {
                        if (!response.isCommitted()) {
                            response.resetBuffer();
                        }
                    }
                }
                closeAnnualCreditsState(annualCreditsState, documentsWithConfidentionalInformation,
                        documentsWithoutConfidentionalInformation);
            }
            response.sendRedirect(buildRedirectURL((HttpServletRequest) arg0));
            response.flushBuffer();
        }
    }

    private String buildRedirectURL(HttpServletRequest request) {
        String url = "/scientificCouncil/defineCreditsPeriods.do?method=showPeriods";

        String urlWithChecksum = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), url, request.getSession());

        return request.getContextPath() + urlWithChecksum;
    }

    @Atomic
    private void closeAnnualCreditsState(AnnualCreditsState annualCreditsState,
            Map<AnnualTeachingCredits, ByteArrayOutputStream> documentsWithConfidentionalInformation,
            Map<AnnualTeachingCredits, ByteArrayOutputStream> documentsWithoutConfidentionalInformation) {
        annualCreditsState.setIsCreditsClosed(true);
        for (AnnualTeachingCredits annualTeachingCredits : documentsWithConfidentionalInformation.keySet()) {
            new AnnualTeachingCreditsDocument(annualTeachingCredits, documentsWithConfidentionalInformation.get(
                    annualTeachingCredits).toByteArray(), true);
        }
        for (AnnualTeachingCredits annualTeachingCredits : documentsWithoutConfidentionalInformation.keySet()) {
            new AnnualTeachingCreditsDocument(annualTeachingCredits, documentsWithoutConfidentionalInformation.get(
                    annualTeachingCredits).toByteArray(), false);
        }
    }

    protected ByteArrayOutputStream getAnnualTeacherCreditsDocument(ServletRequest arg0, ServletResponse arg1, FilterChain arg2,
            Teacher teacher, ExecutionYear executionYear, RoleType role) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        ResponseWrapper response = (ResponseWrapper) arg1;
        request.setAttribute("teacher", teacher);
        request.setAttribute("executionYear", executionYear);
        arg2.doFilter(arg0, arg1);
        String responseHtml = clean(response.getContent());
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        }
        Document doc;
        try {
            doc = builder.parse(new ByteArrayInputStream(responseHtml.getBytes()));
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
        patchLinks(doc, request);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, "");
        renderer.layout();
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        try {
            renderer.createPDF(pdfStream);
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
        return pdfStream;
    }

    private String clean(String dirtyHtml) {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();

            TagNode root = cleaner.clean(dirtyHtml);

            return new SimpleHtmlSerializer(cleaner.getProperties()).getAsString(root);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    private void patchLinks(Document doc, HttpServletRequest request) {
        // build basePath
        String appContext = request.getContextPath();

        // patch css link nodes
        NodeList linkNodes = doc.getElementsByTagName("link");
        for (int i = 0; i < linkNodes.getLength(); i++) {
            Element link = (Element) linkNodes.item(i);
            String href = link.getAttribute("href");

            if (appContext.length() > 0 && href.contains(appContext)) {
                href = href.substring(appContext.length());
            }

            try {
                String realPath = servletContext.getResource(href).toString();
                link.setAttribute("href", realPath);
            } catch (MalformedURLException e) {
                logger.error(e.getMessage(), e);
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
                logger.error(e.getMessage(), e);
            }
        }
    }
}
