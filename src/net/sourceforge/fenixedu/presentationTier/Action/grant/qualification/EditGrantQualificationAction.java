/*
 * Created on 20/Dec/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.qualification;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantQualificationAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantQualificationForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer idQualification = null;
        if (verifyParameterInRequest(request, "idQualification")) {
            idQualification = new Integer(request.getParameter("idQualification"));
        }

        DynaValidatorForm grantQualificationForm = (DynaValidatorForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer idPerson = null;

        if (idQualification != null && request.getParameter("load") != null) //Edit
        {
            try {
                //Read the qualification
                Object[] args = { idQualification };
                InfoQualification infoGrantQualification = (InfoQualification) ServiceUtils
                        .executeService(userView, "ReadQualification", args);

                //Populate the form
                setFormGrantQualification(grantQualificationForm, infoGrantQualification);
                idPerson = infoGrantQualification.getInfoPerson().getIdInternal();
            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.qualification.read",
                        "manage-grant-qualification", null);
            }
        } else //New
        {
            try {
                if (verifyParameterInRequest(request, "idPerson")) {
                    idPerson = new Integer(request.getParameter("idPerson"));
                }
                grantQualificationForm.set("idPerson", idPerson);
            } catch (Exception e) {
                return setError(request, mapping, "errors.grant.unrecoverable",
                        "manage-grant-qualification", null);
            }
        }

        //Setting request values
        request.setAttribute("idPerson", idPerson);
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("idGrantOwner", request.getParameter("idGrantOwner"));
        request.setAttribute("grantOwnerNumber", request.getParameter("grantOwnerNumber"));

        List countryList = null;
        try {
            countryList = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadAllCountries", null);

            //Adding a select country line to the list (presentation reasons)
            InfoCountry selectCountry = new InfoCountry();
            selectCountry.setIdInternal(null);
            selectCountry.setName("[Escolha um país]");
            countryList.add(0, selectCountry);
            request.setAttribute("countryList", countryList);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable",
                    "manage-grant-qualification", null);
        }
        return mapping.findForward("edit-grant-qualification");
    }

    /*
     * Editing a qualification
     */
    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            DynaValidatorForm editGrantQualificationForm = (DynaValidatorForm) form;

            //Populate info from Form Bean
            InfoQualification infoGrantQualification = populateInfoFromForm(editGrantQualificationForm);

            //Setting request
            request.setAttribute("idInternal", editGrantQualificationForm.get("idGrantOwner"));
            request.setAttribute("idPerson", editGrantQualificationForm.get("idPerson"));
            request.setAttribute("grantOwnerNumber", request.getParameter("grantOwnerNumber"));
            request.setAttribute("username", request.getParameter("username"));

            //Run Service
            Object[] args = { infoGrantQualification.getIdInternal(), infoGrantQualification };
            IUserView userView = SessionUtils.getUserView(request);
            ServiceUtils.executeService(userView, "EditQualification", args);
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.qualification.bd.create", null, null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
        return mapping.findForward("manage-grant-qualification");
    }

    /*
     * Delete a qualification
     */
    public ActionForward doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            Integer idQualification = new Integer(request.getParameter("idQualification"));

            //Setting request
            request.setAttribute("idInternal", request.getParameter("idGrantOwner"));
            request.setAttribute("idPerson", request.getParameter("idPerson"));
            request.setAttribute("grantOwnerNumber", request.getParameter("grantOwnerNumber"));
            request.setAttribute("username", request.getParameter("username"));

            //Run Service
            Object[] args = { idQualification };
            IUserView userView = SessionUtils.getUserView(request);
            ServiceUtils.executeService(userView, "DeleteQualification", args);
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.qualification.bd.delete", null, null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
        return mapping.findForward("manage-grant-qualification");
    }

    /*
     * Populates form from InfoContract
     */
    private void setFormGrantQualification(DynaValidatorForm form,
            InfoQualification infoGrantQualification) throws Exception {
        form.set("idPerson", infoGrantQualification.getInfoPerson().getIdInternal());

        form.set("mark", infoGrantQualification.getMark());
        form.set("school", infoGrantQualification.getSchool());
        form.set("title", infoGrantQualification.getTitle());
        form.set("degree", infoGrantQualification.getDegree());
        form.set("branch", infoGrantQualification.getBranch());
        form.set("specializationArea", infoGrantQualification.getSpecializationArea());
        form.set("degreeRecognition", infoGrantQualification.getDegreeRecognition());
        form.set("equivalenceSchool", infoGrantQualification.getEquivalenceSchool());
        form.set("idQualification", infoGrantQualification.getIdInternal());

        if (infoGrantQualification.getInfoCountry() != null)
            form.set("country", infoGrantQualification.getInfoCountry().getIdInternal());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (infoGrantQualification.getDate() != null)
            form.set("qualificationDate", sdf.format(infoGrantQualification.getDate()));
        if (infoGrantQualification.getEquivalenceDate() != null)
            form.set("equivalenceDate", sdf.format(infoGrantQualification.getEquivalenceDate()));
    }

    private InfoQualification populateInfoFromForm(DynaValidatorForm editGrantQualificationForm)
            throws Exception {
        InfoQualification infoQualification = new InfoQualification();

        infoQualification.setSchool((String) editGrantQualificationForm.get("school"));
        infoQualification.setDegree((String) editGrantQualificationForm.get("degree"));

        if (verifyStringParameterInForm(editGrantQualificationForm, "idQualification"))
            infoQualification.setIdInternal((Integer) editGrantQualificationForm.get("idQualification"));
        if (verifyStringParameterInForm(editGrantQualificationForm, "mark"))
            infoQualification.setMark((String) editGrantQualificationForm.get("mark"));
        if (verifyStringParameterInForm(editGrantQualificationForm, "title"))
            infoQualification.setTitle((String) editGrantQualificationForm.get("title"));
        if (verifyStringParameterInForm(editGrantQualificationForm, "branch"))
            infoQualification.setBranch((String) editGrantQualificationForm.get("branch"));
        if (verifyStringParameterInForm(editGrantQualificationForm, "specializationArea"))
            infoQualification.setSpecializationArea((String) editGrantQualificationForm
                    .get("specializationArea"));
        if (verifyStringParameterInForm(editGrantQualificationForm, "degreeRecognition"))
            infoQualification.setDegreeRecognition((String) editGrantQualificationForm
                    .get("degreeRecognition"));
        if (verifyStringParameterInForm(editGrantQualificationForm, "equivalenceSchool"))
            infoQualification.setEquivalenceSchool((String) editGrantQualificationForm
                    .get("equivalenceSchool"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyStringParameterInForm(editGrantQualificationForm, "qualificationDate"))
            infoQualification.setDate(sdf.parse((String) editGrantQualificationForm
                    .get("qualificationDate")));
        if (verifyStringParameterInForm(editGrantQualificationForm, "equivalenceDate"))
            infoQualification.setEquivalenceDate(sdf.parse((String) editGrantQualificationForm
                    .get("equivalenceDate")));

        //Setting person
        final Person person = (Person) rootDomainObject.readPartyByOID((Integer) editGrantQualificationForm.get("idPerson"));
        infoQualification.setInfoPerson(InfoPerson.newInfoFromDomain(person));

        //Setting country
        InfoCountry infoCountry = new InfoCountry();
        if (((Integer) editGrantQualificationForm.get("country")).equals(new Integer(0))) {
            infoCountry.setIdInternal(null);
        } else {
            infoCountry.setIdInternal((Integer) editGrantQualificationForm.get("country"));
        }
        infoQualification.setInfoCountry(infoCountry);

        return infoQualification;
    }
}