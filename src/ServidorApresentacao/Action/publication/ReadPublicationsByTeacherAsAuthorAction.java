/*
 * Created on Jun 8, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorApresentacao.Action.publication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.SiteView;
import DataBeans.publication.InfoSitePublications;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import constants.publication.PublicationConstants;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadPublicationsByTeacherAsAuthorAction extends FenixDispatchAction {

    public ActionForward readPublicationsAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        String typePublication = request.getParameter("typePublication");

        ActionForward actionForward = mapping.findForward("show-cientific-author-form");

        if (session != null) {

            Object[] argsToReadPublicationsOfTeacher = { userView.getUtilizador() };
            SiteView siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ReadAuthorPublicationsToInsert", argsToReadPublicationsOfTeacher);

            InfoSitePublications infoSitePublications = (InfoSitePublications) siteView.getComponent();
            request.setAttribute("infoSitePublications", infoSitePublications);
        }
        if (typePublication.equals(PublicationConstants.DIDATIC_STRING)) {
            actionForward = mapping.findForward("show-didatic-author-form");
        }
        return actionForward;
    }

    public ActionForward insertPublicationTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        String typePublication = request.getParameter("typePublication");

        Integer teacherId = new Integer(request.getParameter("teacherId"));

        Integer publicationId = new Integer(request.getParameter("idPublication"));

        ActionForward actionForward = mapping.findForward("show-cientific-Teacher-form");

        if (typePublication.equals(PublicationConstants.DIDATIC_STRING)) {
            actionForward = mapping.findForward("show-didatic-Teacher-form");
        }
        try {
            if (session != null) {

                Object[] argsToInsertPublicationInTeacherSList = { teacherId, publicationId,
                        typePublication };
                SiteView siteView = (SiteView) ServiceUtils.executeService(userView,
                        "InsertPublicationInTeacherList", argsToInsertPublicationInTeacherSList);

                request.setAttribute("siteView", siteView);
            }

        } catch (ExistingServiceException e) {
            sendErrors(request, "existing", "message.publication.alreadyInserted");
            return actionForward;
        }
        return actionForward;
    }

    public ActionForward deletePublicationTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        String typePublication = request.getParameter("typePublication");

        Integer teacherId = new Integer(request.getParameter("teacherId"));

        Integer publicationId = new Integer(request.getParameter("idPublication"));

        ActionForward actionForward = mapping.findForward("show-cientific-Teacher-form");
        //DynaActionForm dynaForm = (DynaActionForm) form;

        if (typePublication.equals(PublicationConstants.DIDATIC_STRING)) {
            actionForward = mapping.findForward("show-didatic-Teacher-form");
        }
        try {
            if (session != null) {

                Object[] argsToInsertPublicationInTeacherSList = { teacherId, publicationId };
                SiteView siteView = (SiteView) ServiceUtils.executeService(userView,
                        "DeletePublicationInTeacherList", argsToInsertPublicationInTeacherSList);

                request.setAttribute("siteView", siteView);
            }

        } catch (NotExistingServiceException e) {

            if (typePublication.equals(PublicationConstants.DIDATIC_STRING)) {
                actionForward = mapping.findForward("show-didatic-Teacher-form");
            }
            sendErrors(request, "nonExisting", "message.publication.notfound");
            return actionForward;
        }
        return actionForward;
    }

    private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
        ActionErrors errors = new ActionErrors();
        errors.add(arg0, new ActionError(arg1));
        saveErrors(request, errors);
    }

}