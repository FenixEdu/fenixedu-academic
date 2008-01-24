package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class DegreeUnit extends DegreeUnit_Base {
    
    private DegreeUnit() {
        super();
        super.setType(PartyTypeEnum.DEGREE_UNIT);
    }
        
    public static DegreeUnit createNewInternalDegreeUnit(MultiLanguageString unitName, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType, 
	    String webAddress, Degree degree, UnitClassification classification, Boolean canBeResponsibleOfSpaces, 
	    Campus campus) {
	
	DegreeUnit degreeUnit = new DegreeUnit();	
	degreeUnit.init(unitName, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces, campus);
	degreeUnit.setDegree(degree);		
	degreeUnit.addParentUnit(parentUnit, accountabilityType);
	
	checkIfAlreadyExistsOneDegreeWithSameAcronym(degreeUnit);
	
	return degreeUnit;
    }
    
    @Override
    public void edit(MultiLanguageString unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification, 
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Campus campus) {
        
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces, campus);
        setDegree(degree);
        
        checkIfAlreadyExistsOneDegreeWithSameAcronym(this);        
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && !parentUnit.isInternal()) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}	
	return super.addParentUnit(parentUnit, accountabilityType);
    }
     
    @Override
    public void setAcronym(String acronym) {
        if(StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
	super.setAcronym(acronym);
    }
     
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    public boolean isDegreeUnit() {
        return true;
    }
    
    @Override
    public void setDegree(Degree degree) {
        if(degree == null) {
            throw new DomainException("error.DegreeUnit.empty.degree");
        }
	super.setDegree(degree);
    }
    
    @Override
    public void delete() {
	super.setDegree(null);
        super.delete();        
    }
    
    private static void checkIfAlreadyExistsOneDegreeWithSameAcronym(DegreeUnit degreeUnit) {
	for (Unit unit: UnitUtils.readInstitutionUnit().getAllSubUnits()) {
	    if(!unit.equals(degreeUnit) && unit.isDegreeUnit() && degreeUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {		
		throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");		
	    }
	}		
    }
    
    
    /*
     * METHODS FOR DELEGATE FUNCTIONS
     */
    
    /* For each delegate function type, there can be only one active function */
    public Function getActiveDelegateFunctionByType(FunctionType functionType) {
    	YearMonthDay today = new YearMonthDay();
    	for (Function function : getFunctions()) {
    		if (function.getFunctionType().equals(functionType) && function.isActive(today)) {
    			return function;
}
    	}
    	return null;
    }
    /*
     * ACTIVE DELEGATE PERSON FUNCTIONS
     */
    public List<PersonFunction> getAllActiveDelegatePersonFunctions() {
    	List<PersonFunction> result = new ArrayList<PersonFunction>();
    	for(FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
    		result.addAll(getAllActiveDelegatePersonFunctionsByFunctionType(functionType));
    	}
    	return result;
    }
    
    public List<PersonFunction> getAllActiveDelegatePersonFunctionsByFunctionType(FunctionType functionType) {
    	final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
    	List<PersonFunction> result = new ArrayList<PersonFunction>();
    	final Function function = getActiveDelegateFunctionByType(functionType);
    	if(function != null) {
    		return function.getActivePersonFunctionsStartingIn(executionYear);
    	}
    	return result;
    }
    
    
    public PersonFunction getActiveYearDelegatePersonFunctionByCurricularYear(CurricularYear curricularYear) {
    	final List<PersonFunction> delegateFunctions = getAllActiveDelegatePersonFunctionsByFunctionType(FunctionType.DELEGATE_OF_YEAR);
    	for (PersonFunction delegateFunction : delegateFunctions) {
    		if(delegateFunction.hasCurricularYear() && delegateFunction.getCurricularYear().equals(curricularYear)) {
    			return delegateFunction;
    		}
    	}
    	return null;
    }
    
    /*
     * DELEGATE PERSON FUNCTIONS FROM GIVEN EXECUTION YEAR (PAST DELEGATES)
     */
    public List<PersonFunction> getAllDelegatePersonFunctionsByExecutionYear(ExecutionYear executionYear) {
    	List<PersonFunction> result = new ArrayList<PersonFunction>();
    	for(FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
    		result.addAll(getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(executionYear, functionType));
    	}
    	return result;
    }
    
    public List<PersonFunction> getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(ExecutionYear executionYear, 
    		FunctionType functionType) {
    	List<PersonFunction> result = new ArrayList<PersonFunction>();
    	final Function function = getDelegateFunctionByTypeAndExecutionYear(executionYear, functionType);
    	if(function != null) {
    		return function.getPersonFunctions();
    	}
    	return result;
    }
    
    public PersonFunction getYearDelegatePersonFunctionByExecutionYearAndCurricularYear(ExecutionYear executionYear,
    		CurricularYear curricularYear) {
    	final List<PersonFunction> delegateFunctions = getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(executionYear, 
    			FunctionType.DELEGATE_OF_YEAR);
    	for (PersonFunction delegateFunction : delegateFunctions) {
    		if(delegateFunction.getCurricularYear().equals(curricularYear) &&
    				delegateFunction.belongsToPeriod(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay())) {
    			return delegateFunction;
    		}
    	}
    	return null;
    }
    
    /* Return delegate function, of the given type, in the given execution year */
    public Function getDelegateFunctionByTypeAndExecutionYear(ExecutionYear executionYear, FunctionType functionType) {
    	for (Function function : getFunctions()) {
    		if (function.getFunctionType().equals(functionType) && function.belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
    				executionYear.getEndDateYearMonthDay())) {
    			return function;
    		}
    	}
    	return null;
    }
    
    public List<Function> getAllDelegateFunctionsByType(FunctionType functionType) {
    	List<Function> result = new ArrayList<Function>();
    	for (Function function : getFunctions()) {
    		if (function.getFunctionType().equals(functionType)) {
    			result.add(function);
    		}
    	}
    	return result;
    }
    
    public List<PersonFunction> getAllDelegatePersonFunctionsByFunctionType(FunctionType functionType) {
    	List<PersonFunction> result = new ArrayList<PersonFunction>();
    	for(Function function : getAllDelegateFunctionsByType(functionType)) {
	    	result.addAll(function.getPersonFunctions());
    	}
    	return result;
    }
    
    
    //TODO: controlo de acesso?
    public void addYearDelegatePersonFunction(Student student, CurricularYear curricularYear) {
    	
    	if(student.getLastActiveRegistration() != null && !student.getLastActiveRegistration().getDegree().equals(getDegree())) {
    		throw new DomainException("error.delegates.studentNotBelongsToDegree");
    	}
    	
    	YearMonthDay currentDate = new YearMonthDay();
    	
    	//The following restriction tries to guarantee that a new delegate is elected before this person function ends 
    	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
    	YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1); 
    	
    	Function function = getActiveDelegateFunctionByType(FunctionType.DELEGATE_OF_YEAR);
    	
    	/* Check if there is another active person function with this type */
    	if(function != null) {
	    	PersonFunction delegateFunction = getActiveYearDelegatePersonFunctionByCurricularYear(curricularYear);
	    	if(delegateFunction != null) {
	    		delegateFunction.setOccupationInterval(delegateFunction.getBeginDate(), currentDate.minusDays(1));
	    	}
    	}
    	
    	PersonFunction.createYearDelegatePersonFunction(this, student.getPerson(), currentDate , endDate, function, curricularYear);
    }
    
    //TODO: controlo de acesso?
    public void addDelegatePersonFunction(Student student, FunctionType functionType) {
    	
    	if(!student.getLastActiveRegistration().getDegree().equals(getDegree())) {
    		throw new DomainException("error.delegates.studentNotBelongsToDegree");
    	}
    	
    	YearMonthDay currentDate = new YearMonthDay();
    	
    	//The following restriction tries to guarantee that a new delegate is elected before this person function ends 
    	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
    	YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);
    	
    	Function function = getActiveDelegateFunctionByType(functionType);
    	
    	/* Check if there is another active person function with this type */
    	if(function != null) {
	    	List<PersonFunction> delegateFunctions = function.getActivePersonFunctions();
	    	for (PersonFunction personFunction : delegateFunctions) {
	    		personFunction.setOccupationInterval(personFunction.getBeginDate(), currentDate.minusDays(1)); //if consistent, there can be only one
			}
    	}
    	
    	PersonFunction.createDelegatePersonFunction(this, student.getPerson(), currentDate , endDate, function);
    }
    
    //TODO: controlo de acesso?
    public void removeAllActiveDelegatePersonFunctionsFromStudent(Student student) {
    	for(FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
    		removeActiveDelegatePersonFunctionFromStudentByFunctionType(student, functionType);
		}
    }
    
    //TODO: controlo de acesso?
    public void removeActiveDelegatePersonFunctionFromStudentByFunctionType(Student student, FunctionType functionType) {
    	List<PersonFunction> delegatesFunctions = getAllActiveDelegatePersonFunctionsByFunctionType(functionType);
    	if(!delegatesFunctions.isEmpty()) {
    		for(PersonFunction function : delegatesFunctions) {
				Student delegateStudent = function.getPerson().getStudent();
				if(delegateStudent.equals(student)) {
					function.delete();
				}
			}
    	}
	}
}
