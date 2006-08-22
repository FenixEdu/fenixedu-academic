package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.FinalMark;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Ftp;

import org.apache.commons.lang.StringUtils;

public class SubmitMarks extends Service {
	
	private static final String NA = "NA";
	
	private static final String IMPROVMENT = "4";
	private static final String NORMAL = "0";
	
	private static final String NORMAL_DIR = "notas/";
	private static final String IMPROVMENT_DIR = "melhorias/";
	
	public void run(Integer executionCourseID, Integer evaluationID, String[] attendsIDs, Date evaluationDate, IUserView userView) throws ExcepcaoPersistencia, InvalidArgumentsServiceException, OutOfPeriodException {
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( executionCourseID);
		FinalEvaluation finalEvaluation = (FinalEvaluation) rootDomainObject.readEvaluationByOID(evaluationID);
		
		if(executionCourse == null || finalEvaluation == null) {
			throw new InvalidArgumentsServiceException();
		}
		
		checkEvaluationDateInExamsPeriod(executionCourse, evaluationDate);
		
		Map<GenericTrio<String, Degree, CurricularCourse>, List<Attends>> attendsMap = separateAttendsByDegreeAndEnrolmentType(executionCourse, attendsIDs, finalEvaluation);
		
		try {
			submitMarks(attendsMap, executionCourse, finalEvaluation, userView.getPerson().getTeacher(), evaluationDate);
		} catch (IOException e) {
			throw new ExcepcaoPersistencia(e.getMessage());
		}
	}

	private void checkEvaluationDateInExamsPeriod(ExecutionCourse executionCourse, Date evaluationDate) throws OutOfPeriodException {
		for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
			if(curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(DegreeType.DEGREE)) {
				ExecutionDegree executionDegree = curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionCourse.getExecutionPeriod().getExecutionYear());
				OccupationPeriod occupationPeriod = null;
				if(executionCourse.getExecutionPeriod().getSemester().equals(1)) {
					occupationPeriod = executionDegree.getPeriodExamsFirstSemester();
				} else {
					occupationPeriod = executionDegree.getPeriodExamsSecondSemester();
				}
				
				if(evaluationDate.before(occupationPeriod.getStart()) || evaluationDate.after(occupationPeriod.getEnd())) {
					throw new OutOfPeriodException("error.invalid.evaluationDate", occupationPeriod.getStart(), occupationPeriod.getEnd());
				}
			}
		}
	}

	private void submitMarks(Map<GenericTrio<String, Degree, CurricularCourse>, List<Attends>> attendsMap, ExecutionCourse executionCourse, FinalEvaluation finalEvaluation, Teacher teacher, Date evaluationDate) throws IOException {
		Map<String, InputStream> normalFileList = new HashMap<String, InputStream>();
		Map<String, InputStream> improvmentFileList = new HashMap<String, InputStream>();
		Integer version = finalEvaluation.getGradesListVersion();
		Date now = new Date();
		
		for (Entry<GenericTrio<String, Degree, CurricularCourse>, List<Attends>> mapEntry : attendsMap.entrySet()) {
			Map<Attends, FinalMark> marksMap = getMarks(mapEntry.getValue(), finalEvaluation, evaluationDate, now, version);
			if(mapEntry.getKey().getLeft().equals(IMPROVMENT)) {
				addMarksFileToMap(improvmentFileList, finalEvaluation, mapEntry.getKey().getMiddle(), mapEntry.getKey().getRight(), teacher, marksMap, executionCourse.getExecutionPeriod(), version, IMPROVMENT);
			} else {
				addMarksFileToMap(normalFileList, finalEvaluation, mapEntry.getKey().getMiddle(), mapEntry.getKey().getRight(), teacher, marksMap, executionCourse.getExecutionPeriod(), version, NORMAL);
			}
		}
		
		Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties", normalFileList, NORMAL_DIR);
		Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties", improvmentFileList, IMPROVMENT_DIR);
	}
	
	

	private void addMarksFileToMap(Map<String, InputStream> fileMap, FinalEvaluation finalEvaluation, Degree degree, CurricularCourse curricularCourse, Teacher teacher, Map<Attends, FinalMark> marksMap, ExecutionPeriod executionPeriod, Integer version, String season) throws IOException {
		MarksFile marksFile = new MarksFile(degree, teacher, curricularCourse.getCurricularSemesterWithLowerYearBySemester(executionPeriod.getSemester(), executionPeriod.getEndDate()), curricularCourse, version);
		marksFile.addLines(marksMap, season);
		fileMap.put(marksFile.getFileName(), new ByteArrayInputStream(marksFile.getFileContent().getBytes()));
	}

	private Map<Attends, FinalMark> getMarks(List<Attends> attendsList, FinalEvaluation finalEvaluation, Date evaluationDate, Date when, Integer version) {
		Map<Attends, FinalMark> map = new HashMap<Attends, FinalMark>();
		for (Attends attends : attendsList) {
			FinalMark mark = (FinalMark) attends.getMarkByEvaluation(finalEvaluation);
			if(mark == null) {
				mark = (FinalMark) finalEvaluation.addNewMark(attends, NA);
			} else {
				if(mark.getMark() == null || mark.getMark().length() == 0) {
					mark.setMark(NA);
				} 
			}
			map.put(attends, mark);
			mark.setGradeListVersion(version);
			mark.setSubmitedMark(mark.getMark());
			mark.setSubmitDate(evaluationDate);
			mark.setWhenSubmited(when);
		}
		return map;
	}
	
	private Map<GenericTrio<String, Degree, CurricularCourse>, List<Attends>> separateAttendsByDegreeAndEnrolmentType(ExecutionCourse executionCourse, String[] attendsIDs, FinalEvaluation finalEvaluation) {
		Map<GenericTrio<String, Degree, CurricularCourse>, List<Attends>> map = new HashMap<GenericTrio<String, Degree, CurricularCourse>, List<Attends>>();
		for (String attendsID : attendsIDs) {
			Attends attends = getAttendsByID(executionCourse, attendsID);
			if(attends != null && attends.getEnrolment() != null && !attends.getAluno().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
				if(attends.getEnrolment().isImprovementForExecutionCourse(executionCourse)) {
					addToMap(map, IMPROVMENT, attends.getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan().getDegree(), attends.getEnrolment().getCurricularCourse(), attends);
				} else {
					addToMap(map, NORMAL, attends.getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan().getDegree(), attends.getEnrolment().getCurricularCourse(), attends);
				}
			}
		}
		
		return map;
	}

	private void addToMap(Map<GenericTrio<String, Degree, CurricularCourse>, List<Attends>> map, String enrolmentType, Degree degree, CurricularCourse curricularCourse, Attends attends) {
		GenericTrio<String, Degree, CurricularCourse> trio = new GenericTrio<String, Degree, CurricularCourse>(enrolmentType, degree, curricularCourse);
		if(map.get(trio) == null) {
			map.put(trio, new ArrayList<Attends>());
		} 
		map.get(trio).add(attends);
	}

	private Attends getAttendsByID(ExecutionCourse executionCourse, String attendsID) {
		for (Attends attends : executionCourse.getAttends()) {
			if(attends.getIdInternal().equals(Integer.valueOf(attendsID))){
				return attends;
			}
		}
		return null;
	}
	
	private class MarksFile {
		private String degreeCode;
		private String teacherNumber;
		private String curricularYear;
		private String curricularSemester;
		private String curricularCourseCode;
		private String version;
		private String teacherMail;
		private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		private List<String> lines = new ArrayList<String>();
		
		public MarksFile(Degree degree, Teacher teacher, CurricularSemester curricularSemester, CurricularCourse curricularCourse, Integer version) {
			setDegreeCode(degree.getIdInternal().toString());
			setTeacherNumber(teacher.getTeacherNumber().toString());
			setCurricularSemester(curricularSemester.getSemester().toString());
			setCurricularYear(curricularSemester.getCurricularYear().getYear().toString());
			setCurricularCourseCode(curricularCourse.getCode());
			setVersion(version.toString());
			setTeacherMail(teacher.getPerson().getEmail());
		}

		public String getCurricularCourseCode() {
			return curricularCourseCode;
		}

		public void setCurricularCourseCode(String curricularCourseCode) {
			this.curricularCourseCode = StringUtils.rightPad(curricularCourseCode, 3, '_');
		}

		public String getCurricularSemester() {
			return curricularSemester;
		}

		public void setCurricularSemester(String curricularSemester) {
			this.curricularSemester = curricularSemester;
		}

		public String getCurricularYear() {
			return curricularYear;
		}

		public void setCurricularYear(String curricularYear) {
			this.curricularYear = curricularYear;
		}

		public String getDegreeCode() {
			return degreeCode;
		}

		public void setDegreeCode(String degreeCode) {
	        if (degreeCode.equals("51")) {
	            degreeCode = "24";
	        }
			this.degreeCode = StringUtils.leftPad(degreeCode, 2, '0');
		}

		public String getTeacherNumber() {
			return teacherNumber;
		}

		public void setTeacherNumber(String teacherNumber) {
			this.teacherNumber = StringUtils.leftPad(teacherNumber, 4, '0');
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = StringUtils.leftPad(version, 2, '0');
		}
		
		public String getFileName() {
			StringBuilder builder = new StringBuilder();
			builder.append(getDegreeCode());
			builder.append(getTeacherNumber());
			builder.append(getCurricularYear());
			builder.append(getCurricularSemester());
			builder.append(getCurricularCourseCode());
			builder.append(getVersion());
			builder.append(".TXT");
			return builder.toString();
		}

		public String getTeacherMail() {
			return teacherMail;
		}

		public void setTeacherMail(String teacherMail) {
			this.teacherMail = StringUtils.rightPad(teacherMail, 39, ' ');
		}
		
		private void addLine(Registration registration, String season, FinalMark mark, ExecutionYear executionYear) {
			StringBuilder builder = new StringBuilder();
			builder.append(getDegreeCode());
			builder.append(getCurricularYear());
			builder.append(getCurricularSemester());
			builder.append(season);
			builder.append(getCurricularCourseCode());
			builder.append(mark.getMark());
			builder.append(dateFormat.format(mark.getSubmitDate()));
			builder.append(getStudentNumber(registration.getNumber()));
			builder.append(getTeacherNumber());
			builder.append(dateFormat.format(mark.getWhenSubmited()));
			builder.append(getEnrolmentYear(executionYear));
			lines.add(builder.toString());
		}
		
		private String getEnrolmentYear(ExecutionYear executionYear) {
			String[] tokens = executionYear.getYear().split("/");
	        return StringUtils.trim(tokens[0]);
		}

		private String getStudentNumber(Integer number) {
			return StringUtils.leftPad(number.toString(), 5, '0');
		}

		public void addLines(Map<Attends, FinalMark> attendsMap, String season) {
			for (Entry<Attends, FinalMark> entry : attendsMap.entrySet()) {
				addLine(entry.getKey().getAluno(), season, entry.getValue(), entry.getKey().getEnrolment().getExecutionPeriod().getExecutionYear());
			}
		}
		
		public String getFileContent() {
			StringBuilder builder = new StringBuilder();
			builder.append(getTeacherMail());
			builder.append("\n");
			for (String line : lines) {
				builder.append(line);
				builder.append("\n");
			}
			return builder.toString();
		}
		
	}
	
	private static class GenericTrio<L, M, R>{
		private L left;
		private M middle;
		private R right;
		
		public GenericTrio(L left, M middle, R right) {
			this.left = left;
			this.middle = middle;
			this.right = right;
		}
		
		public L getLeft() {
			return left;
		}
		public void setLeft(L left) {
			this.left = left;
		}
		public M getMiddle() {
			return middle;
		}
		public void setMiddle(M middle) {
			this.middle = middle;
		}
		public R getRight() {
			return right;
		}
		public void setRight(R right) {
			this.right = right;
		}		
		
		public boolean equals(Object obj) {
			GenericTrio genericTrio = null;
			if(obj instanceof GenericTrio) {
				genericTrio = (GenericTrio) obj;
			} else {
				return false;
			}
			return (this.getLeft().equals(genericTrio.getLeft()) && this.getMiddle().equals(genericTrio.getMiddle()) && this.getRight().equals(genericTrio.getRight()));
		}
		
		public int hashCode() {
			StringBuilder builder = new StringBuilder();
			builder.append(String.valueOf(getLeft().hashCode()));
			builder.append('@');
			builder.append(String.valueOf(getMiddle().hashCode()));
			builder.append('@');
			builder.append(String.valueOf(getRight().hashCode()));
			
			return builder.toString().hashCode();
		}
	}
}
