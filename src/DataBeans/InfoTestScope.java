/*
 * Created on 5/Fev/2004
 *
 */
package DataBeans;

/**
 *
 * @author Susana Fernandes
 *
 */
public class InfoTestScope extends InfoObject
{
	private InfoObject infoObject;

	public InfoTestScope()
	{
	}

	public InfoObject getInfoObject()
	{
		return infoObject;
	}

	public void setInfoObject(InfoObject object)
	{
		infoObject = object;
	}

}
