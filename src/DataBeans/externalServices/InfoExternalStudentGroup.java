/*
 * Created on 5:28:40 PM,Mar 8, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 5:28:40 PM, Mar 8, 2005
 */
public class InfoExternalStudentGroup
{
    private InfoExternalGroupInfo infoGroup;
    private Collection students;

    /**
     * 
     */
    public InfoExternalStudentGroup()
    {
       this.students = new ArrayList();
    }
    
    public void addStudent(InfoExternalStudentInfo student)
    {
        this.students.add(student);
    }
    
    /**
     * @return Returns the infoGroup.
     */
    public InfoExternalGroupInfo getInfoGroup()
    {
        return this.infoGroup;
    }
    /**
     * @param infoGroup The infoGroup to set.
     */
    public void setInfoGroup(InfoExternalGroupInfo infoGroup)
    {
        this.infoGroup = infoGroup;
    }
    /**
     * @return Returns the students.
     */
    public Collection getStudents()
    {
        return this.students;
    }
    /**
     * @param students The students to set.
     */
    public void setStudents(Collection students)
    {
        this.students = students;
    }
}