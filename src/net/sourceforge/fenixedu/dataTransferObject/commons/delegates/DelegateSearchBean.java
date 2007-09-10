package net.sourceforge.fenixedu.dataTransferObject.commons.delegates;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class DelegateSearchBean implements Serializable {
	private DegreeType degreeType;
		
	private DomainReference<Degree> degree;
	
	private String delegateName;
	
	private Integer studentNumber;
	
	private DomainReference<Person> delegate;
	
	private DomainReference<PersonFunction> delegateFunction;
	
	private FunctionType delegateType;
	
	private DomainReference<CurricularYear> curricularYear;
	
	private DomainReference<ExecutionYear> executionYear;
	
	private DelegateSearchType delegateSearchType;
	
	static final public Comparator YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR = new ComparatorChain();
    static {
	((ComparatorChain) YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR)
		.addComparator(new BeanComparator("executionYear.year"));
	((ComparatorChain) YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR)
		.addComparator(new BeanComparator("curricularYear.year"));
    }
    
    static final public Comparator DELEGATE_COMPARATOR_BY_EXECUTION_YEAR = new BeanComparator("executionYear.year");

	public DelegateSearchBean() {
		setDegreeType(null);
		setDegree(null);
		setCurricularYear(null);
		setExecutionYear(null);
		setDelegateSearchType(DelegateSearchType.ACTIVE_DELEGATES);
	}
	
	public DelegateSearchBean(Person person, PersonFunction delegateFunction) {
		setDelegate(person);
		if(person.hasStudent()) {
			setDegree(person.getStudent().getLastActiveRegistration().getDegree());
			setDegreeType(person.getStudent().getLastActiveRegistration().getDegree().getDegreeType());
		}
		setDelegateType(delegateFunction.getFunction().getFunctionType());
		setDelegateFunction(delegateFunction);
		setCurricularYear(delegateFunction.getCurricularYear());
		setExecutionYear(ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
		setDelegateSearchType(DelegateSearchType.ACTIVE_DELEGATES);
	}
	
	public DelegateSearchBean(Person person, FunctionType functionType, ExecutionYear executionYear) {
		setDelegate(person);
		if(person.hasStudent()) {
			setDegree(person.getStudent().getLastActiveRegistration().getDegree());
			setDegreeType(person.getStudent().getLastActiveRegistration().getDegree().getDegreeType());
		}
		setDelegateType(functionType);
		setExecutionYear(executionYear);
	}
	
	public DelegateSearchBean(Person person, FunctionType functionType, CurricularYear curricularYear, ExecutionYear executionYear) {
		this(person, functionType, executionYear);
		setCurricularYear(curricularYear);
	}

	public Degree getDegree() {
		return (degree == null ? null : degree.getObject());
	}

	public void setDegree(Degree degree) {
		this.degree = new DomainReference<Degree>(degree);
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

	public Integer getStudentNumber() {
		return  studentNumber;
	}

	public void setStudentNumber(Integer studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getDelegateName() {
		return delegateName;
	}

	public void setDelegateName(String delegateName) {
		this.delegateName = delegateName;
	}

	public Person getDelegate() {
		return (delegate == null ? null : delegate.getObject());
	}

	public void setDelegate(Person delegate) {
		this.delegate = new DomainReference<Person>(delegate);
	}

	public FunctionType getDelegateType() {
		return delegateType;
	}


	public void setDelegateType(FunctionType delegateType) {
		this.delegateType = delegateType;
	}
	
	public CurricularYear getCurricularYear() {
		return (curricularYear == null ? null : curricularYear.getObject());
	}

	public void setCurricularYear(CurricularYear curricularYear) {
		this.curricularYear = new DomainReference<CurricularYear>(curricularYear);
	}
	
	public ExecutionYear getExecutionYear() {
		return (executionYear == null ? null : executionYear.getObject());
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public PersonFunction getDelegateFunction() {
		return (delegateFunction == null ? null : delegateFunction.getObject());
	}

	public void setDelegateFunction(PersonFunction delegateFunction) {
		this.delegateFunction = new DomainReference<PersonFunction>(delegateFunction);
	}
	
	public boolean getOnlyActiveDelegates() {
		return getDelegateSearchType().equals(DelegateSearchType.ACTIVE_DELEGATES);
	}
	
	public YearMonthDay getStartDate() {
		return delegateFunction.getObject().getBeginDate();
	}
	
	public YearMonthDay getEndDate() {
		return (delegateFunction.getObject().isActive(new YearMonthDay()) ? null : delegateFunction.getObject().getEndDate());
	}
	
	public String getDelegateFunctionNameIfGgaeDelegate() {
		return (getDelegateType().equals(FunctionType.DELEGATE_OF_GGAE) ? getDelegateFunction().getFunction().getName() : null);
	}
	
	public static enum DelegateSearchType {
		ACTIVE_DELEGATES,
		ALL_DELEGATES;
	}

	public DelegateSearchType getDelegateSearchType() {
		return delegateSearchType;
	}

	public void setDelegateSearchType(DelegateSearchType delegateSearchType) {
		this.delegateSearchType = delegateSearchType;
	}
	
	public String getDegreeName() {
		return (getDegree() != null ? getDegree().getName() : null);
	}
}