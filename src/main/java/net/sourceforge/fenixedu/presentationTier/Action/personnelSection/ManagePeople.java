package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateNewInternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPersonMatchingAnyParameter;
import net.sourceforge.fenixedu.dataTransferObject.person.InternalPersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/personnelManagePeople", module = "personnelSection", scope = "request", parameter = "method")
@Forwards({
        @Forward(name = "searchPeople", path = "/personnelSection/people/searchPeople.jsp", tileProperties = @Tile(
                title = "private.staffarea.managepeople.searchpeople")),
        @Forward(name = "createPerson", path = "/personnelSection/people/createPerson.jsp", tileProperties = @Tile(
                title = "private.staffarea.managepeople.createperson")),
        @Forward(name = "createPersonFillInfo", path = "/personnelSection/people/createPersonFillInfo.jsp",
                tileProperties = @Tile(title = "private.staffarea.managepeople.createperson")),
        @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp", tileProperties = @Tile(
                title = "private.staffarea.managepeople")) })
public class ManagePeople extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("searchPeople");
    }

    public ActionForward prepareCreatePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final AnyPersonSearchBean anyPersonSearchBean = new AnyPersonSearchBean();
        request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
        return mapping.findForward("createPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("anyPersonSearchBeanId");
        AnyPersonSearchBean bean = (AnyPersonSearchBean) viewState.getMetaObject().getObject();

        CollectionPager<Person> result =
                SearchPersonMatchingAnyParameter.run(bean.getName(), null, null, bean.getDocumentIdNumber(),
                        bean.getIdDocumentType(), null, null, null, null, null, null, null);

        request.setAttribute("resultPersons", result.getCollection());
        request.setAttribute("anyPersonSearchBean", bean);
        return mapping.findForward("createPerson");
    }

    private void setRequestParametersToCreateInvitedPerson(final HttpServletRequest request, final InternalPersonBean personBean) {

        AnyPersonSearchBean anyPersonSearchBean = getRenderedObject("anyPersonSearchBeanId");
        if (anyPersonSearchBean != null) {
            personBean.setName(anyPersonSearchBean.getName());
            personBean.setIdDocumentType(anyPersonSearchBean.getIdDocumentType());
            personBean.setDocumentIdNumber(anyPersonSearchBean.getDocumentIdNumber());
        } else {
            final String name = request.getParameter("name");
            if (isSpecified(name)) {
                personBean.setName(name);
            }
            final String idDocumentType = request.getParameter("idDocumentType");
            if (isSpecified(idDocumentType)) {
                personBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
            }
            final String documentIdNumber = request.getParameter("documentIdNumber");
            if (isSpecified(documentIdNumber)) {
                personBean.setDocumentIdNumber(documentIdNumber);
            }
        }
        request.setAttribute("personBean", personBean);
    }

    private boolean isSpecified(final String string) {
        return !StringUtils.isEmpty(string);
    }

    public ActionForward prepareCreatePersonFillInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        setRequestParametersToCreateInvitedPerson(request, new InternalPersonBean());
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward createNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final InternalPersonBean bean = getRenderedObject();
        try {
            final Person person = CreateNewInternalPerson.run(bean);
            return viewPerson(person, mapping, request);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("invitedPersonBean", bean);
            return mapping.findForward("createPersonFillInfo");
        }
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InternalPersonBean bean = getRenderedObject();
        request.setAttribute("invitedPersonBean", bean);
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        return viewPerson(person, mapping, request);
    }

    public ActionForward viewPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request)
            throws Exception {
        request.setAttribute("person", person);
        return mapping.findForward("viewPerson");
    }

    public ActionForward attributeRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        final RoleType roleType = RoleType.valueOf(request.getParameter("roleType"));
        CreateNewInternalPerson.attributeRoles(person, Collections.singleton(roleType));
        return viewPerson(person, mapping, request);
    }

    public ActionForward prepareImportPersons(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("personsUploadBean", new PersonsUploadBean());
        return prepareCreatePerson(mapping, actionForm, request, response);
    }

    public ActionForward importPersons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final PersonsUploadBean personsUploadBean = getRenderedObject();
        final PersonsBatchImporter batchImporter = new PersonsBatchImporter(personsUploadBean.getInputStream());

        batchImporter.createPersons();

        request.setAttribute("resultPersons", batchImporter.getPersons());
        request.setAttribute("anyPersonSearchBean", new AnyPersonSearchBean());

        return mapping.findForward("createPerson");
    }

}
