/*
 * Created on Nov 4, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice.FunctionsManagementBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class ManagerFunctionsManagementBackingBean extends FunctionsManagementBackingBean {

    public ManagerFunctionsManagementBackingBean() {

    }

    public String associateNewFunction() throws FenixFilterException, FenixServiceException,
            ParseException {

        if (this.getPerson().containsActiveFunction(this.getFunction())) {
            setErrorMessage("error.duplicate.function");
        } else {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");            
            Double credits = Double.valueOf(this.getCredits());

            Date beginDate_ = null, endDate_ = null;
            try {
                if(this.getBeginDate() != null){
                    beginDate_ = format.parse(this.getBeginDate());
                }else{
                    setErrorMessage("error.notBeginDate");
                    return "";
                }
                if (this.getEndDate() != null) {
                    endDate_ = format.parse(this.getEndDate());
                }else{
                    setErrorMessage("error.notEndDate");
                    return "";
                }

                final Object[] argsToRead = { this.getFunctionID(), this.getPersonID(), credits,
                        beginDate_, endDate_ };
                ServiceUtils.executeService(getUserView(), "AssociateNewFunctionToPerson", argsToRead);
                setErrorMessage("message.success");
                return "success";

            } catch (ParseException e) {
                setErrorMessage("error.date1.format");
            } catch (FenixServiceException e) {
                setErrorMessage(e.getMessage());
            } catch (DomainException e) {
                setErrorMessage(e.getMessage());
            }
        }
        return "";
    }

    public String getUnits() throws FenixFilterException, FenixServiceException {
        
        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnits = readAllDomainObjects(Unit.class);

        Collections.sort(allUnits, new BeanComparator("name"));
        
        Date currentDate = Calendar.getInstance().getTime();
        for (Unit unit : allUnits) {
            if (unit.getParentUnits().isEmpty() && unit.isActive(currentDate)) {
                getUnitTree(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit) {
        buffer.append("<ul class='padding nobullet'>");
        getUnitsList(parentUnit, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, StringBuilder buffer) {
        
        buffer.append("<li>");

        if (parentUnit.hasAnySubUnits()) {
            buffer.append("<img ").append("src='").append(getContextPath()).append(
                    "/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
                    .append("indexed='true' onClick=\"").append("check(document.getElementById('")
                    .append("aa").append(parentUnit.getIdInternal()).append(
                            "'),document.getElementById('").append(parentUnit.getIdInternal()).append(
                            "'));return false;").append("\"> ");
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/manager/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>");
        
        if (parentUnit.hasAnySubUnits()) {
            buffer.append("<ul class='mvert0' id=\"").append("aa").append(parentUnit.getIdInternal())
                    .append("\" ").append("style='display:none'>\r\n");                                    
        }

        List<Unit> subUnits = new ArrayList<Unit>();
        subUnits.addAll(parentUnit.getSubUnits());
        Collections.sort(subUnits, new BeanComparator("name"));
        
        for (Unit subUnit : subUnits) {
            if (subUnit.isActive(Calendar.getInstance().getTime())) {
                getUnitsList(subUnit, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            buffer.append("</ul>");
        }
    }

    public List<PersonFunction> getActiveFunctions() throws FenixFilterException, FenixServiceException {

        if (this.activeFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> activeFunctions = person.getActiveFunctions();
            Collections.sort(activeFunctions, new BeanComparator("endDate"));
            this.activeFunctions = activeFunctions;
        }
        return activeFunctions;
    }

    public List<PersonFunction> getInactiveFunctions() throws FenixFilterException,
            FenixServiceException {

        if (this.inactiveFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> inactiveFunctions = person.getInactiveFunctions();
            Collections.sort(inactiveFunctions, new BeanComparator("endDate"));
            this.inactiveFunctions = inactiveFunctions;
        }
        return inactiveFunctions;
    }

    public List<Function> getInherentFunctions() throws FenixFilterException, FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = this.getPerson().getActiveInherentFunctions();
        }
        return inherentFunctions;
    }
}
