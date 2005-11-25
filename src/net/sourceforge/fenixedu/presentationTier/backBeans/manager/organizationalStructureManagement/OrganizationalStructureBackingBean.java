/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.organizationalStructureManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.IFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    public String unitName, unitCostCenter, unitTypeName, unitBeginDate, unitEndDate;

    public String functionName, functionTypeName, functionBeginDate, functionEndDate;

    public Integer principalFunctionID;

    public IUnit unit, chooseUnit;

    public IFunction function;

    public HtmlInputHidden unitIDHidden, chooseUnitIDHidden, functionIDHidden;

    
    public OrganizationalStructureBackingBean() {
        if (getRequestParameter("unitID") != null
                && !getRequestParameter("unitID").toString().equals("")) {
            getUnitIDHidden().setValue(Integer.valueOf(getRequestParameter("unitID").toString()));
        }
        if (getRequestParameter("chooseUnitID") != null
                && !getRequestParameter("chooseUnitID").toString().equals("")) {
            getChooseUnitIDHidden().setValue(
                    Integer.valueOf(getRequestParameter("chooseUnitID").toString()));
        }
        if (getRequestParameter("functionID") != null
                && !getRequestParameter("functionID").toString().equals("")) {
            getFunctionIDHidden()
                    .setValue(Integer.valueOf(getRequestParameter("functionID").toString()));
        }
        if (getRequestParameter("principalFunctionID") != null
                && !getRequestParameter("principalFunctionID").toString().equals("")) {
            this.principalFunctionID = Integer.valueOf(getRequestParameter("principalFunctionID")
                    .toString());
        }
    }

    public List<IFunction> getAllNonInherentFunctions() throws FenixFilterException,
            FenixServiceException {

        List<IFunction> allNonInherentFunctions = new ArrayList<IFunction>();
        for (IFunction function : this.getUnit().getFunctions()) {
            if (!function.isInherentFunction()) {
                allNonInherentFunctions.add(function);
            }
        }
        return allNonInherentFunctions;
    }

    public List<IFunction> getAllInherentFunctions() throws FenixFilterException, FenixServiceException {

        List<IFunction> allInherentFunctions = new ArrayList<IFunction>();
        for (IFunction function : this.getUnit().getFunctions()) {
            if (function.isInherentFunction()) {
                allInherentFunctions.add(function);
            }
        }
        return allInherentFunctions;
    }

    public List<IUnit> getAllUnitsWithoutParent() throws FenixFilterException, FenixServiceException {
        List<IUnit> allUnitsWithoutParent = new ArrayList<IUnit>();
        for (IUnit unit : readAllUnits()) {
            if (unit.getParentUnit() == null) {
                allUnitsWithoutParent.add(unit);
            }
        }
        return allUnitsWithoutParent;
    }

    private List<IUnit> readAllUnits() throws FenixServiceException, FenixFilterException {
        final Object[] argsToRead = { Unit.class };

        List<IUnit> allUnits = new ArrayList<IUnit>();
        allUnits.addAll((List<IUnit>) ServiceUtils.executeService(null, "ReadAllDomainObject",
                argsToRead));
        return allUnits;
    }

    public String getAllUnitsToChooseParentUnit() throws FenixFilterException, FenixServiceException {
        StringBuffer buffer = new StringBuffer();
        List<IUnit> allUnitsWithoutParent = getAllUnitsWithoutParent();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));
        for (IUnit unit : allUnitsWithoutParent) {
            if (!unit.equals(this.getUnit())) {
                getUnitTreeToChooseParentUnit(buffer, unit);
            }
        }
        return buffer.toString();
    }

    public void getUnitTreeToChooseParentUnit(StringBuffer buffer, IUnit parentUnit)
            throws FenixFilterException, FenixServiceException {
        buffer.append("<ul>");
        getUnitsListToChooseParentUnit(parentUnit, 0, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsListToChooseParentUnit(IUnit parentUnit, int index, StringBuffer buffer)
            throws FenixFilterException, FenixServiceException {

        buffer.append("<li>").append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("chooseParentUnit.faces?").append(
                "unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
                parentUnit.getIdInternal()).append("\">").append(parentUnit.getName()).append("</a>")
                .append("</li>").append("<ul>");

        for (IUnit subUnit : parentUnit.getAssociatedUnits()) {
            getUnitsListToChooseParentUnit(subUnit, index + 1, buffer);
        }

        buffer.append("</ul>");
    }

    public String getUnits() throws FenixFilterException, FenixServiceException {
        StringBuffer buffer = new StringBuffer();
        List<IUnit> allUnitsWithoutParent = getAllUnitsWithoutParent();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));
        for (IUnit unit : allUnitsWithoutParent) {
            getUnitTree(buffer, unit);
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
                "/manager/organizationalStructureManagament/").append("unitDetails.faces?").append(
                "unitID=").append(parentUnit.getIdInternal()).append("\">").append(parentUnit.getName())
                .append("</a>").append("</li>").append("<ul>");

        for (IUnit subUnit : parentUnit.getAssociatedUnits()) {
            getUnitsList(subUnit, index + 1, buffer);
        }

        buffer.append("</ul>");
    }

    public String getUnitsToChoosePrincipalFunction() throws FenixFilterException, FenixServiceException {
        StringBuffer buffer = new StringBuffer();

        List<IUnit> allUnitsWithoutParent = getAllUnitsWithoutParent();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));
        for (IUnit unit : allUnitsWithoutParent) {
            if (!unit.equals(this.getUnit())) {
                getUnitTreeToChoosePrincipalFunction(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTreeToChoosePrincipalFunction(StringBuffer buffer, IUnit parentUnit)
            throws FenixFilterException, FenixServiceException {
        buffer.append("<ul>");
        getUnitsListToChoosePrincipalFunction(parentUnit, 0, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsListToChoosePrincipalFunction(IUnit parentUnit, int index, StringBuffer buffer)
            throws FenixFilterException, FenixServiceException {

        buffer.append("<li>").append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("chooseFunction.faces?").append(
                "unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
                parentUnit.getIdInternal()).append("&functionID=").append(
                this.getFunction().getIdInternal()).append("\">").append(parentUnit.getName()).append(
                "</a>").append("</li>").append("<ul>");

        for (IUnit subUnit : parentUnit.getAssociatedUnits()) {
            getUnitsListToChoosePrincipalFunction(subUnit, index + 1, buffer);
        }

        buffer.append("</ul>");
    }

    public List<SelectItem> getValidUnitType() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        ResourceBundle bundle = getResourceBundle("ServidorApresentacao/EnumerationResources");       
        
        SelectItem selectItem = null;
        for (UnitType type : UnitType.values()) {
            selectItem = new SelectItem();
            selectItem.setLabel(bundle.getString(type.getName()));
            selectItem.setValue(type.getName());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));
        
        addDefaultSelectedItem(list, bundle);
        
        return list;
    }

    public List<SelectItem> getValidFunctionType() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        ResourceBundle bundle = getResourceBundle("ServidorApresentacao/EnumerationResources");

        SelectItem selectItem = null;
        for (FunctionType type : FunctionType.values()) {
            selectItem = new SelectItem();
            selectItem.setLabel(bundle.getString(type.getName()));
            selectItem.setValue(type.getName());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));
        
        addDefaultSelectedItem(list, bundle);
        
        return list;
    }

    private void addDefaultSelectedItem(List<SelectItem> list, ResourceBundle bundle) {
        SelectItem firstItem = new SelectItem();
        firstItem.setLabel(bundle.getString("dropDown.Default"));
        firstItem.setValue("#");        
        list.add(0, firstItem);
    }
    
    public String createTopUnit() throws FenixFilterException, FenixServiceException {

        if (verifyCostCenterCode()) {
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        UnitType type = getUnitType();
        
        final Object[] argsToRead = { null, null, this.getUnitName(), this.getUnitCostCenter(),
                datesResult.getBeginDate(), datesResult.getEndDate(),
                type};

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewUnit", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "listAllUnits";
    }

    public String createSubUnit() throws FenixFilterException, FenixServiceException {

        if (verifyCostCenterCode()) {
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        UnitType type = getUnitType();
        
        final Object[] argsToRead = { null, this.getUnit().getIdInternal(), this.getUnitName(),
                this.getUnitCostCenter(), datesResult.getBeginDate(), datesResult.getEndDate(),
                type};

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewUnit", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }
    
    public String editUnit() throws FenixFilterException, FenixServiceException {

        if (verifyCostCenterCode()) {
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        UnitType type = getUnitType();
        
        final Object[] argsToRead = { this.getChooseUnit().getIdInternal(),
                this.getChooseUnit().getParentUnit().getIdInternal(), this.getUnitName(),
                this.getUnitCostCenter(), datesResult.getBeginDate(), datesResult.getEndDate(),
                type };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewUnit", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    private UnitType getUnitType() throws FenixFilterException, FenixServiceException {
        UnitType type = null;
        if(!this.getUnitTypeName().equals("#")){
            UnitType.valueOf(this.getUnitTypeName());
        }
        return type;
    }
    
    public String associateParentUnit() throws FenixFilterException, FenixServiceException {
        
        String costCenterCodeString = getValidCosteCenterCode();
        
        final Object[] argsToRead = { this.getUnit().getIdInternal(),
                this.getChooseUnit().getIdInternal(), this.getUnit().getName(),
                costCenterCodeString, this.getUnit().getBeginDate(),
                this.getUnit().getEndDate(), this.getUnit().getType() };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewUnit", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String disassociateParentUnit() throws FenixFilterException, FenixServiceException {

        String costCenterCodeString = getValidCosteCenterCode();
        
        final Object[] argsToRead = { this.getUnit().getIdInternal(), null, this.getUnit().getName(),
                costCenterCodeString, this.getUnit().getBeginDate(),
                this.getUnit().getEndDate(), this.getUnit().getType() };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewUnit", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "";
    }

    private String getValidCosteCenterCode() throws FenixFilterException, FenixServiceException {
        Integer costCenterCode = this.getUnit().getCostCenterCode();
        String costCenterCodeString = null;
        if(costCenterCode != null){
            costCenterCodeString = costCenterCodeString.toString();
        }
        return costCenterCodeString;
    }

    private FunctionType getFunctionType() throws FenixFilterException, FenixServiceException {
        FunctionType type = null;
        if(!this.getFunctionTypeName().equals("#")){
            FunctionType.valueOf(this.getFunctionTypeName());
        }
        return type;
    }
    
    public String createFunction() throws FenixFilterException, FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this
                .getFunctionEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        FunctionType type = getFunctionType();
        
        final Object[] argsToRead = { null, this.getUnit().getIdInternal(), this.getFunctionName(),
                datesResult.getBeginDate(), datesResult.getEndDate(),
                type, null };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewFunction", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String editFunction() throws FenixFilterException, FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this
                .getFunctionEndDate());

        if (datesResult.isTest()) {
            return "";
        }

        IFunction parentInherentFunction = this.getFunction().getParentInherentFunction();
        Integer parentInherentFunctionID = null;
        if (parentInherentFunction != null) {
            parentInherentFunctionID = parentInherentFunction.getIdInternal();
        }

        FunctionType type = getFunctionType();
        
        final Object[] argsToRead = { this.getFunction().getIdInternal(),
                this.getFunction().getUnit().getIdInternal(), this.getFunctionName(),
                datesResult.getBeginDate(), datesResult.getEndDate(),
                type, parentInherentFunctionID };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewFunction", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String prepareAssociateInherentParentFunction() throws FenixFilterException,
            FenixServiceException {

        IFunction function = this.getFunction();
        if (!function.getPersonFunctions().isEmpty()) {
            setErrorMessage("error.becomeInherent");
            return "";
        }
        return "chooseInherentParentFunction";
    }

    public String associateInherentParentFunction() throws FenixFilterException, FenixServiceException {

        IFunction function = this.getFunction();

        final Object[] argsToRead = { function.getIdInternal(), function.getUnit().getIdInternal(),
                function.getName(), function.getBeginDate(), function.getEndDate(), function.getType(),
                this.principalFunctionID };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewFunction", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String disassociateInherentFunction() throws FenixFilterException, FenixServiceException {

        IFunction function = this.getFunction();

        final Object[] argsToRead = { function.getIdInternal(), function.getUnit().getIdInternal(),
                function.getName(), function.getBeginDate(), function.getEndDate(), function.getType(),
                null };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewFunction", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "";
    }

    public String deleteSubUnit() throws FenixFilterException {
        final Object[] argsToRead = { Integer
                .valueOf(this.getChooseUnitIDHidden().getValue().toString()) };
        try {
            ServiceUtils.executeService(getUserView(), "DeleteUnit", argsToRead);

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
        }
        return "";
    }

    public String deleteUnit() throws FenixFilterException {
        final Object[] argsToRead = { Integer
                .valueOf(this.getChooseUnitIDHidden().getValue().toString()) };
        try {
            ServiceUtils.executeService(getUserView(), "DeleteUnit", argsToRead);

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
            return "";
        }
        return "listAllUnits";
    }

    public String deleteFunction() throws FenixFilterException {
        final Object[] argsToRead = { Integer.valueOf(this.getFunctionIDHidden().getValue().toString()) };
        try {
            ServiceUtils.executeService(getUserView(), "DeleteFunction", argsToRead);

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
        }
        return "";
    }

    private PrepareDatesResult prepareDates(String beginDate, String endDate)
            throws FenixFilterException, FenixServiceException {
        boolean test = false;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date beginDate_ = null, endDate_ = null;
        try {
            beginDate_ = format.parse(beginDate);
            if (endDate != null && !endDate.equals("")) {
                endDate_ = format.parse(endDate);
            }
        } catch (ParseException exception) {
            setErrorMessage("error.date.format");
            test = true;
        }

        return new PrepareDatesResult(test, beginDate_, endDate_);
    }

    public String getUnitCostCenter() throws FenixFilterException, FenixServiceException {
        if (this.unitCostCenter == null && this.getChooseUnit() != null
                && this.getChooseUnit().getCostCenterCode() != null) {
            this.unitCostCenter = this.getChooseUnit().getCostCenterCode().toString();
        }
        return unitCostCenter;
    }

    public void setUnitCostCenter(String costCenter) {
        this.unitCostCenter = costCenter;
    }

    public String getUnitBeginDate() throws FenixFilterException, FenixServiceException {
        if (this.unitBeginDate == null && this.getChooseUnit() != null) {
            this.unitBeginDate = processDate(this.getChooseUnit().getBeginDate());
        }
        return unitBeginDate;
    }

    public void setUnitBeginDate(String unitBeginDate) {
        this.unitBeginDate = unitBeginDate;
    }

    public String getUnitEndDate() throws FenixFilterException, FenixServiceException {
        if (this.unitEndDate == null && this.getChooseUnit() != null
                && this.getChooseUnit().getEndDate() != null) {
            this.unitEndDate = processDate(this.getChooseUnit().getEndDate());
        }
        return unitEndDate;
    }

    public void setUnitEndDate(String unitEndDate) {
        this.unitEndDate = unitEndDate;
    }

    public String getUnitName() throws FenixFilterException, FenixServiceException {
        if (this.unitName == null && this.getChooseUnit() != null) {
            this.unitName = this.getChooseUnit().getName();
        }
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitTypeName() throws FenixFilterException, FenixServiceException {
        if (this.unitTypeName == null && this.getChooseUnit() != null
                && this.getChooseUnit().getType() != null) {
            this.unitTypeName = this.getChooseUnit().getType().getName();
        }
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public IUnit getUnit() throws FenixFilterException, FenixServiceException {
        if (this.unit == null && this.getUnitIDHidden() != null
                && this.getUnitIDHidden().getValue() != null
                && !this.getUnitIDHidden().getValue().equals("")) {

            this.unit = (IUnit) readDomainObject(Unit.class, Integer.valueOf(this.getUnitIDHidden()
                    .getValue().toString()));
        }
        return unit;
    }

    public void setUnit(IUnit unit) {
        this.unit = unit;
    }

    private String processDate(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day + "/" + month + "/" + year);
    }

    private Calendar prepareCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public HtmlInputHidden getUnitIDHidden() {
        if (this.unitIDHidden == null) {
            this.unitIDHidden = new HtmlInputHidden();
        }
        return unitIDHidden;
    }

    public void setUnitIDHidden(HtmlInputHidden unitIDHidden) {
        this.unitIDHidden = unitIDHidden;
    }

    public IFunction getFunction() throws FenixFilterException, FenixServiceException {
        if (this.function == null && this.getFunctionIDHidden() != null
                && this.getFunctionIDHidden().getValue() != null
                && !this.getFunctionIDHidden().getValue().equals("")) {

            this.function = (IFunction) readDomainObject(Function.class, Integer.valueOf(this
                    .getFunctionIDHidden().getValue().toString()));
        }
        return function;
    }

    public void setFunction(IFunction function) {
        this.function = function;
    }

    public String getFunctionBeginDate() throws FenixFilterException, FenixServiceException {
        if (this.functionBeginDate == null && this.getFunction() != null
                && this.getFunction().getBeginDate() != null) {
            this.functionBeginDate = processDate(this.getFunction().getBeginDate());
        }
        return functionBeginDate;
    }

    public void setFunctionBeginDate(String functionBeginDate) {
        this.functionBeginDate = functionBeginDate;
    }

    public String getFunctionEndDate() throws FenixFilterException, FenixServiceException {
        if (this.functionEndDate == null && this.getFunction() != null
                && this.getFunction().getEndDate() != null) {
            this.functionEndDate = processDate(this.getFunction().getEndDate());
        }
        return functionEndDate;
    }

    public void setFunctionEndDate(String functionEndDate) {
        this.functionEndDate = functionEndDate;
    }

    public String getFunctionName() throws FenixFilterException, FenixServiceException {
        if (this.functionName == null && this.getFunction() != null) {
            this.functionName = this.getFunction().getName();
        }
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionTypeName() throws FenixFilterException, FenixServiceException {
        if (this.functionTypeName == null && this.getFunction() != null
                && this.getFunction().getType() != null) {
            this.functionTypeName = this.getFunction().getType().getName();
        }
        return functionTypeName;
    }

    public void setFunctionTypeName(String functionTypeName) {
        this.functionTypeName = functionTypeName;
    }

    public IUnit getChooseUnit() throws FenixFilterException, FenixServiceException {
        if (this.chooseUnit == null && this.getChooseUnitIDHidden() != null
                && this.getChooseUnitIDHidden().getValue() != null
                && !this.getChooseUnitIDHidden().getValue().equals("")) {

            this.chooseUnit = (IUnit) readDomainObject(Unit.class, Integer.valueOf(this
                    .getChooseUnitIDHidden().getValue().toString()));
        }

        return chooseUnit;
    }

    public void setChooseUnit(IUnit chooseUnit) {
        this.chooseUnit = chooseUnit;
    }

    public HtmlInputHidden getChooseUnitIDHidden() {
        if (this.chooseUnitIDHidden == null) {
            this.chooseUnitIDHidden = new HtmlInputHidden();
        }
        return chooseUnitIDHidden;
    }

    public void setChooseUnitIDHidden(HtmlInputHidden chooseUnitIDHidden) {
        this.chooseUnitIDHidden = chooseUnitIDHidden;
    }

    private boolean verifyCostCenterCode() throws FenixFilterException, FenixServiceException {
        List<IUnit> allUnits = readAllUnits();
        for (IUnit unit : allUnits) {
            if (unit.getCostCenterCode() != null && !unit.equals(this.getChooseUnit())
                    && this.getUnitCostCenter().equals(String.valueOf(unit.getCostCenterCode()))
                    && unit.isActive(prepareCalendar().getTime())) {
                setErrorMessage("error.costCenter.alreadyExists");
                return true;
            }
        }
        return false;
    }

    public HtmlInputHidden getFunctionIDHidden() {
        if (this.functionIDHidden == null) {
            this.functionIDHidden = new HtmlInputHidden();
        }
        return functionIDHidden;
    }

    public void setFunctionIDHidden(HtmlInputHidden functionIDHidden) {
        this.functionIDHidden = functionIDHidden;
    }

    public Integer getPrincipalFunctionID() {
        return principalFunctionID;
    }

    public void setPrincipalFunctionID(Integer principalFunctionID) {
        this.principalFunctionID = principalFunctionID;
    }

    public class PrepareDatesResult {

        private boolean test;

        private Date beginDate, endDate;

        public PrepareDatesResult(boolean test, Date beginDate, Date endDate) {
            this.test = test;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        public Date getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(Date beginDate) {
            this.beginDate = beginDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public boolean isTest() {
            return test;
        }

        public void setTest(boolean test) {
            this.test = test;
        }
    }
}
