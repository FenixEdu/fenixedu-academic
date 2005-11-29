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

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.IFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IPersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice.FunctionsManagementBackingBean;

public class ManagerFunctionsManagementBackingBean extends FunctionsManagementBackingBean {

    public ManagerFunctionsManagementBackingBean() {

    }

    public String associateNewFunction() throws FenixFilterException, FenixServiceException,
            ParseException {

        if (this.getPerson().containsActiveFunction(this.getFunction())) {
            setErrorMessage("error.duplicate.function");
        } else {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Integer credits = Integer.valueOf(this.getCredits());

            Date beginDate_ = null, endDate_ = null;
            try {
                beginDate_ = format.parse(this.getBeginDate());
                endDate_ = format.parse(this.getEndDate());

                final Object[] argsToRead = { this.getFunctionID(), this.getPersonID(), credits,
                        beginDate_, endDate_ };
                ServiceUtils.executeService(getUserView(), "AssociateNewFunctionToPerson", argsToRead);
                setErrorMessage("message.success");
                return "success";

            } catch (ParseException e) {
                setErrorMessage("error.date1.format");
            } catch (FenixServiceException e) {
                setErrorMessage(e.getMessage());
            }
        }
        return "";
    }

    public String getUnits() throws FenixFilterException, FenixServiceException {
        StringBuffer buffer = new StringBuffer();

        final Object[] argsToRead = { Unit.class };

        List<IUnit> allUnits = new ArrayList<IUnit>();
        allUnits.addAll((List<IUnit>) ServiceUtils.executeService(null, "ReadAllDomainObject",
                argsToRead));

        Date currentDate = Calendar.getInstance().getTime();
        for (IUnit unit : allUnits) {
            if (unit.getParentUnits().isEmpty() && unit.isActive(currentDate)) {
                getUnitTree(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuffer buffer, IUnit parentUnit) {
        buffer.append("<ul>");
        getUnitsList(parentUnit, 0, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(IUnit parentUnit, int index, StringBuffer buffer) {

        buffer.append("<li>").append("<a href=\"").append(getContextPath()).append(
                "/manager/functionsManagement/chooseFunction.faces?personID=").append(personID).append(
                "&unitID=").append(parentUnit.getIdInternal()).append("\">")
                .append(parentUnit.getName()).append("</a>").append("</li>").append("<ul>");

        Date currentDate = Calendar.getInstance().getTime();
        for (IUnit subUnit : parentUnit.getSubUnits()) {
            if (subUnit.isActive(currentDate)) {
                getUnitsList(subUnit, index + 1, buffer);
            }
        }

        buffer.append("</ul>");
    }

    public List<IPersonFunction> getActiveFunctions() throws FenixFilterException, FenixServiceException {

        if (this.activeFunctions == null) {
            IPerson person = this.getPerson();
            List<IPersonFunction> activeFunctions = person.getActiveFunctions();
            Collections.sort(activeFunctions, new BeanComparator("endDate"));
            this.activeFunctions = activeFunctions;
        }
        return activeFunctions;
    }

    public List<IPersonFunction> getInactiveFunctions() throws FenixFilterException,
            FenixServiceException {

        if (this.inactiveFunctions == null) {
            IPerson person = this.getPerson();
            List<IPersonFunction> inactiveFunctions = person.getInactiveFunctions();
            Collections.sort(inactiveFunctions, new BeanComparator("endDate"));
            this.inactiveFunctions = inactiveFunctions;
        }
        return inactiveFunctions;
    }

    public List<IFunction> getInherentFunctions() throws FenixFilterException, FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = this.getPerson().getActiveInherentFunctions();
        }
        return inherentFunctions;
    }
}
