/*
 * Created on 2:43:44 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

import java.util.Collection;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:43:44 PM, Mar 11, 2005
 */
public class InfoStudentExternalInformation
{
    private InfoExternalPersonInfo person;
    private InfoExternalDegreeCurricularPlanInfo degree;
    private Collection courses;
    /**
     * @return Returns the courses.
     */
    public Collection getCourses()
    {
        return this.courses;
    }
    /**
     * @param courses The courses to set.
     */
    public void setCourses(Collection courses)
    {
        this.courses = courses;
    }
    /**
     * @return Returns the degree.
     */
    public InfoExternalDegreeCurricularPlanInfo getDegree()
    {
        return this.degree;
    }
    /**
     * @param degree The degree to set.
     */
    public void setDegree(InfoExternalDegreeCurricularPlanInfo degree)
    {
        this.degree = degree;
    }
    /**
     * @return Returns the person.
     */
    public InfoExternalPersonInfo getPerson()
    {
        return this.person;
    }
    /**
     * @param person The person to set.
     */
    public void setPerson(InfoExternalPersonInfo person)
    {
        this.person = person;
    }
}
