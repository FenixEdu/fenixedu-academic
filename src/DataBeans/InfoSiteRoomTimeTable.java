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

	private List infoLessons;
	private InfoRoom infoRoom;

	/**
	 * @return
	 */
	public List getInfoLessons() {
		return infoLessons;
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
	public void setInfoLessons(List list) {
		infoLessons = list;
	}

	/**
	 * @param list
	 */
	public void setInfoRoom(InfoRoom list) {
		infoRoom = list;
	}

}
