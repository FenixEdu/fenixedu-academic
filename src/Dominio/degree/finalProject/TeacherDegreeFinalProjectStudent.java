/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package Dominio.degree.finalProject;

import Dominio.DomainObject;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudent extends DomainObject
        implements
            ITeacherDegreeFinalProjectStudent
{
    private IExecutionYear executionYear;
    private Integer keyExecutionYear;
    private Integer keyStudent;
    private Integer keyTeacher;
    private IStudent student;
    private ITeacher teacher;

    public TeacherDegreeFinalProjectStudent()
    {
    }

    /**
	 * @param teacherDegreeFinalProjectStudentId
	 */
    public TeacherDegreeFinalProjectStudent(Integer teacherDegreeFinalProjectStudentId)
    {
        super(teacherDegreeFinalProjectStudentId);
    }

    /**
	 * @return Returns the executionYear.
	 */
    public IExecutionYear getExecutionYear()
    {
        return this.executionYear;
    }

    /**
	 * @return Returns the keyExecutionYear.
	 */
    public Integer getKeyExecutionYear()
    {
        return this.keyExecutionYear;
    }

    /**
	 * @return Returns the keyStudent.
	 */
    public Integer getKeyStudent()
    {
        return this.keyStudent;
    }

    /**
	 * @return Returns the keyTeacher.
	 */
    public Integer getKeyTeacher()
    {
        return this.keyTeacher;
    }

    /**
	 * @return Returns the student.
	 */
    public IStudent getStudent()
    {
        return this.student;
    }

    /**
	 * @return Returns the teacher.
	 */
    public ITeacher getTeacher()
    {
        return this.teacher;
    }

    /**
	 * @param executionYear
	 *                   The executionYear to set.
	 */
    public void setExecutionYear(IExecutionYear executionYear)
    {
        this.executionYear = executionYear;
    }

    /**
	 * @param keyExecutionYear
	 *                   The keyExecutionYear to set.
	 */
    public void setKeyExecutionYear(Integer keyExecutionYear)
    {
        this.keyExecutionYear = keyExecutionYear;
    }

    /**
	 * @param keyStudent
	 *                   The keyStudent to set.
	 */
    public void setKeyStudent(Integer keyStudent)
    {
        this.keyStudent = keyStudent;
    }

    /**
	 * @param keyTeacher
	 *                   The keyTeacher to set.
	 */
    public void setKeyTeacher(Integer keyTeacher)
    {
        this.keyTeacher = keyTeacher;
    }

    /**
	 * @param student
	 *                   The student to set.
	 */
    public void setStudent(IStudent student)
    {
        this.student = student;
    }

    /**
	 * @param teacher
	 *                   The teacher to set.
	 */
    public void setTeacher(ITeacher teacher)
    {
        this.teacher = teacher;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object obj)
    {
        if (obj instanceof TeacherDegreeFinalProjectStudent)
        {
            TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (TeacherDegreeFinalProjectStudent) obj;

            return (this.getStudent()
                    .equals(teacherDegreeFinalProjectStudent.getStudent().getIdInternal())
                    && this.getTeacher().equals(teacherDegreeFinalProjectStudent.getTeacher()) && this.getExecutionYear()
                    .equals(teacherDegreeFinalProjectStudent.getExecutionYear()));

        }
        return false;
    }

}