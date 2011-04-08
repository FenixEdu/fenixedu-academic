package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.IrsDeclarationLink;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class IRSDeclarationAction extends FenixDispatchAction {

    public static class IRSDeclarationBean implements Serializable {
    	MultiLanguageString title;
    	Boolean available;
    	String irsLink;
	public IRSDeclarationBean(IrsDeclarationLink irsDeclarationLink) {
	    title = irsDeclarationLink.getTitle();
	    available = irsDeclarationLink.getAvailable();
	    irsLink = irsDeclarationLink.getIrsLink();
	}
	public MultiLanguageString getTitle() {
	    return title;
	}
	public void setTitle(MultiLanguageString title) {
	    this.title = title;
	}
	public Boolean getAvailable() {
	    return available;
	}
	public void setAvailable(Boolean available) {
	    this.available = available;
	}
	public String getIrsLink() {
	    return irsLink;
	}
	public void setIrsLink(String irsLink) {
	    this.irsLink = irsLink;
	}
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final IrsDeclarationLink irsDeclarationLink = IrsDeclarationLink.getInstance();
	final IRSDeclarationBean declarationBean = new IRSDeclarationBean(irsDeclarationLink);
	request.setAttribute("declarationBean", declarationBean);
	return mapping.findForward("edit.IRSDeclaration.link");
    }

    public ActionForward editBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final IRSDeclarationBean declarationBean = getRenderedObject("declarationBean");
	IrsDeclarationLink.set(declarationBean.getTitle(), declarationBean.getAvailable(), declarationBean.getIrsLink());
	return mapping.findForward("edit.IRSDeclaration.link");
    }

    public ActionForward viewIrsDocumentInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("loggedPerson", AccessControl.getPerson());
	
	return mapping.findForward("view.irsDocument.information");
    }

}
