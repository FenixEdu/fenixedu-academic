package DataBeans;


/**
 * @author Tânia Pousão Create on 10/Nov/2003
 */
public class InfoCampus extends InfoObject
{
    private String name;

    public InfoCampus()
    {
    }

    /**
     * @param integer
     */
    public InfoCampus(Integer campusId)
    {
        super(campusId);
    }

    /**
	 * @return Returns the name.
	 */
    public String getName()
    {
        return name;
    }

    /**
	 * @param name
	 *                   The name to set.
	 */
    public void setName(String name)
    {
        this.name = name;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof InfoCampus)
        {
            InfoCampus infoCampus = (InfoCampus) obj;
            result = getName().equals(infoCampus.getName());
        }
        return result;
    }

    public String toString()
    {
        String result = "[INFODEGREE_INFO:";
        result += " codigo interno= " + getIdInternal();
        result += " name= " + getName();
        result += "]";
        return result;
    }
}