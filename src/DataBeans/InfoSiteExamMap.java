/*
 * Created on 8/Mai/2003
 *
 * 
 */
package DataBeans;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteExamMap extends DataTranferObject implements ISiteComponent {
	
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
