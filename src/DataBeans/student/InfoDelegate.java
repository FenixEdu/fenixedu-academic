/*
 * Created on Feb 18, 2004
 *  
 */
package DataBeans.student;

import Util.DelegateType;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoObject;
import DataBeans.InfoStudent;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class InfoDelegate extends InfoObject
{
    private DelegateType type;
    private InfoStudent infoStudent;
    private InfoDegree infoDegree;
    private InfoExecutionYear infoExecutionYear;
    /**
	 *  
	 */
    public InfoDelegate()
    {
        super();
    }

    /**
	 * @param idInternal
	 */
    public InfoDelegate(Integer idInternal)
    {
        super(idInternal);
    }

    /**
	 * @return Returns the infoDegree.
	 */
    public InfoDegree getInfoDegree()
    {
        return infoDegree;
    }

    /**
	 * @param infoDegree
	 *            The infoDegree to set.
	 */
    public void setInfoDegree(InfoDegree infoDegree)
    {
        this.infoDegree = infoDegree;
    }

    /**
	 * @return Returns the infoExecutionYear.
	 */
    public InfoExecutionYear getInfoExecutionYear()
    {
        return infoExecutionYear;
    }

    /**
	 * @param infoExecutionYear
	 *            The infoExecutionYear to set.
	 */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear)
    {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
	 * @return Returns the infoStudent.
	 */
    public InfoStudent getInfoStudent()
    {
        return infoStudent;
    }

    /**
	 * @param infoStudent
	 *            The infoStudent to set.
	 */
    public void setInfoStudent(InfoStudent infoStudent)
    {
        this.infoStudent = infoStudent;
    }

    /**
	 * @return Returns the type.
	 */
    public DelegateType getType()
    {
        return type;
    }

    /**
	 * @param type
	 *            The type to set.
	 */
    public void setType(DelegateType type)
    {
        this.type = type;
    }

}
