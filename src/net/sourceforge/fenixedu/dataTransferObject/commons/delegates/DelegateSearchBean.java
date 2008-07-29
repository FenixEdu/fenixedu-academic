package net.sourceforge.fenixedu.dataTransferObject.commons.delegates;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Registration;

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

    static final public Comparator<DelegateSearchBean> YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR = new ComparatorChain();
    static {
	((ComparatorChain) YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR).addComparator(new BeanComparator(
		"executionYear.year"));
	((ComparatorChain) YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR).addComparator(new BeanComparator(
		"curricularYear.year"));
    }

    static final public Comparator<DelegateSearchBean> DELEGATE_COMPARATOR_BY_EXECUTION_YEAR = new BeanComparator(
	    "executionYear.year");

    public DelegateSearchBean() {
	setDegreeType(null);
	setDegree(null);
	setCurricularYear(null);
	setExecutionYear(null);
	setDelegate(null);
	setDelegateFunction(null);
	setDelegateSearchType(DelegateSearchType.ACTIVE_DELEGATES);
    }

    public DelegateSearchBean(Person person, PersonFunction delegateFunction) {
	this();
	setDelegate(person);
	if (person.hasStudent()) {
	    Registration registration = person.getStudent().getLastActiveRegistration();
	    if (registration != null) {
		setDegree(registration.getDegree());
		setDegreeType(registration.getDegree().getDegreeType());
	    }
	}
	setDelegateType(delegateFunction.getFunction().getFunctionType());
	setDelegateFunction(delegateFunction);
	setCurricularYear(delegateFunction.getCurricularYear());
	setExecutionYear(ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
	setDelegateSearchType(DelegateSearchType.ACTIVE_DELEGATES);
    }

    public DelegateSearchBean(Person person, FunctionType functionType, ExecutionYear executionYear) {
	this();
	setDelegate(person);
	Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
	if (person.hasStudent()) {
	    setDegree(lastActiveRegistration != null ? lastActiveRegistration.getDegree() : null);
	    setDegreeType(lastActiveRegistration != null ? lastActiveRegistration.getDegree().getDegreeType() : null);
	}
	setDelegateType(functionType);
	setExecutionYear(executionYear);
    }

    public DelegateSearchBean(Person person, FunctionType functionType, CurricularYear curricularYear, ExecutionYear executionYear) {
	this(person, functionType, executionYear);
	setCurricularYear(curricularYear);
    }

    public Degree getDegree() {
	return degree.getObject();
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
	return studentNumber;
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
	return delegate.getObject();
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
	return curricularYear.getObject();
    }

    public void setCurricularYear(CurricularYear curricularYear) {
	this.curricularYear = new DomainReference<CurricularYear>(curricularYear);
    }

    public ExecutionYear getExecutionYear() {
	return executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public PersonFunction getDelegateFunction() {
	return delegateFunction.getObject();
    }

    public void setDelegateFunction(PersonFunction delegateFunction) {
	this.delegateFunction = new DomainReference<PersonFunction>(delegateFunction);
    }

    public boolean getOnlyActiveDelegates() {
	return getDelegateSearchType().equals(DelegateSearchType.ACTIVE_DELEGATES);
    }

    public YearMonthDay getStartDate() {
	return getDelegateFunction().getBeginDate();
    }

    public YearMonthDay getEndDate() {
	PersonFunction delegateFunction = getDelegateFunction();
	return delegateFunction.isActive(new YearMonthDay()) ? null : delegateFunction.getEndDate();
    }

    public String getDelegateFunctionNameIfGgaeDelegate() {
	return (getDelegateType().equals(FunctionType.DELEGATE_OF_GGAE) ? getDelegateFunction().getFunction().getName() : null);
    }

    public static enum DelegateSearchType {
	ACTIVE_DELEGATES, ALL_DELEGATES;
    }

    public DelegateSearchType getDelegateSearchType() {
	return delegateSearchType;
    }

    public void setDelegateSearchType(DelegateSearchType delegateSearchType) {
	this.delegateSearchType = delegateSearchType;
    }

    public String getDegreeName() {
	return (getDegree() != null ? getDegree().getNameFor(getExecutionYear()).getContent() : null);
    }
}