/*
 * Created on 15/Set/2003, 15:36:52
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans;
import java.util.Iterator;
import java.util.List;

import Dominio.IStudent;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 15/Set/2003, 15:36:52
 * 
 */
public class InfoGroupProjectStudents extends InfoObject
{
	private InfoStudentGroup studentGroup;
	private List studentList;
	/**
	 * @return
	 */
	public InfoStudentGroup getStudentGroup()
	{
		return studentGroup;
	}
	/**
	 * @return
	 */
	public List getStudentList()
	{
		return studentList;
	}
	/**
	 * @param group
	 */
	public void setStudentGroup(InfoStudentGroup group)
	{
		studentGroup= group;
	}
	/**
	 * @param list
	 */
	public void setStudentList(List list)
	{
		studentList= list;
	}
	public String toString()
	{
		String result= "[InfoGroupProjectStudents";
		result += "studentGroup: " + this.studentGroup + ";";
		result += "students: " + this.studentList + "]";
		return result;
	}
	public boolean isStudentMemberOfThisGroup(Integer number)
	{
		for (Iterator iter= studentList.iterator(); iter.hasNext();)
		{
			InfoStudent element= (InfoStudent) iter.next();
			if (element.getNumber().equals(number))
				return true;
		}
		return false;
	}
}
