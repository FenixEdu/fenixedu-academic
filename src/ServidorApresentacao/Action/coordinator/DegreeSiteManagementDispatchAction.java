package ServidorApresentacao.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDegreeInfo;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author Tânia Pousão Created on 31/Out/2003
 */
public class DegreeSiteManagementDispatchAction extends FenixDispatchAction
{

    public ActionForward subMenu(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        return mapping.findForward("degreeSiteMenu");
    }

    public ActionForward viewInformation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute("UserView");

        Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeId", request);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        //it's necessary to find which information will be edited
        String info2Edit = getFromRequestString("info", request);
        request.setAttribute("info", info2Edit);

        Object[] args = { infoExecutionDegreeId };

        GestorServicos gestorServicos = GestorServicos.manager();

        InfoDegreeInfo infoDegreeInfo = null;
        try
        {
            infoDegreeInfo =
                (InfoDegreeInfo) gestorServicos.executar(
                    userView,
                    "ReadDegreeInfoByExecutionDegree",
                    args);
        } catch (NotAuthorizedException e)
        {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (FenixServiceException e)
        {
            if (e.getMessage().equals("error.invalidExecutionDegree"))
            {
                errors.add("invalidExecutionDegree", new ActionError("error.invalidExecutionDegree"));
            } else if (e.getMessage().equals("error.impossibleDegreeInfo"))
            {
                errors.add("impossibleDegreeInfo", new ActionError("error.impossibleDegreeInfo"));
            } else
            {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }
        if (infoDegreeInfo == null)
        {
            errors.add("impossibleDegreeInfo", new ActionError("error.impossibleDegreeInfo"));
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        DynaActionForm degreeInfoForm = (DynaActionForm) form;
        fillForm(infoDegreeInfo, degreeInfoForm);
        Integer infoDegreeInfoId = infoDegreeInfo.getIdInternal();

        request.setAttribute("infoDegreeInfoId", infoDegreeInfoId);

        return mapping.findForward("viewInformation");
    }

    public ActionForward editDegreeInformation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute("UserView");

        Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeId", request);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);

        Integer infoDegreeInfoId = getFromRequest("infoDegreeInfoId", request);
        request.setAttribute("infoDegreeInfoId", infoDegreeInfoId);

        String info2Edit = getFromRequestString("info", request);
        request.setAttribute("info", info2Edit);

        DynaActionForm degreeInfoForm = (DynaActionForm) form;
        InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
        infoDegreeInfo.setIdInternal(infoDegreeInfoId);
        fillInfoDegreeInfo(degreeInfoForm, infoDegreeInfo);

        Object[] args = { infoExecutionDegreeId, infoDegreeInfoId, infoDegreeInfo };

        GestorServicos gestorServicos = GestorServicos.manager();

        try
        {
            gestorServicos.executar(userView, "EditDegreeInfoByExecutionDegree", args);
        } catch (NotAuthorizedException e)
        {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (FenixServiceException e)
        {
            if (e.getMessage().equals("error.impossibleEditDegreeInfo"))
            {
                errors.add(
                    "impossibleEditDegreeInfo",
                    new ActionError("error.impossibleEditDegreeInfo"));
            } else
            {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        return mapping.findForward("editOK");
    }

    private void fillForm(InfoDegreeInfo infoDegreeInfo, DynaActionForm degreeInfoForm)
    {
        degreeInfoForm.set("description", infoDegreeInfo.getDescription());
        degreeInfoForm.set("objectives", infoDegreeInfo.getObjectives());
        degreeInfoForm.set("history", infoDegreeInfo.getHistory());
        degreeInfoForm.set("professionalExits", infoDegreeInfo.getProfessionalExits());
        degreeInfoForm.set("additionalInfo", infoDegreeInfo.getAdditionalInfo());
        degreeInfoForm.set("links", infoDegreeInfo.getLinks());
        degreeInfoForm.set("testIngression", infoDegreeInfo.getTestIngression());
        degreeInfoForm.set("driftsInitial", convertInteger2String(infoDegreeInfo.getDriftsInitial()));
        degreeInfoForm.set("driftsFirst", convertInteger2String(infoDegreeInfo.getDriftsFirst()));
        degreeInfoForm.set("driftsSecond", convertInteger2String(infoDegreeInfo.getDriftsSecond()));
        degreeInfoForm.set("classifications", infoDegreeInfo.getClassifications());
        degreeInfoForm.set("markMin", convertDouble2String(infoDegreeInfo.getMarkMin()));
        degreeInfoForm.set("markMax", convertDouble2String(infoDegreeInfo.getMarkMax()));
        degreeInfoForm.set("markAverage", convertDouble2String(infoDegreeInfo.getMarkAverage()));
        degreeInfoForm.set("lastModificationDate", infoDegreeInfo.getLastModificationDate().toString());

        //information in english
        degreeInfoForm.set("descriptionEn", infoDegreeInfo.getDescriptionEn());
        degreeInfoForm.set("objectivesEn", infoDegreeInfo.getObjectivesEn());
        degreeInfoForm.set("historyEn", infoDegreeInfo.getHistoryEn());
        degreeInfoForm.set("professionalExitsEn", infoDegreeInfo.getProfessionalExitsEn());
        degreeInfoForm.set("additionalInfoEn", infoDegreeInfo.getAdditionalInfoEn());
        degreeInfoForm.set("linksEn", infoDegreeInfo.getLinksEn());
        degreeInfoForm.set("testIngressionEn", infoDegreeInfo.getTestIngressionEn());
        degreeInfoForm.set("classificationsEn", infoDegreeInfo.getClassificationsEn());
    }

    private void fillInfoDegreeInfo(DynaActionForm degreeInfoForm, InfoDegreeInfo infoDegreeInfo)
    {
        infoDegreeInfo.setDescription((String) degreeInfoForm.get("description"));
        infoDegreeInfo.setObjectives((String) degreeInfoForm.get("objectives"));
        infoDegreeInfo.setHistory((String) degreeInfoForm.get("history"));
        infoDegreeInfo.setProfessionalExits((String) degreeInfoForm.get("professionalExits"));
        infoDegreeInfo.setAdditionalInfo((String) degreeInfoForm.get("additionalInfo"));
        infoDegreeInfo.setLinks((String) degreeInfoForm.get("links"));
        infoDegreeInfo.setTestIngression((String) degreeInfoForm.get("testIngression"));
        infoDegreeInfo.setDriftsInitial(
            convertString2Integer((String) degreeInfoForm.get("driftsInitial")));
        infoDegreeInfo.setDriftsFirst(convertString2Integer((String) degreeInfoForm.get("driftsFirst")));
        infoDegreeInfo.setDriftsSecond(
            convertString2Integer((String) degreeInfoForm.get("driftsSecond")));
        infoDegreeInfo.setClassifications((String) degreeInfoForm.get("classifications"));
        infoDegreeInfo.setMarkMin(convertString2Double((String) degreeInfoForm.get("markMin")));
        infoDegreeInfo.setMarkMax(convertString2Double((String) degreeInfoForm.get("markMax")));
        infoDegreeInfo.setMarkAverage(convertString2Double((String) degreeInfoForm.get("markAverage")));

        //information in english
        infoDegreeInfo.setDescriptionEn((String) degreeInfoForm.get("descriptionEn"));
        infoDegreeInfo.setObjectivesEn((String) degreeInfoForm.get("objectivesEn"));
        infoDegreeInfo.setHistoryEn((String) degreeInfoForm.get("historyEn"));
        infoDegreeInfo.setProfessionalExitsEn((String) degreeInfoForm.get("professionalExitsEn"));
        infoDegreeInfo.setAdditionalInfoEn((String) degreeInfoForm.get("additionalInfoEn"));
        infoDegreeInfo.setLinksEn((String) degreeInfoForm.get("linksEn"));
        infoDegreeInfo.setTestIngressionEn((String) degreeInfoForm.get("testIngressionEn"));
        infoDegreeInfo.setClassificationsEn((String) degreeInfoForm.get("classificationsEn"));
    }

    public ActionForward viewDescriptionCurricularPlan(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute("UserView");

        Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeId", request);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);

        Object[] args = { infoExecutionDegreeId };

        GestorServicos gestorServicos = GestorServicos.manager();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try
        {
            infoDegreeCurricularPlan =
                (InfoDegreeCurricularPlan) gestorServicos.executar(
                    userView,
                    "ReadActiveDegreeCurricularPlanByExecutionDegreeCode",
                    args);
        } catch (NotAuthorizedException e)
        {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (NonExistingServiceException e)
        {
            errors.add("noDegreeCurricularPlan", new ActionError("error.invalidExecutionDegree"));
        } catch (FenixServiceException e)
        {
            if (e.getMessage().equals("error.invalidExecutionDegree"))
            {
                errors.add("noDegreeCurricularPlan", new ActionError("error.invalidExecutionDegree"));
            } else
            {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }
        if (infoDegreeCurricularPlan == null)
        {
            errors.add(
                "noDegreeCurricularPlan",
                new ActionError("error.coordinator.noDegreeCurricularPlan"));
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        DynaActionForm descriptionCurricularPlanForm = (DynaActionForm) form;

        descriptionCurricularPlanForm.set(
            "descriptionDegreeCurricularPlan",
            infoDegreeCurricularPlan.getDescription());
        descriptionCurricularPlanForm.set(
            "descriptionDegreeCurricularPlanEn",
            infoDegreeCurricularPlan.getDescriptionEn());

        request.setAttribute("infoDegreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());

        return mapping.findForward("viewDescriptionCurricularPlan");
    }

    public ActionForward editDescriptionDegreeCurricularPlan(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute("UserView");

        Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeId", request);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);

        Integer infoDegreeCurricularPlanId = getFromRequest("infoDegreeCurricularPlanId", request);
        request.setAttribute("infoDegreeCurricularPlanId", infoDegreeCurricularPlanId);

        DynaActionForm descriptionCurricularPlanForm = (DynaActionForm) form;

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(infoDegreeCurricularPlanId);

        infoDegreeCurricularPlan.setDescription(
            (String) descriptionCurricularPlanForm.get("descriptionDegreeCurricularPlan"));
        infoDegreeCurricularPlan.setDescriptionEn(
            (String) descriptionCurricularPlanForm.get("descriptionDegreeCurricularPlanEn"));

        Object[] args = { infoExecutionDegreeId, infoDegreeCurricularPlan };

        GestorServicos gestorServicos = GestorServicos.manager();

        try
        {
            gestorServicos.executar(userView, "EditDescriptionDegreeCurricularPlan", args);
        } catch (NotAuthorizedException e)
        {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (NonExistingServiceException e)
        {
            errors.add(
                "noDegreeCurricularPlan",
                new ActionError("message.nonExistingDegreeCurricularPlan"));
        } catch (FenixServiceException e)
        {
            if (e.getMessage().equals("message.nonExistingDegreeCurricularPlan"))
            {
                errors.add(
                    "nonExistingDegreeCurricularPlan",
                    new ActionError("message.nonExistingDegreeCurricularPlan"));
            } else
            {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        return mapping.findForward("editOK");
    }

    public ActionForward viewHistoric(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        return mapping.findForward("viewHistoric");
    }

    public Integer convertString2Integer(String string)
    {
        Integer integer = null;
        if (string != null && string.length() > 0)
        {
            integer = Integer.valueOf(string);
        }
        return integer;
    }

    public String convertInteger2String(Integer integer)
    {
        String string = null;

        if (integer != null)
        {
            string = String.valueOf(integer);
        }

        return string;
    }

    public Double convertString2Double(String string)
    {
        Double double1 = null;
        if (string != null && string.length() > 0)
        {
            double1 = Double.valueOf(string);
        }
        return double1;
    }

    public String convertDouble2String(Double double1)
    {
        String string = null;

        if (double1 != null)
        {
            string = String.valueOf(double1);
        }

        return string;
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request)
    {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null)
        {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null)
        {
            parameterCode = new Integer(parameterCodeString);
        }
        return parameterCode;
    }

    private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request)
    {
        Boolean parameterBoolean = null;

        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null)
        {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null)
        {
            parameterBoolean = new Boolean(parameterCodeString);
        }

        return parameterBoolean;
    }

    private String getFromRequestString(String parameter, HttpServletRequest request)
    {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null)
        {
            parameterString = (String) request.getAttribute(parameter);
        }

        return parameterString;
    }
}
