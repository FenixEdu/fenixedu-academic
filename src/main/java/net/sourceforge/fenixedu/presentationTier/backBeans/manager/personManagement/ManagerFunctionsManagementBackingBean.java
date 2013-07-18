/*
 * Created on Nov 4, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.AssociateNewFunctionToPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice.FunctionsManagementBackingBean;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class ManagerFunctionsManagementBackingBean extends FunctionsManagementBackingBean {

    public ManagerFunctionsManagementBackingBean() {
    }

    @Override
    public String associateNewFunction() throws  FenixServiceException, ParseException {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Double credits = Double.valueOf(this.getCredits());

        Date beginDate_ = null, endDate_ = null;
        try {
            if (this.getBeginDate() != null) {
                beginDate_ = format.parse(this.getBeginDate());
            } else {
                setErrorMessage("error.notBeginDate");
                return "";
            }
            if (this.getEndDate() != null) {
                endDate_ = format.parse(this.getEndDate());
            } else {
                setErrorMessage("error.notEndDate");
                return "";
            }

            AssociateNewFunctionToPerson.runAssociateNewFunctionToPerson(this.getFunctionID(), this.getPersonID(), credits,
                    YearMonthDay.fromDateFields(beginDate_), YearMonthDay.fromDateFields(endDate_));
            setErrorMessage("message.success");
            return "success";

        } catch (ParseException e) {
            setErrorMessage("error.date1.format");
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }

        return "";
    }

    @Override
    public String getUnits() throws  FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        getUnitTree(buffer, UnitUtils.readInstitutionUnit(), currentDate);
        return buffer.toString();
    }

    protected void getUnitTree(StringBuilder buffer, Unit parentUnit, YearMonthDay currentDate) {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsList(parentUnit, null, buffer, currentDate);
        buffer.append("</ul>");
    }

    protected void getUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer, YearMonthDay currentDate) {

        openLITag(buffer);

        List<Unit> subUnits = new ArrayList<Unit>(getSubUnits(parentUnit, currentDate));
        Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath())
                .append("/manager/functionsManagement/chooseFunction.faces?personID=").append(personID).append("&unitID=")
                .append(parentUnit.getIdInternal()).append("\">").append(parentUnit.getPresentationName()).append("</a>")
                .append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
        }

        for (Unit subUnit : subUnits) {
            getUnitsList(subUnit, parentUnit, buffer, currentDate);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    @Override
    public List<PersonFunction> getActiveFunctions() throws  FenixServiceException {

        if (this.activeFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> activeFunctions = person.getPersonFunctions(null, false, true, null);
            Collections.sort(activeFunctions, new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
            this.activeFunctions = activeFunctions;
        }
        return activeFunctions;
    }

    @Override
    public List<PersonFunction> getInactiveFunctions() throws  FenixServiceException {

        if (this.inactiveFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> inactiveFunctions = person.getPersonFunctions(null, false, false, null);
            Collections.sort(inactiveFunctions, new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
            this.inactiveFunctions = inactiveFunctions;
        }
        return inactiveFunctions;
    }

    @Override
    public List<Function> getInherentFunctions() throws  FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = this.getPerson().getActiveInherentPersonFunctions();
        }
        return inherentFunctions;
    }
}
