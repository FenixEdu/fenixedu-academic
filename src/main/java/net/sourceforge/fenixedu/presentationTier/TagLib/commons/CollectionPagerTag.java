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
package net.sourceforge.fenixedu.presentationTier.TagLib.commons;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.fenixedu.commons.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionPagerTag extends TagSupport {

    private static final Logger logger = LoggerFactory.getLogger(CollectionPagerTag.class);

    private String url;

    private String bundle;

    private String numberOfPagesAttributeName;

    private String pageNumberAttributeName;

    private String numberOfVisualizedPages;

    private String module;

    @Override
    public int doStartTag() throws JspException {
        String pages = createCollectionPages();
        try {
            pageContext.getOut().print(pages);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return SKIP_BODY;
    }

    private String createCollectionPages() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuilder stringBuilder = new StringBuilder();

        final int lastPage = ((Integer) pageContext.findAttribute(getNumberOfPagesAttributeName())).intValue();
        final int pageNumber = ((Integer) pageContext.findAttribute(getPageNumberAttributeName())).intValue();

        addFirstSymbols(stringBuilder, request, pageNumber);

        generatePageNumbers(stringBuilder, request, lastPage, pageNumber);

        addLastSymbols(stringBuilder, request, lastPage, pageNumber);

        return stringBuilder.toString();
    }

    private void generatePageNumbers(StringBuilder stringBuilder, HttpServletRequest request, final int lastPage,
            final int pageNumber) {

        int firstVisualizedPage = 1;
        int lastVisualizedPage = lastPage;
        Integer numberOfVisualizedPages =
                (getNumberOfVisualizedPages() != null) ? Integer.valueOf(getNumberOfVisualizedPages()) : null;

        if (lastPage > 0 && numberOfVisualizedPages != null && numberOfVisualizedPages.intValue() > 0
                && lastPage > numberOfVisualizedPages.intValue()) {

            if (pageNumber == 1) {
                lastVisualizedPage = numberOfVisualizedPages;

            } else if (pageNumber == lastPage) {
                firstVisualizedPage = lastPage - (numberOfVisualizedPages - 1);

            } else {
                int numberOfLeftAndRightPages = (int) Math.floor((numberOfVisualizedPages - 1) / 2);
                int leftPage = (pageNumber - numberOfLeftAndRightPages) <= 0 ? 1 : pageNumber - numberOfLeftAndRightPages;
                int rightPage = numberOfLeftAndRightPages + pageNumber;

                firstVisualizedPage = (pageNumber != 1) ? (leftPage >= 1 ? leftPage : 1) : 1;
                lastVisualizedPage = (pageNumber != lastPage) ? (rightPage <= lastPage ? rightPage : lastPage) : lastPage;

                int numberOfResultPages = Math.abs(firstVisualizedPage - lastVisualizedPage) + 1;
                if (firstVisualizedPage == 1) {
                    lastVisualizedPage = lastVisualizedPage + (numberOfVisualizedPages - numberOfResultPages);
                } else if (lastVisualizedPage == lastPage) {
                    firstVisualizedPage = firstVisualizedPage - (numberOfVisualizedPages - numberOfResultPages);
                }
            }
        }

        for (int i = firstVisualizedPage; i <= lastVisualizedPage; i++) {
            if (i != pageNumber) {
                stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&amp;")
                        .append(getPageNumberAttributeName()).append("=").append(Integer.toString(i)).append("\">");
                stringBuilder.append(Integer.toString(i)).append("</a>&nbsp;");
            } else {
                stringBuilder.append("&nbsp;<b>").append(Integer.toString(i)).append("</b>&nbsp;");
            }
        }
    }

    private void addLastSymbols(StringBuilder stringBuilder, HttpServletRequest request, final int lastPage, final int pageNumber)
            throws JspException {
        if (lastPage > 1 && pageNumber != lastPage) {
            stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&amp;")
                    .append(getPageNumberAttributeName()).append("=").append(Integer.toString(pageNumber + 1)).append("\">");
            stringBuilder.append(getMessage("label.collectionPager.next", "&gt;")).append("</a>");
            stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&amp;")
                    .append(getPageNumberAttributeName()).append("=").append(Integer.toString(lastPage)).append("\">");
            stringBuilder.append(getMessage("label.collectionPager.last", "&gt;&gt;")).append("</a>");

            Integer numberOfVisualizedPages =
                    (getNumberOfVisualizedPages() != null) ? Integer.valueOf(getNumberOfVisualizedPages()) : null;
            if (numberOfVisualizedPages != null && numberOfVisualizedPages > 0 && lastPage > numberOfVisualizedPages.intValue()) {
                stringBuilder.append(" <span style=\"color: #777;\">(Total: ").append(lastPage).append(")</span>");
            }
        }
    }

    private void addFirstSymbols(StringBuilder stringBuilder, HttpServletRequest request, final int pageNumber)
            throws JspException {
        if (pageNumber > 0 && pageNumber != 1) {
            stringBuilder.append("<a href=\"").append(request.getContextPath()).append(getUrl()).append("&amp;")
                    .append(getPageNumberAttributeName()).append("=").append("1").append("\">");
            stringBuilder.append(getMessage("label.collectionPager.first", "&lt;&lt;")).append("</a>&nbsp;");
            stringBuilder.append("<a href=\"").append(request.getContextPath()).append(getUrl()).append("&amp;")
                    .append(getPageNumberAttributeName()).append("=").append(Integer.toString(pageNumber - 1)).append("\">");
            stringBuilder.append(getMessage("label.collectionPager.previous", "&lt;")).append("</a>&nbsp;");
        }
    }

    private String getMessage(String key, String alternative) throws JspException {
        String message = getMessageFromBundle(key);
        return (message != null) ? message : alternative;
    }

    private String getMessageFromBundle(String key) throws JspException {
        return (getBundle() != null) ? ((TagUtils.getInstance().present(this.pageContext, getBundle(), I18N.getLocale()
                .toString(), key)) ? TagUtils.getInstance().message(this.pageContext, getBundle(),
                I18N.getLocale().toString(), key) : null) : null;
    }

    private Boolean hasModule() {
        return module != null;
    }

    public String getNumberOfPagesAttributeName() {
        return numberOfPagesAttributeName;
    }

    public void setNumberOfPagesAttributeName(String numberOfPagesAttributeName) {
        this.numberOfPagesAttributeName = numberOfPagesAttributeName;
    }

    public String getPageNumberAttributeName() {
        return pageNumberAttributeName;
    }

    public void setPageNumberAttributeName(String pageNumberAttributeName) {
        this.pageNumberAttributeName = pageNumberAttributeName;
    }

    public String getUrl() {
        if (hasModule()) {
            return "/" + module + (url.startsWith("/") ? "" : "/") + url;
        } else {
            return url;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getNumberOfVisualizedPages() {
        return numberOfVisualizedPages;
    }

    public void setNumberOfVisualizedPages(String numberOfVisualizedPages) {
        this.numberOfVisualizedPages = numberOfVisualizedPages;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
