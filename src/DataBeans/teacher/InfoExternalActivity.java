/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package DataBeans.teacher;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoExternalActivity extends InfoObject implements ISiteComponent
{

    private InfoTeacher infoTeacher;
    private String activity;

    public InfoExternalActivity()
    {
    }

    /**
	 * @return Returns the activity.
	 */
    public String getActivity()
    {
        return activity;
    }

    /**
	 * @param activity
	 *            The activity to set.
	 */
    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoExternalActivity)
        {
            resultado = getInfoTeacher().equals(((InfoExternalActivity) obj).getInfoTeacher());
        }
        return resultado;
    }
}
