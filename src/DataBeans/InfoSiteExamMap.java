/*
 * Created on 8/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoSiteExamMap implements ISiteComponent {
	
	private InfoExamsMap infoExamsMap;
	private InfoExecutionDegree infoExecutionDegree;

	/**
	 * @return
	 */
	public InfoExamsMap getInfoExamsMap() {
		return infoExamsMap;
	}

	/**
	 * @param map
	 */
	public void setInfoExamsMap(InfoExamsMap map) {
		infoExamsMap = map;
	}

	/**
	 * @return
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * @param degree
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree degree) {
		infoExecutionDegree = degree;
	}

}
