package middleware.marks;

import java.util.Calendar;
import java.util.Date;

import Dominio.IEmployee;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class RowMarksFile
{
    public static final int MAX_COURSECODE = 3;
    public static final int MAX_STUDENT_NUMBER = 5;
    public static final int MAX_TEACHER_NUMBER = 4;

    private Integer degreeCode = null;
    private Integer curricularYear = null;
    private Integer curricularSemester = null;
    private Integer curricularSeason = null;
    private String courseCode = null;
    private String mark = null;
    private Date evaluationDate = null;
    private Integer studentNumber = null;
    private Integer teacherNumber = null;
    private Date submitDate = null;

    public RowMarksFile()
    {
        setDegreeCode(new Integer(0));
        setCurricularYear(new Integer(0));
        setCurricularSemester(new Integer(0));
        setCurricularSeason(new Integer(0));
        setCourseCode(new String());
        setMark(new String());
        setEvaluationDate(new Date());
        setStudentNumber(new Integer(0));
        setTeacherNumber(new Integer(0));
        setSubmitDate(new Date());
    }

    public RowMarksFile(IEnrolmentEvaluation enrolmentEvaluation)
    {
        setDegreeCode(
            enrolmentEvaluation
                .getEnrolment()
                .getStudentCurricularPlan()
                .getDegreeCurricularPlan()
                .getDegree()
                .getIdInternal());
        setCurricularYear(
            enrolmentEvaluation
                .getEnrolment()
                .getCurricularCourseScope()
                .getCurricularSemester()
                .getCurricularYear()
                .getYear());
        setCurricularSemester(
            enrolmentEvaluation
                .getEnrolment()
                .getCurricularCourseScope()
                .getCurricularSemester()
                .getSemester());
        setCurricularSeason(new Integer(0));
        setCourseCode(
            enrolmentEvaluation
                .getEnrolment()
                .getCurricularCourseScope()
                .getCurricularCourse()
                .getCode());
        setMark(enrolmentEvaluation.getGrade());
        setEvaluationDate(enrolmentEvaluation.getExamDate());
        setStudentNumber(
            enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getStudent().getNumber());
        setTeacherNumber(
            readEmployee(enrolmentEvaluation.getPersonResponsibleForGrade()).getEmployeeNumber());
        setSubmitDate(enrolmentEvaluation.getGradeAvailableDate());

    }

    public void transform(IEnrolmentEvaluation enrolmentEvaluation)
    {
        setDegreeCode(
            enrolmentEvaluation
                .getEnrolment()
                .getStudentCurricularPlan()
                .getDegreeCurricularPlan()
                .getDegree()
                .getIdInternal());
        setCurricularYear(
            enrolmentEvaluation
                .getEnrolment()
                .getCurricularCourseScope()
                .getCurricularSemester()
                .getCurricularYear()
                .getYear());
        setCurricularSemester(
            enrolmentEvaluation
                .getEnrolment()
                .getCurricularCourseScope()
                .getCurricularSemester()
                .getSemester());
        setCurricularSeason(new Integer(0));
        setCourseCode(
            enrolmentEvaluation
                .getEnrolment()
                .getCurricularCourseScope()
                .getCurricularCourse()
                .getCode());
        setMark(enrolmentEvaluation.getGrade());
        setEvaluationDate(enrolmentEvaluation.getExamDate());
        setStudentNumber(
            enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getStudent().getNumber());
        setTeacherNumber(
            readEmployee(enrolmentEvaluation.getPersonResponsibleForGrade()).getEmployeeNumber());
        setSubmitDate(enrolmentEvaluation.getGradeAvailableDate());
    }

    private IEmployee readEmployee(IPessoa person)
    {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try
        {
            persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
        }
        return employee;
    }

    public String toString()
    {
        Calendar calendar = Calendar.getInstance();

        String result = degreeCode.toString();
        result += curricularYear.toString();
        result += curricularSemester.toString();
        result += curricularSeason.toString();
        result += courseCode;
        result += mark;

        calendar.clear();
        calendar.setTime(evaluationDate);
        result += calendar.get(Calendar.DAY_OF_MONTH)
            + "/"
            + calendar.get(Calendar.MONTH)
            + "/"
            + calendar.get(Calendar.YEAR);

        result += studentNumber.toString();
        result += teacherNumber.toString();

        calendar.clear();
        calendar.setTime(submitDate);
        result += calendar.get(Calendar.DAY_OF_MONTH)
            + "/"
            + calendar.get(Calendar.MONTH)
            + "/"
            + calendar.get(Calendar.YEAR);

        return result;
    }

    public String toWriteInFile()
    {
        Calendar calendar = Calendar.getInstance();
        String day;
        String month;

        String result;
        if (degreeCode.intValue() < 10)
            result = "0" + degreeCode.toString();
        else
            result = degreeCode.toString();
        result += curricularYear.toString();
        result += curricularSemester.toString();
        result += curricularSeason.toString();

        int indice = courseCode.length();
        while (indice < MAX_COURSECODE)
        {
            courseCode = courseCode.concat("_");
            indice++;
        }
        result += courseCode;

        result += mark;

        calendar.clear();
        calendar.setTime(evaluationDate);
        day = new String(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        month = new String(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) <= 9)
        {
            day = "0" + day;
        }
        if (calendar.get(Calendar.MONTH) <= 9)
        {
            month = "0" + month;
        }
        result += day + "/" + month + "/" + calendar.get(Calendar.YEAR);

        String sNumber = studentNumber.toString();
        while (sNumber.length() < MAX_STUDENT_NUMBER)
        {
            sNumber = "0" + sNumber;
        }

        result += sNumber;

        String tNumber = teacherNumber.toString();
        while (tNumber.length() < MAX_TEACHER_NUMBER)
        {
            tNumber = "0" + tNumber;
        }
        result += tNumber;

        calendar.clear();
        calendar.setTime(submitDate);
        day = new String(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        month = new String(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) <= 9)
        {
            day = "0" + day;
        }
        if (calendar.get(Calendar.MONTH) <= 9)
        {
            month = "0" + month;
        }
        result += day + "/" + month + "/" + calendar.get(Calendar.YEAR);

        return result;
    }

    public String fileName()
    {
        String fileName;
        if (degreeCode.intValue() < 10)
            fileName = "0" + degreeCode.toString();
        else
            fileName = degreeCode.toString();
        String tNumber = teacherNumber.toString();
        while (tNumber.length() < MAX_TEACHER_NUMBER)
        {
            tNumber = "0" + tNumber;
        }
        fileName += tNumber;
        fileName += curricularYear.toString();
        fileName += curricularSemester.toString();
        
		int indice = courseCode.length();
		while (indice < MAX_COURSECODE)
		{
			courseCode = courseCode.concat("_");
			indice++;
		}
        fileName += courseCode;
        fileName += ".txt";
        return fileName;
    }

    public String getCourseCode()
    {
        return courseCode;
    }
    public Integer getCurricularSeason()
    {
        return curricularSeason;
    }
    public Integer getCurricularSemester()
    {
        return curricularSemester;
    }
    public Integer getCurricularYear()
    {
        return curricularYear;
    }
    public Integer getDegreeCode()
    {
        return degreeCode;
    }
    public Date getEvaluationDate()
    {
        return evaluationDate;
    }
    public String getMark()
    {
        return mark;
    }
    public Integer getStudentNumber()
    {
        return studentNumber;
    }
    public Date getSubmitDate()
    {
        return submitDate;
    }
    public Integer getTeacherNumber()
    {
        return teacherNumber;
    }
    public void setCourseCode(String string)
    {
        courseCode = string;
    }
    public void setCurricularSeason(Integer integer)
    {
        curricularSeason = integer;
    }
    public void setCurricularSemester(Integer integer)
    {
        curricularSemester = integer;
    }
    public void setCurricularYear(Integer integer)
    {
        curricularYear = integer;
    }
    public void setDegreeCode(Integer integer)
    {
        degreeCode = integer;
    }
    public void setEvaluationDate(Date date)
    {
        evaluationDate = date;
    }
    public void setMark(String string)
    {
        mark = string;
    }
    public void setStudentNumber(Integer integer)
    {
        studentNumber = integer;
    }
    public void setSubmitDate(Date date)
    {
        submitDate = date;
    }
    public void setTeacherNumber(Integer integer)
    {
        teacherNumber = integer;
    }

}
