/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis.ListMasterDegreeProofsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/listConcludedMasterDegreeProofs", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "showList",
        path = "/masterDegreeAdministrativeOffice/lists/listConcludedMasterDegreeProofs.jsp", tileProperties = @Tile(
                navLocal = "/masterDegreeAdministrativeOffice/lists/listsMenu.jsp",
                navGeral = "/masterDegreeAdministrativeOffice/commonNavGeralPosGraduacao.jsp",
                title = "private.postgraduateoffice.listings")) })
public class ListConcludedMasterDegreeProofsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        ListMasterDegreeProofsBean bean = null;

        if (RenderUtils.getViewState() != null) {
            bean = (ListMasterDegreeProofsBean) RenderUtils.getViewState().getMetaObject().getObject();
        }

        if (bean == null) {
            bean = new ListMasterDegreeProofsBean();
        }

        request.setAttribute("chooseDegreeAndYearBean", bean);
        return mapping.findForward("showList");
    }
}
