package ServidorApresentacao.Action.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.publication.InfoAuthor;
import DataBeans.publication.InfoSiteAttributes;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Carlos Pereira
 * @author Francisco Passos
 *  
 */
public class PrepareInsertPublicationAction extends FenixDispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepareInsert(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        if ((session != null)) {

            Integer keyTeacher = new Integer(request.getParameter("infoTeacher#idInternal"));
            dynaForm.set("authorIdInternal",keyTeacher);
            
            Object[] args = { new Integer(1) }; //journal
            InfoSiteAttributes siteAttributes = (InfoSiteAttributes)
                ServiceUtils.executeService(userView, "ReadAllPublicationAttributes", args);
    		request.setAttribute("siteAttributes",siteAttributes);

            request.setAttribute("publicationManagementForm",dynaForm);
        }

        return mapping.findForward("insert-publication");

    }

    public ActionForward moveAuthorDown(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        System.out.println("Vamos a ver: " + dynaForm.get("authorIdInternal"));
        ArrayList authors = (ArrayList) dynaForm.get("authors");
        Integer selectedId = Integer.valueOf(request.getParameter("idInternal"));
        System.out.println("Vamos a ver o id: " + selectedId);
        
        System.out.println("Size do authors: "+authors.size());

        Iterator iterator = authors.iterator();
        for (int iter = 0; iterator.hasNext(); iter++) {
            InfoAuthor infoAuthor = (InfoAuthor) iterator.next();
            if (infoAuthor.getIdInternal().equals(selectedId)) {
                try {
                	Collections.swap(authors, iter, --iter);
	            } catch (IndexOutOfBoundsException ioobe) {
	                //this empty catch is on purpose :)
	            }
                break;
            }
        }

        //dynaForm.set("authors", authors);

        //request.setAttribute("infoAuthors", authors);
        request.setAttribute("publicationManagementForm",dynaForm);

        return mapping.findForward("insert-publication");

    }

    public ActionForward moveAuthorUp(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        System.out.println("Vamos a ver: " + dynaForm.get("idInternal"));
        ArrayList authors = (ArrayList) dynaForm.get("authors");
        Integer selectedId = (Integer) request.getAttribute("idInternal");

        Iterator iterator = authors.iterator();
        for (int iter = 0; iterator.hasNext(); iter++) {
            InfoAuthor infoAuthor = (InfoAuthor) iterator.next();
            if (infoAuthor.getIdInternal().equals(selectedId)) {
                try {
                    Collections.swap(authors, iter, ++iter);
                } catch (IndexOutOfBoundsException ioobe) {
                }
                break;
            }
        }

        dynaForm.set("authors", authors);

        request.setAttribute("infoAuthors", authors);

        return mapping.findForward("insert-publication");

    }
}