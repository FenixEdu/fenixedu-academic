package Dominio;

import Util.AgreementType;
import Util.StudentState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class Student extends DomainObject implements IStudent {

	protected Integer number;
	protected StudentState state;
	protected TipoCurso degreeType;
	private IStudentKind studentKind;
    private AgreementType agreementType;
	
	private Integer personKey;
	private Integer studentKindKey;
	private IPessoa person;
	
	
	public Student(Integer idInternal){
		setIdInternal(idInternal);
	}
	
	public Student() {
		setNumber(null);
		setState(null);
		setPerson(null);
		setDegreeType(null);
		
		setPersonKey(null);
		setStudentKind(null);
		setStudentKindKey(null);
	}

	public Student(Integer number, StudentState state, IPessoa person, TipoCurso degreeType) {
		this();
		setNumber(number);
		setState(state);
		setPerson(person);
		setDegreeType(degreeType);
		
		setPersonKey(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IStudent) {
			IStudent student = (IStudent) obj;
			
			resultado = (student != null) &&
						((this.getNumber().equals(student.getNumber()) && this.getDegreeType().equals(student.getDegreeType()) )
						||
						(this.getDegreeType().equals(student.getDegreeType()) && this.getPerson().equals(student.getPerson())));
		}
		return resultado;
//		return true;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "internalCode = " + getIdInternal() + "; ";
		result += "number = " + this.number + "; ";
		result += "state = " + this.state + "; ";
		result += "degreeType = " + this.degreeType + "; ";
		result += "studentKind = " + this.studentKind + "; ";
		//result += "person = " + this.person + "]";
		return result;
	}

	/**
	 * Returns the degreeType.
	 * @return TipoCurso
	 */
	public TipoCurso getDegreeType() {
		return degreeType;
	}

	

	/**
	 * Returns the number.
	 * @return Integer
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * Returns the person.
	 * @return IPessoa
	 */
	public IPessoa getPerson() {
		return person;
	}

	/**
	 * Returns the personKey.
	 * @return Integer
	 */
	public Integer getPersonKey() {
		return personKey;
	}

	/**
	 * Returns the state.
	 * @return StudentState
	 */
	public StudentState getState() {
		return state;
	}

	/**
	 * Sets the degreeType.
	 * @param degreeType The degreeType to set
	 */
	public void setDegreeType(TipoCurso degreeType) {
		this.degreeType = degreeType;
	}

	

	/**
	 * Sets the number.
	 * @param number The number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * Sets the person.
	 * @param person The person to set
	 */
	public void setPerson(IPessoa person) {
		this.person = person;
	}

	/**
	 * Sets the personKey.
	 * @param personKey The personKey to set
	 */
	public void setPersonKey(Integer personKey) {
		this.personKey = personKey;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(StudentState state) {
		this.state = state;
	}
	/**
	 * @return
	 */
	public IStudentKind getStudentKind() {
		return studentKind;
	}

	/**
	 * @return
	 */
	public Integer getStudentKindKey() {
		return studentKindKey;
	}

	/**
	 * @param type
	 */
	public void setStudentKind(IStudentKind studentKind) {
		this.studentKind = studentKind;
	}

	/**
	 * @param integer
	 */
	public void setStudentKindKey(Integer studentKindKey) {
		this.studentKindKey = studentKindKey;
	}

	

    /**
     * @return Returns the agreementType.
     */
    public AgreementType getAgreementType()
    {
        return agreementType;
    }
    /**
     * @param agreementType The agreementType to set.
     */
    public void setAgreementType(AgreementType agreementType)
    {
        this.agreementType = agreementType;
    }
}
