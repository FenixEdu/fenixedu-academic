package net.sourceforge.fenixedu.presentationTier.Action.gep.a3es;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation.LecturedCurricularUnit;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation.QualificationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;

public class A3esBean implements Serializable {
    private DegreeType degreeType;
    private Degree degree;
    private PhdProgram phdProgram;
    private ExecutionSemester executionSemester;

    public A3esBean() {
	super();
	executionSemester = ExecutionSemester.readActualExecutionSemester();
    }

    public DegreeType getDegreeType() {
	return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    public Degree getDegree() {
	return degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public PhdProgram getPhdProgram() {
	return phdProgram;
    }

    public void setPhdProgram(PhdProgram phdProgram) {
	this.phdProgram = phdProgram;
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public List<Degree> getAvailableDegrees() {
	if (degreeType != null) {
	    return Degree.readAllByDegreeType(degreeType);
	}
	return ListUtils.EMPTY_LIST;
    }

    public List<ExecutionSemester> getAvailableExecutionSemesters() {
	List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	ExecutionSemester readActualExecutionSemester = ExecutionSemester.readActualExecutionSemester();
	result.add(readActualExecutionSemester);
	result.add(readActualExecutionSemester.getPreviousExecutionPeriod());
	return result;
    }

    public List<ExecutionSemester> getSelectedExecutionSemesters() {
	List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	if (getExecutionSemester() != null) {
	    result.add(getExecutionSemester());
	    result.add(getExecutionSemester().getPreviousExecutionPeriod());
	}
	return result;
    }

    public SpreadsheetBuilder getTeacherCurricularInformation() {
	SpreadsheetBuilder spreadsheetBuilder = new SpreadsheetBuilder();
	Set<Teacher> teachers = new HashSet<Teacher>();
	List<ExecutionSemester> executionSemesters = getSelectedExecutionSemesters();
	for (ExecutionSemester executionSemester : executionSemesters) {
	    for (ExecutionCourse executionCourse : getExecutionCourses(degree, executionSemester)) {
		for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
		    if (professorhip.getPerson().getTeacher().isActiveOrHasAuthorizationForSemester(executionSemester)) {
			teachers.add(professorhip.getPerson().getTeacher());
		    }
		}
	    }
	    for (Thesis thesis : degree.getThesis()) {
		if (executionSemesters.contains(thesis.getEnrolment().getExecutionPeriod())) {
		    for (ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getOrientation()) {
			if (thesisEvaluationParticipant.getPerson().getTeacher() != null
				&& thesisEvaluationParticipant.getPerson().getTeacher()
					.isActiveOrHasAuthorizationForSemester(executionSemester)) {
			    teachers.add(thesisEvaluationParticipant.getPerson().getTeacher());
			}
		    }
		}
	    }
	}

	PhdProgram phdProgram = degree.getPhdProgram();
	if (phdProgram != null) {
	    for (PhdIndividualProgramProcess phdIndividualProgramProcess : phdProgram.getIndividualProgramProcesses()) {
		for (ExecutionSemester executionSemester : executionSemesters) {
		    if (phdIndividualProgramProcess.isActive(executionSemester.getAcademicInterval().toInterval())) {
			for (PhdParticipant phdParticipant : phdIndividualProgramProcess.getParticipants()) {
			    if (phdParticipant instanceof InternalPhdParticipant) {
				InternalPhdParticipant internalPhdParticipant = (InternalPhdParticipant) phdParticipant;
				if (internalPhdParticipant.isGuidingOrAssistantGuiding()
					&& internalPhdParticipant.getPerson().getTeacher() != null
					&& internalPhdParticipant.getPerson().getTeacher()
						.isActiveOrHasAuthorizationForSemester(executionSemester)) {
				    teachers.add(internalPhdParticipant.getPerson().getTeacher());
				}
			    }
			}
		    }
		}
	    }
	}
	spreadsheetBuilder = spreadsheetBuilder.addSheet(degree.getSigla(), generateSheet(teachers, degree, executionSemesters));
	return spreadsheetBuilder;
    }

    private List<ExecutionCourse> getExecutionCourses(final Degree degree, final ExecutionSemester executionSemester) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
		    if (executionSemester == executionCourse.getExecutionPeriod()) {
			for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
			    if (scope.isActiveForExecutionPeriod(executionSemester)
				    && scope.getCurricularSemester() == executionSemester.getSemester()) {
				result.add(executionCourse);
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    private SheetData<TeacherCurricularInformation> generateSheet(Set<Teacher> teachers, Degree degree,
	    List<ExecutionSemester> executionSemesters) {

	List<TeacherCurricularInformation> teacherCurricularInformationList = new ArrayList<TeacherCurricularInformation>();
	for (Teacher teacher : teachers) {
	    TeacherCurricularInformation teacherCurricularInformation = new TeacherCurricularInformation(teacher, degree,
		    executionSemesters);
	    teacherCurricularInformationList.add(teacherCurricularInformation);
	}

	SheetData<TeacherCurricularInformation> sheet = new SheetData<TeacherCurricularInformation>(
		teacherCurricularInformationList) {
	    @Override
	    protected void makeLine(TeacherCurricularInformation teacherCurricularInformation) {
		addCell("IstId", teacherCurricularInformation.getTeacher().getPerson().getUsername());
		addCell("Nome", teacherCurricularInformation.getTeacher().getPerson().getName());
		addCell("Instituição", "Instituto Superior Técnico");
		addCell("Unidade Orgânica", teacherCurricularInformation.getUnitName());
		addCell("Categoria", teacherCurricularInformation.getProfessionalCategoryName());
		QualificationBean qualification = teacherCurricularInformation.getCurrentQualification();
		addCell("Grau", qualification != null ? qualification.getDegree() : null);
		addCell("Área científica", qualification != null ? qualification.getScientificArea() : null);
		addCell("Ano", qualification != null ? qualification.getYear() : null);
		addCell("Instituição", qualification != null ? qualification.getInstitution() : null);
		addCell("Regime", teacherCurricularInformation.getProfessionalRegimeName());
		SortedSet<QualificationBean> otherQualifications = teacherCurricularInformation.getOtherQualifications();
		List<String> otherQualificationStrings = new ArrayList<String>();
		for (QualificationBean otherQualification : otherQualifications) {
		    StringBuilder qualificationString = new StringBuilder();
		    qualificationString.append(otherQualification.getYear()).append(",");
		    qualificationString.append(otherQualification.getDegree()).append(",");
		    qualificationString.append(otherQualification.getScientificArea()).append(",");
		    qualificationString.append(otherQualification.getInstitution()).append(",");
		    qualificationString.append(otherQualification.getClassification());
		    otherQualificationStrings.add(qualificationString.toString());
		}
		addCell("Outras Qualificações", StringUtils.join(otherQualificationStrings, "\n"));
		addCell("Publicações", StringUtils.join(teacherCurricularInformation.getTop5ResultParticipation(), "\n"));
		addCell("Experiência Profissional",
			StringUtils.join(teacherCurricularInformation.getTop5ProfessionalCareer(), "\n"));

		List<String> lectured = new ArrayList<String>();
		for (LecturedCurricularUnit lecturedCurricularUnit : teacherCurricularInformation.getLecturedUCsOnCycle()) {
		    StringBuilder lecturedString = new StringBuilder();
		    lecturedString.append(lecturedCurricularUnit.getName()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getShiftType()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getHours());
		    lectured.add(lecturedString.toString());
		}
		addCell("UCs no ciclo proposto", StringUtils.join(lectured, "\n"));

		lectured = new ArrayList<String>();
		for (LecturedCurricularUnit lecturedCurricularUnit : teacherCurricularInformation.getLecturedUCsOnOtherCycles()) {
		    StringBuilder lecturedString = new StringBuilder();
		    lecturedString.append(lecturedCurricularUnit.getName()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getShiftType()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getHours());
		    lectured.add(lecturedString.toString());
		}
		addCell("UCs outros ciclos", StringUtils.join(lectured, "\n"));
	    }
	};
	return sheet;
    }
}
