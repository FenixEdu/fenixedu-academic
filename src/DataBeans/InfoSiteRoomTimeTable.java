/*
 * Created on 14/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 */
public class InfoSiteRoomTimeTable extends DataTranferObject implements ISiteComponent {

	//private List infoLessons;
    private List infoShowOccupation;
	private InfoRoom infoRoom;

	/**
	 * @return
	 */
	public List getInfoShowOccupation() {
		return infoShowOccupation;
	}

	/**
	 * @return
	 */
	public InfoRoom getInfoRoom() {
		return infoRoom;
	}

	/**
	 * @param list
	 */
	public void setInfoShowOccupation(List list) {
		infoShowOccupation = list;
	}

	/**
	 * @param list
	 */
	public void setInfoRoom(InfoRoom list) {
		infoRoom = list;
	}

}
