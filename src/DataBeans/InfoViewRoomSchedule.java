/*
 * InfoViewRoomSchedule.java
 *
 * Created on 15 de Julho de 2003, 17:09
 */

package DataBeans;

import java.util.List;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class InfoViewRoomSchedule extends InfoObject {
	protected InfoRoom infoRoom;
	protected List roomLessons;
	
	public InfoViewRoomSchedule() {
	}

	public InfoViewRoomSchedule(
		InfoRoom infoRoom, List roomLessons) {
			setInfoRoom(infoRoom);
			setRoomLessons(roomLessons);
	}

	
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoViewRoomSchedule) {
			InfoViewRoomSchedule infoViewClassSchedule = (InfoViewRoomSchedule) obj;
			resultado =
				getInfoRoom().equals(infoViewClassSchedule.getInfoRoom()) &&
				getRoomLessons().size() == infoViewClassSchedule.getRoomLessons().size();
		}
		return resultado;
	}

	public InfoRoom getInfoRoom() {
		return infoRoom;
	}

	public List getRoomLessons() {
		return roomLessons;
	}

	public void setInfoRoom(InfoRoom class1) {
		infoRoom = class1;
	}

	public void setRoomLessons(List list) {
		roomLessons = list;
	}

}
