package net.sourceforge.fenixedu.presentationTier.TagLib.commons;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class CollectionPager extends TagSupport {

    private String url;

    private String numberOfPagesAttributeName;

    private String pageNumberAttributeName;

    public int doStartTag() throws JspException {
	String pages = createCollectionPages();
	try {
	    pageContext.getOut().print(pages);
	} catch (IOException e) {
	    e.printStackTrace(System.out);
	}
	return SKIP_BODY;
    }

    private String createCollectionPages() {
	StringBuilder stringBuilder = new StringBuilder();
	HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	
	final int lastPage = ((Integer) pageContext.findAttribute(getNumberOfPagesAttributeName())).intValue();
	final int pageNumber = ((Integer) pageContext.findAttribute(getPageNumberAttributeName())).intValue();
	
	for (int i = 1; i <= lastPage; i++) {
	    if (i == 1 && i != pageNumber) {
		stringBuilder.append("<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(pageNumber - 1)).append("\">");
		stringBuilder.append("<<").append("</a>&nbsp;");	    
	    }
	    
	    if (i != pageNumber) {
		stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(i)).append("\">");
		stringBuilder.append(Integer.toString(i)).append("</a>&nbsp;");
	    } else {
		stringBuilder.append("&nbsp;<b>").append(Integer.toString(i)).append("</b>&nbsp;");
	    }
	    
	    if (i == lastPage && i != pageNumber) {
		stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(pageNumber + 1)).append("\">");
		stringBuilder.append(">>").append("</a>");
	    }
	}
	
	return stringBuilder.toString();
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
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }
}
