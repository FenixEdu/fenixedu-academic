/*
 * Created on 20/Dec/2003
 */

package ServidorApresentacao.Action.grant.qualification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.person.InfoQualification;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantQualificationAction extends DispatchAction
{
    /*
     * Sets an error to be displayed in the page and sets the mapping forward
     */
    private ActionForward setError(
        HttpServletRequest request,
        ActionMapping mapping,
        String errorMessage,
        String forwardPage,
        Object actionArg)
    {
        ActionErrors errors = new ActionErrors();
        ActionError error = new ActionError(errorMessage,actionArg);
        errors.add(errorMessage, error);
        saveErrors(request, errors);

        if (forwardPage != null)
            return mapping.findForward(forwardPage);
        else
            return mapping.getInputForward();
    }

    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantQualificationForm(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
    	return null;
    }

    public ActionForward doEdit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
    	return null;
    }

    /*
     * Populates form from InfoContract
     */
    private void setFormGrantQualification(DynaValidatorForm form, InfoGrantContract infoGrantContract)
    {
    }

    private InfoQualification populateInfoFromForm(DynaValidatorForm editGrantQualificationForm)
    {
        return null;
    }
}
