package net.sourceforge.fenixedu.presentationTier.TagLib.commons;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.struts.taglib.TagUtils;

public class CollectionPagerTag extends TagSupport {

    private String url;
    
    private String bundle;

    private String numberOfPagesAttributeName;

    private String pageNumberAttributeName;
    
    private String numberOfVisualizedPages;

    
    public int doStartTag() throws JspException {
	String pages = createCollectionPages();
	try {
	    pageContext.getOut().print(pages);
	} catch (IOException e) {
	    e.printStackTrace(System.out);
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

    private void generatePageNumbers(StringBuilder stringBuilder, HttpServletRequest request, final int lastPage, final int pageNumber) {
	
	int firstVisualizedPage = 1;
	int lastVisualizedPage = lastPage;
	Integer numberOfVisualizedPages = (getNumberOfVisualizedPages() != null) ? Integer.valueOf(getNumberOfVisualizedPages()) : null;
	
	if(lastPage > 0 && numberOfVisualizedPages != null && numberOfVisualizedPages.intValue() > 0
		&& lastPage > numberOfVisualizedPages.intValue()) {
	    
	    if(pageNumber == 1) {
		lastVisualizedPage = numberOfVisualizedPages;		
	    
	    } else if(pageNumber == lastPage) {
		firstVisualizedPage = lastPage - (numberOfVisualizedPages - 1);			    
	    
	    } else {    	   
                int numberOfLeftAndRightPages = (int)Math.floor((numberOfVisualizedPages - 1) / 2);	    
                int leftPage = (pageNumber - numberOfLeftAndRightPages) <= 0 ? 1 : pageNumber - numberOfLeftAndRightPages;
                int rightPage = numberOfLeftAndRightPages + pageNumber;	    	    
                
                firstVisualizedPage = (pageNumber != 1) ? (leftPage >= 1 ? leftPage : 1) : 1;
                lastVisualizedPage = (pageNumber != lastPage) ? (rightPage <= lastPage ? rightPage : lastPage) : lastPage;
                
                int numberOfResultPages = Math.abs(firstVisualizedPage - lastVisualizedPage) + 1;
                if(firstVisualizedPage == 1) {
                    lastVisualizedPage = lastVisualizedPage + (numberOfVisualizedPages - numberOfResultPages);
                } else if(lastVisualizedPage == lastPage) {
                    firstVisualizedPage = firstVisualizedPage - (numberOfVisualizedPages - numberOfResultPages);
                }                
	    }
	} 
	
	for (int i = firstVisualizedPage; i <= lastVisualizedPage; i++) {	    	   
	    if (i != pageNumber) {
		stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(i)).append("\">");
		stringBuilder.append(Integer.toString(i)).append("</a>&nbsp;");	   
	    } else {
		stringBuilder.append("&nbsp;<b>").append(Integer.toString(i)).append("</b>&nbsp;");
	    }	    	   
	}
    }

    private void addLastSymbols(StringBuilder stringBuilder, HttpServletRequest request, final int lastPage, final int pageNumber) throws JspException {
	if (lastPage > 1 && pageNumber != lastPage) {           
	    stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(pageNumber + 1)).append("\">");            
	    stringBuilder.append(getMessage("label.collectionPager.next", ">")).append("</a>");            
            stringBuilder.append("&nbsp;<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(lastPage)).append("\">");         
            stringBuilder.append(getMessage("label.collectionPager.last", ">>")).append("</a>");
            
            Integer numberOfVisualizedPages = (getNumberOfVisualizedPages() != null) ? Integer.valueOf(getNumberOfVisualizedPages()) : null;    	
            if(numberOfVisualizedPages != null && numberOfVisualizedPages > 0 && lastPage > numberOfVisualizedPages.intValue()) {	    
        	stringBuilder.append(" <span style=\"color: #777;\">(Total: ").append(lastPage).append(")</span>");
            }
        }
    }

    private void addFirstSymbols(StringBuilder stringBuilder, HttpServletRequest request, final int pageNumber) throws JspException {
	if (pageNumber > 0 && pageNumber != 1) {	    
            stringBuilder.append("<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append("1").append("\">");            
            stringBuilder.append(getMessage("label.collectionPager.first", "<<" )).append("</a>&nbsp;");                        
            stringBuilder.append("<a href=\"").append(request.getContextPath()).append(getUrl()).append("&").append(getPageNumberAttributeName()).append("=").append(Integer.toString(pageNumber - 1)).append("\">");                       
            stringBuilder.append(getMessage("label.collectionPager.previous", "<" )).append("</a>&nbsp;");
	}
    }
    
    private String getMessage(String key, String alternative) throws JspException {
	String message = getMessageFromBundle(key);
	return (message != null) ? message : alternative;
    }

    private String getMessageFromBundle(String key) throws JspException {
	return (getBundle() != null) ? ((TagUtils.getInstance().present(this.pageContext, getBundle(),
		LanguageUtils.getLocale().toString(), key)) ? TagUtils.getInstance().message(
		this.pageContext, getBundle(), LanguageUtils.getLocale().toString(), key) : null) : null;
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
}
