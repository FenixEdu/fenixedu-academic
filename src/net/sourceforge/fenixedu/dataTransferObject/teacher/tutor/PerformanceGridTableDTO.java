package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;

public class PerformanceGridTableDTO extends DataTranferObject {
    private DomainReference<ExecutionYear> studentEntryYear;

    private DomainReference<ExecutionYear> monitoringYear;

    private List<PerformanceGridLine> performanceGridTableLines;

    public PerformanceGridTableDTO(ExecutionYear studentEntryYear, ExecutionYear monitoringYear) {
	setStudentEntryYear(studentEntryYear);
	setMonitoringYear(monitoringYear);
	this.performanceGridTableLines = new ArrayList<PerformanceGridLine>();
    }

    public ExecutionYear getMonitoringYear() {
	return (monitoringYear == null) ? null : this.monitoringYear.getObject();
    }

    public void setMonitoringYear(ExecutionYear monitoringYear) {
	this.monitoringYear = new DomainReference<ExecutionYear>(monitoringYear);
    }

    public ExecutionYear getStudentEntryYear() {
	return (studentEntryYear == null) ? null : this.studentEntryYear.getObject();
    }

    public void setStudentEntryYear(ExecutionYear studentEntryYear) {
	this.studentEntryYear = new DomainReference<ExecutionYear>(studentEntryYear);
    }

    public List<PerformanceGridLine> getPerformanceGridTableLines() {
	return performanceGridTableLines;
    }

    public void setPerformanceGridTableLines(List<PerformanceGridLine> performanceGridTableLines) {
	this.performanceGridTableLines = performanceGridTableLines;
    }

    public PerformanceGridLine getNewPerformanceGridLine(Tutorship tutorship) {
	return new PerformanceGridLine(tutorship);
    }

    public void addPerformanceGridLine(PerformanceGridLine newLine) {
	this.performanceGridTableLines.add(newLine);

    }

    /*
     * Performance Grid Line (one for each student)
     */
    public class PerformanceGridLine {
	private DomainReference<Registration> registration;

	private DomainReference<Tutorship> tutorship;

	private Double aritmeticAverage;

	private Integer approvedEnrolmentsNumber;

	private List<Enrolment> enrolmentsWithLastExecutionPeriod;

	private List<Integer> approvedRatioBySemester = new ArrayList<Integer>(2);

	private List<PerformanceGridLineYearGroup> studentPerformanceByYear = new ArrayList<PerformanceGridLineYearGroup>(5);

	public PerformanceGridLine(Tutorship tutoship) {
	    setTutorship(tutoship);
	    setRegistration(tutoship.getStudentCurricularPlan().getRegistration());
	}

	public Registration getRegistration() {
	    return (registration == null ? null : registration.getObject());
	}

	public void setRegistration(Registration registration) {
	    this.registration = new DomainReference<Registration>(registration);
	}

	public Tutorship getTutorship() {
	    return (tutorship == null ? null : tutorship.getObject());
	}

	public void setTutorship(Tutorship tutorship) {
	    this.tutorship = new DomainReference<Tutorship>(tutorship);
	}

	public List<Integer> getApprovedRatioBySemester() {
	    return approvedRatioBySemester;
	}

	public void setApprovedRatioBySemester(List<Integer> approvedRatioBySemester) {
	    this.approvedRatioBySemester = approvedRatioBySemester;
	}

	public Double getAritmeticAverage() {
	    return aritmeticAverage;
	}

	public void setAritmeticAverage(Double aritmeticAverage) {
	    this.aritmeticAverage = aritmeticAverage;
	}

	public Integer getApprovedEnrolmentsNumber() {
	    return approvedEnrolmentsNumber;
	}

	public void setApprovedEnrolmentsNumber(Integer approvedEnrolmentsNumber) {
	    this.approvedEnrolmentsNumber = approvedEnrolmentsNumber;
	}

	public String getEntryGrade() {
	    if (this.getRegistration().getEntryGrade() != null)
		return String.format("%.2f", this.getRegistration().getEntryGrade());
	    else
		return ("-");
	}

	public List<PerformanceGridLineYearGroup> getStudentPerformanceByYear() {
	    return this.studentPerformanceByYear;
	}

	public void setStudentPerformanceByYear(List<PerformanceGridLineYearGroup> studentPerformanceByYear) {
	    this.studentPerformanceByYear = studentPerformanceByYear;
	}

	public List<Enrolment> getEnrolmentsWithLastExecutionPeriod() {
	    return this.enrolmentsWithLastExecutionPeriod;
	}

	public void setEnrolmentsWithLastExecutionPeriod(List<Enrolment> enrolmentsWithLastExecutionPeriod) {
	    this.enrolmentsWithLastExecutionPeriod = enrolmentsWithLastExecutionPeriod;
	}

	public ExecutionYear getCurrentMonitoringYearYear() {
	    return getMonitoringYear();
	}

	public PerformanceGridLineYearGroup getNewPerformanceGridLineYearGroup() {
	    return new PerformanceGridLineYearGroup();
	}

	/*
	 * Student curricular information to present in each curricular year of
	 * the performance grid
	 */
	public class PerformanceGridLineYearGroup implements Serializable {
	    private List<DomainReference<Enrolment>> firstSemesterEnrolments;
	    private List<DomainReference<Enrolment>> secondSemesterEnrolments;

	    public PerformanceGridLineYearGroup() {
		firstSemesterEnrolments = new ArrayList<DomainReference<Enrolment>>();
		secondSemesterEnrolments = new ArrayList<DomainReference<Enrolment>>();
	    }

	    public List getFirstSemesterEnrolments() {
		List<Enrolment> enrolments = new ArrayList<Enrolment>();
		for (DomainReference<Enrolment> enrolmentDR : this.firstSemesterEnrolments) {
		    enrolments.add(enrolmentDR.getObject());
		}
		return enrolments;
	    }

	    public void setFirstSemesterEnrolments(List<Enrolment> firstSemesterEnrolments) {
		this.firstSemesterEnrolments = new ArrayList<DomainReference<Enrolment>>();
		for (Enrolment enrolment : firstSemesterEnrolments) {
		    this.firstSemesterEnrolments.add(new DomainReference<Enrolment>(enrolment));
		}
	    }

	    public List getSecondSemesterEnrolments() {
		List<Enrolment> enrolments = new ArrayList<Enrolment>();
		for (DomainReference<Enrolment> enrolmentDR : this.secondSemesterEnrolments) {
		    enrolments.add(enrolmentDR.getObject());
		}
		return enrolments;
	    }

	    public void setSecondSemesterEnrolments(List<Enrolment> secondSemesterEnrolments) {
		this.secondSemesterEnrolments = new ArrayList<DomainReference<Enrolment>>();
		for (Enrolment enrolment : secondSemesterEnrolments) {
		    this.secondSemesterEnrolments.add(new DomainReference<Enrolment>(enrolment));
		}
	    }

	    public void addEnrolmentToSemester(DegreeModuleScope scope, CurricularCourse curricular, Enrolment enrolment) {
		if (curricular.isAnual()) {
		    this.firstSemesterEnrolments.add(new DomainReference<Enrolment>(enrolment));
		    this.secondSemesterEnrolments.add(new DomainReference<Enrolment>(enrolment));
		} else {
		    if (scope.isFirstSemester()) {
			this.firstSemesterEnrolments.add(new DomainReference<Enrolment>(enrolment));
		    } else if (scope.isSecondSemester()) {
			this.secondSemesterEnrolments.add(new DomainReference<Enrolment>(enrolment));
		    }
		}
	    }
	}
    }
}
