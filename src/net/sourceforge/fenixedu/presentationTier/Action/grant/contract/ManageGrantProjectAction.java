/*
 * Created on 15/Fev/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Barbosa
 * @author Pica
 */
@Mapping(module = "facultyAdmOffice", path = "/manageGrantProject",
        input = "/manageGrantProject.do?page=0&method=prepareManageGrantProject", attribute = "voidForm", formBean = "voidForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "manage-grant-project", path = "/facultyAdmOffice/grant/contract/manageGrantProject.jsp",
        tileProperties = @Tile(title = "private.teachingstaffandresearcher.miscellaneousmanagement.projects")) })
public class ManageGrantProjectAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantProject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<GrantProject> grantProjectList = new ArrayList<GrantProject>();
        for (final GrantPaymentEntity grantPaymentEntity : rootDomainObject.getGrantPaymentEntitys()) {
            if (grantPaymentEntity.isGrantProject()) {
                grantProjectList.add((GrantProject) grantPaymentEntity);
            }
        }
        request.setAttribute("grantProjectList", grantProjectList);
        return mapping.findForward("manage-grant-project");

    }
}