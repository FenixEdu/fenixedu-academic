package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

public class ManageSpaceResponsibilityDA extends FenixDispatchAction {

    public ActionForward showSpaceResponsibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        setUnitsList(request, spaceInformation);        
        return mapping.findForward("showSpaceResponsibility");
    }
    
    public ActionForward deleteSpaceResponsibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        SpaceResponsibility spaceResponsibility = getSpaceResponsibility(request);                
        Object[] args = { spaceResponsibility };
        try {
            ServiceUtils.executeService(getUserView(request), "DeleteSpaceResponsibility", args);
        } catch(DomainException domainException) {
            
        }

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        setUnitsList(request, spaceInformation);
        return mapping.findForward("showSpaceResponsibility");
    }

    public ActionForward prepareEditSpaceResponsibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        SpaceResponsibility spaceResponsibility = getSpaceResponsibility(request);
        request.setAttribute("spaceResponsibility", spaceResponsibility);
        return mapping.findForward("manageResponsabilityInterval");        
    }    
    
    public ActionForward manageResponsabilityInterval(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
    
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getResponsibleUnit(request);
        request.setAttribute("unit", responsibleUnit); 
        return mapping.findForward("manageResponsabilityInterval");
    }
    
    private void setSpaceInformation(HttpServletRequest request, final SpaceInformation spaceInformation) {
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString = request.getParameterMap().containsKey(
                "spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                .getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer
                .valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }
    
    private SpaceResponsibility getSpaceResponsibility(final HttpServletRequest request) {
        final String spaceResponsibilityIDString = request.getParameterMap().containsKey(
                "spaceResponsibilityID") ? request.getParameter("spaceResponsibilityID") : (String) request
                .getAttribute("spaceResponsibilityID");
        final Integer spaceResponsibilityID = spaceResponsibilityIDString != null ? Integer
                .valueOf(spaceResponsibilityIDString) : null;
        return rootDomainObject.readSpaceResponsibilityByOID(spaceResponsibilityID);
    }
    
    private Unit getResponsibleUnit(final HttpServletRequest request) {
        final String unitIDString = request.getParameterMap().containsKey(
                "unitID") ? request.getParameter("unitID") : (String) request
                .getAttribute("unitID");
        final Integer unitID = unitIDString != null ? Integer
                .valueOf(unitIDString) : null;
        return (Unit) rootDomainObject.readPartyByOID(unitID);
    }

    public void setUnitsList(HttpServletRequest request, SpaceInformation spaceInformation) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        
        StringBuilder buffer = new StringBuilder();
        Unit instituionUnit = UnitUtils.readInstitutionUnit();
        YearMonthDay currentDate = new YearMonthDay();

        buffer.append("<ul class='padding1 nobullet'>");
        getSubUnitsList(instituionUnit, buffer, currentDate, request, spaceInformation);
        buffer.append("</ul>");

        request.setAttribute("unitsList", buffer.toString());
    }

    private void getSubUnitsList(Unit parentUnit, StringBuilder buffer, YearMonthDay currentDate,
            HttpServletRequest request, SpaceInformation spaceInformation) {

        buffer.append("<li>");

        List<Unit> subUnits = getUnitSubUnits(parentUnit, currentDate);
        if (!subUnits.isEmpty()) {
//            if(parentUnit.hasAnyParentUnits()) {
                putImage(parentUnit, buffer, request);
//            }
        }

        buffer.append("<a href=\"").append(request.getContextPath()).append("/SpaceManager/").append(
                "manageSpaceResponsibility.do?method=manageResponsabilityInterval").append("&unitID=")
                .append(parentUnit.getIdInternal()).append("&spaceInformationID=").append(spaceInformation.getIdInternal())
                .append("\">").append(parentUnit.getName()).append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
//            if(parentUnit.hasAnyParentUnits()) {
                buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(parentUnit.getIdInternal())
                        .append("\" ").append("style='display:none'>\r\n");
//            } else {
//                buffer.append("<ul class='padding1 nobullet'>");
//            }
            Collections.sort(subUnits, new BeanComparator("name"));
        }

        for (Unit subUnit : subUnits) {
            getSubUnitsList(subUnit, buffer, currentDate, request, spaceInformation);
        }

        if (!subUnits.isEmpty()) {
            buffer.append("</ul>");
        }
    }

    private List<Unit> getUnitSubUnits(Unit parentUnit, YearMonthDay currentDate) {
        List<AccountabilityTypeEnum> accountabilityEnums = new ArrayList<AccountabilityTypeEnum>();
        accountabilityEnums.add(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        accountabilityEnums.add(AccountabilityTypeEnum.ACADEMIC_STRUCTURE);
        return new ArrayList(parentUnit.getActiveSubUnits(currentDate, accountabilityEnums));
    }

    private void putImage(Unit parentUnit, StringBuilder buffer, HttpServletRequest request) {
        buffer.append("<img ").append("src='").append(request.getContextPath()).append(
                "/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
                .append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
                        "aa").append(parentUnit.getIdInternal()).append("'),document.getElementById('")
                .append(parentUnit.getIdInternal()).append("'));return false;").append("\"> ");
    }
}
