/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package DataBeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteCommon extends DataTranferObject implements ISiteComponent
{

    private String title;
    private String mail;
    private InfoExecutionCourse executionCourse;
    private List sections;
    private List associatedDegrees;
    // in reality the associatedDegrees list is a list of curricular courses

    public boolean equals(Object objectToCompare)
    {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteCommon
            && (((((InfoSiteCommon) objectToCompare).getTitle() != null
                && this.getTitle() != null
                && ((InfoSiteCommon) objectToCompare).getTitle().equals(this.getTitle()))
                || ((InfoSiteCommon) objectToCompare).getTitle() == null
                && this.getTitle() == null))
            && (((((InfoSiteCommon) objectToCompare).getMail() != null
                && this.getMail() != null
                && ((InfoSiteCommon) objectToCompare).getMail().equals(this.getMail()))
                || ((InfoSiteCommon) objectToCompare).getMail() == null
                && this.getMail() == null))
            && (((((InfoSiteCommon) objectToCompare).getExecutionCourse() != null
                && this.getExecutionCourse() != null
                && ((InfoSiteCommon) objectToCompare).getExecutionCourse().equals(
                    this.getExecutionCourse()))
                || ((InfoSiteCommon) objectToCompare).getExecutionCourse() == null
                && this.getExecutionCourse() == null)))
        {

            result = true;
        }

        if (((InfoSiteCommon) objectToCompare).getSections() == null
            && this.getSections() == null
            && result == true)
        {
            return true;
        }
        if (((InfoSiteCommon) objectToCompare).getSections() == null
            || this.getSections() == null
            || ((InfoSiteCommon) objectToCompare).getSections().size() != this.getSections().size())
        {
            return false;
        }

        ListIterator iter1 = ((InfoSiteCommon) objectToCompare).getSections().listIterator();
        ListIterator iter2 = this.getSections().listIterator();
        while (result && iter1.hasNext())
        {
            InfoSection infoSection1 = (InfoSection) iter1.next();
            InfoSection infoSection2 = (InfoSection) iter2.next();
            if (!infoSection1.equals(infoSection2))
            {
                System.out.println(infoSection1.getName());
                System.out.println(infoSection2.getName());
                result = false;
                break;
            }
        }

        return result;
    }

    /**
	 * @return
	 */
    public List getAssociatedDegrees()
    {
        return associatedDegrees;
    }

    public List getDegrees()
    {
        List infoDegreeList = new ArrayList();
        if (associatedDegrees != null)
        {
            Iterator iter = associatedDegrees.iterator();
            while (iter.hasNext())
            {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                InfoDegree infoDegree =
                    infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree();
                if (!infoDegreeList.contains(infoDegree))
                {
                    infoDegreeList.add(infoDegree);
                }
            }
            return infoDegreeList;
        }
        else
        {
            return null;
        }
    }
    /**
	 * @return
	 */
    public String getMail()
    {
        return mail;
    }

    /**
	 * @return
	 */
    public List getSections()
    {
        return sections;
    }

    /**
	 * @return
	 */
    public String getTitle()
    {
        return title;
    }

    /**
	 * @param list
	 */
    public void setAssociatedDegrees(List list)
    {
        associatedDegrees = list;
    }

    /**
	 * @param string
	 */
    public void setMail(String string)
    {
        mail = string;
    }

    /**
	 * @param list
	 */
    public void setSections(List list)
    {
        sections = list;
    }

    /**
	 * @param string
	 */
    public void setTitle(String string)
    {
        title = string;
    }

    /**
	 * @return
	 */
    public InfoExecutionCourse getExecutionCourse()
    {
        return executionCourse;
    }

    /**
	 * @param course
	 */
    public void setExecutionCourse(InfoExecutionCourse course)
    {
        executionCourse = course;
    }

}
