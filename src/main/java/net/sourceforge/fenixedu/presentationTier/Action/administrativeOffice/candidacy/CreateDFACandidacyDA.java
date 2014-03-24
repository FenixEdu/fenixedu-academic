package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy.CreateCandidacy;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeDfaApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = MasterDegreeDfaApp.class, path = "create-candidacy",
        titleKey = "link.masterDegree.administrativeOffice.dfaCandidacy.createCandidacy")
@Mapping(path = "/createDfaCandidacy", module = "masterDegreeAdministrativeOffice",
        input = "/candidacy/chooseDFACandidacyExecutionDegree.jsp")
@Forwards(@Forward(name = "chooseExecutionDegree",
        path = "/masterDegreeAdministrativeOffice/candidacy/chooseDFACandidacyExecutionDegree.jsp"))
public class CreateDFACandidacyDA extends DFACandidacyDispatchAction {

    @EntryPoint
    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CreateDFACandidacyBean createDFACandidacyBean = new CreateDFACandidacyBean();
        request.setAttribute("candidacyBean", createDFACandidacyBean);
        return mapping.findForward("chooseExecutionDegree");
    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        CreateDFACandidacyBean createDFACandidacyBean =
                (CreateDFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Candidacy candidacy = null;
        try {
            candidacy =
                    CreateCandidacy.run(createDFACandidacyBean.getExecutionDegree(), createDFACandidacyBean.getDegreeType(),
                            createDFACandidacyBean.getName(), createDFACandidacyBean.getIdentificationNumber(),
                            createDFACandidacyBean.getIdDocumentType(), createDFACandidacyBean.getContributorNumber(),
                            createDFACandidacyBean.getCandidacyDate());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            RenderUtils.invalidateViewState();
            return prepareCreateCandidacy(mapping, actionForm, request, response);
        } catch (IllegalDataAccessException e) {
            addActionMessage(request, "error.not.authorized");
            RenderUtils.invalidateViewState();
            return prepareCreateCandidacy(mapping, actionForm, request, response);
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

}
