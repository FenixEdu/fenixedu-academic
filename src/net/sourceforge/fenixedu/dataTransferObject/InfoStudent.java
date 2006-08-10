/*
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StudentState;

/**
 * @author tfc130
 */

public class InfoStudent extends InfoObject {

    protected Integer number;

    protected StudentState state = new StudentState(1);

    private InfoPerson infoPerson;

    protected DegreeType degreeType;

    private InfoStudentKind infoStudentKind;

    private Boolean payedTuition;
    
    private Boolean flunked;
    
    private Boolean requestedChangeDegree;
    
    private Boolean interruptedStudies;
    
    private InfoStudentCurricularPlan infoStudentCurricularPlan;


    public InfoStudent() {
    }

    public InfoStudent(Integer numero, StudentState estado, InfoPerson pessoa, DegreeType degreeType) {
        setNumber(numero);
        setState(estado);
        setInfoPerson(pessoa);
        setDegreeType(degreeType);
    }

    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    public void setInfoPerson(InfoPerson pessoa) {
        infoPerson = pessoa;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer numero) {
        number = numero;
    }

    public StudentState getState() {
        return state;
    }

    public void setState(StudentState estado) {
        state = estado;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public InfoStudentKind getInfoStudentKind() {
        return infoStudentKind;
    }

    public void setInfoStudentKind(InfoStudentKind info) {
        infoStudentKind = info;
    }

    /**
     * @return Returns the payedTuition.
     */
    public Boolean getPayedTuition() {
        return payedTuition;
    }

    /**
     * @param payedTuition
     *            The payedTuition to set.
     */
    public void setPayedTuition(Boolean payedTuition) {
        this.payedTuition = payedTuition;
    }

    // FIXME: The type of degree should be tested also
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoStudent) {
            InfoStudent infoAluno = (InfoStudent) obj;
            resultado = getNumber().equals(infoAluno.getNumber());
        }
        return resultado;
    }

    public String toString() {
        String result = "[InfoStudent";
        result += ", numero=" + number;
        result += ", degreeType=" + degreeType;
        result += ", estado=" + state;
        if (infoPerson != null)
            result += ", pessoa" + infoPerson.toString();
        result += "]";
        return result;
    }

    public void copyFromDomain(Registration student) {
        super.copyFromDomain(student);

        if (student != null) {
            setNumber(student.getNumber());
            setDegreeType(student.getDegreeType());
            setState(student.getState());
            setPayedTuition(student.getPayedTuition());
            setInfoPerson(InfoPerson.newInfoFromDomain(student.getPerson()));
            setFlunked(student.getFlunked());
            setRequestedChangeDegree(student.getRequestedChangeDegree());
            setInterruptedStudies(student.getInterruptedStudies());
        }
    }

    public static InfoStudent newInfoFromDomain(Registration student) {
        InfoStudent infoStudent = null;
        if (student != null) {
            infoStudent = new InfoStudent();
            infoStudent.copyFromDomain(student);
        }
        return infoStudent;
    }

	public Boolean getFlunked() {
		return flunked;
	}

	public void setFlunked(Boolean flunked) {
		this.flunked = flunked;
	}

	public Boolean getRequestedChangeDegree() {
		return requestedChangeDegree;
	}

	public void setRequestedChangeDegree(Boolean requestedChangeDegree) {
		this.requestedChangeDegree = requestedChangeDegree;
	}

	public Boolean getInterruptedStudies() {
		return interruptedStudies;
	}

	public void setInterruptedStudies(Boolean interruptedStudies) {
		this.interruptedStudies = interruptedStudies;
	}


	public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
		return infoStudentCurricularPlan;
	}

	public void setInfoStudentCurricularPlan(
			InfoStudentCurricularPlan infoStudentCurricularPlan) {
		this.infoStudentCurricularPlan = infoStudentCurricularPlan;
	}

	
}