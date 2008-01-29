package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CourseLoadRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected CourseLoadRequestDocument(final CourseLoadRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected CourseLoadRequest getDocumentRequest() {
	return (CourseLoadRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	addDataSourceInformation();
	addParametersInformation();
    }

    private void addParametersInformation() {
	addParameter("studentNumber", getDocumentRequest().getRegistration().getStudent().getNumber());
	addParameter("degreeDescription", getDegreeDescription());
	
	addParameter("administrativeOfficeName", AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getName());
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
    }

    private void addDataSourceInformation() {
	final List<CourseLoadEntry> entries = new ArrayList<CourseLoadEntry>();
	for (final Enrolment enrolment : getDocumentRequest().getEnrolmentsSet()) {
	    entries.add(CourseLoadEntry.create(enrolment));
	}
	Collections.sort(entries);
	addDataSourceElements(entries);
    }

    @Override
    protected boolean showPriceFields() {
	return false;
    }

    @Override
    public String getReportTemplateKey() {
	return getDocumentRequest().getDocumentTemplateKey() + (getDocumentRequest().isBolonha() ? ".BOLONHA" : ".PREBOLONHA");
    }
    
    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }

    static abstract public class CourseLoadEntry implements Comparable<CourseLoadEntry> {
	private String curricularCourseName;
	private String year;
	private Double total;
	
	protected CourseLoadEntry(final String name, final String year) {
	    this.curricularCourseName = name;
	    this.year = year;
	}

	public String getCurricularCourseName() {
	    return curricularCourseName;
	}

	public void setCurricularCourseName(String curricularCourseName) {
	    this.curricularCourseName = curricularCourseName;
	}

	public String getYear() {
	    return year;
	}

	public void setYear(String year) {
	    this.year = year;
	}

	public int compareTo(CourseLoadEntry other) {
	    return getCurricularCourseName().compareTo(other.getCurricularCourseName());
	}
	
	public Double getTotal() {
	    return total;
	}

	public void setTotal(Double total) {
	    this.total = total;
	}
	
	public Boolean getCourseLoadCorrect() {
	    return Boolean.valueOf(total.doubleValue() != 0d);
	}
	
	static CourseLoadEntry create(final Enrolment enrolment) {
	    if (enrolment.isBolonhaDegree()) {
		return new BolonhaCourseLoadEntry(enrolment);
	    } else {
		return new PreBolonhaCourseLoadEntry(enrolment);
	    }
	}
    }
    
    static public class BolonhaCourseLoadEntry extends CourseLoadEntry {
	private Double contactLoad;
	private Double autonomousWork;
	
	public BolonhaCourseLoadEntry(final Enrolment enrolment) {
	    super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());
	    
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    setContactLoad(curricularCourse.getContactLoad(enrolment.getExecutionPeriod()));
	    setAutonomousWork(curricularCourse.getAutonomousWorkHours(enrolment.getExecutionPeriod()));
	    setTotal(curricularCourse.getTotalLoad(enrolment.getExecutionPeriod()));
	}
	
	public Double getAutonomousWork() {
	    return autonomousWork;
	}

	public void setAutonomousWork(Double autonomousWork) {
	    this.autonomousWork = autonomousWork;
	}

	public Double getContactLoad() {
	    return contactLoad;
	}

	public void setContactLoad(Double contactLoad) {
	    this.contactLoad = contactLoad;
	}
    }
    
    static public class PreBolonhaCourseLoadEntry extends CourseLoadEntry {
	private Double theoreticalHours;
	private Double praticalHours;
	private Double labHours;
	private Double theoPratHours;
	
	public PreBolonhaCourseLoadEntry(final Enrolment enrolment) {
	    super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());

	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    setTheoreticalHours(curricularCourse.getTheoreticalHours());
	    setPraticalHours(curricularCourse.getPraticalHours());
	    setLabHours(curricularCourse.getLabHours());
	    setTheoPratHours(curricularCourse.getTheoPratHours());
	    setTotal(calculateTotal(curricularCourse));
	}
	
	public Double getLabHours() {
	    return labHours;
	}

	public void setLabHours(Double labHours) {
	    this.labHours = labHours;
	}

	public Double getPraticalHours() {
	    return praticalHours;
	}

	public void setPraticalHours(Double praticalHours) {
	    this.praticalHours = praticalHours;
	}

	public Double getTheoPratHours() {
	    return theoPratHours;
	}

	public void setTheoPratHours(Double theoPratHours) {
	    this.theoPratHours = theoPratHours;
	}

	public Double getTheoreticalHours() {
	    return theoreticalHours;
	}

	public void setTheoreticalHours(Double theoreticalHours) {
	    this.theoreticalHours = theoreticalHours;
	}

	private Double calculateTotal(final CurricularCourse curricularCourse) {
	    double result = 0d;
	    result += curricularCourse.getTheoreticalHours();
	    result += curricularCourse.getPraticalHours();
	    result += curricularCourse.getLabHours();
	    result += curricularCourse.getTheoPratHours();
	    return Double.valueOf(result);
	}
    }
    
}
