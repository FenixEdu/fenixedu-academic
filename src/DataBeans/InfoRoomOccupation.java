/*
 * Created on 31/Out/2003
 *
  */
package DataBeans;

import java.util.Calendar;
import Dominio.IRoomOccupation;
import Util.DiaSemana;

/**
 * @author Ana e Ricardo
 *
 */
public class InfoRoomOccupation extends InfoObject {

	protected Calendar startTime;
	protected Calendar endTime;
	protected DiaSemana dayOfWeek;
	protected InfoRoom infoRoom;
	protected InfoPeriod infoPeriod;
	
	protected Integer frequency;
    protected Integer weekOfQuinzenalStart;
	
    public Integer getFrequency() {
        return frequency;
    }
 
    public void setFrequency(int frequency) {
        this.frequency = new Integer(frequency);
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getWeekOfQuinzenalStart() {
        return weekOfQuinzenalStart;
    }

    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
        this.weekOfQuinzenalStart = weekOfQuinzenalStart;
    }
	
	/**
	 * @return
	 */
	public DiaSemana getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @return
	 */
	public Calendar getEndTime() {
		return endTime;
	}

	/**
	 * @return
	 */
	public Calendar getStartTime() {
		return startTime;
	}

	/**
	 * @param semana
	 */
	public void setDayOfWeek(DiaSemana semana) {
		dayOfWeek = semana;
	}

	/**
	 * @param calendar
	 */
	public void setEndTime(Calendar calendar) {
		endTime = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setStartTime(Calendar calendar) {
		startTime = calendar;
	}

	/**
	 * @return Returns the infoPeriod.
	 */
	public InfoPeriod getInfoPeriod() {
		return infoPeriod;
	}

	/**
	 * @param infoPeriod The infoPeriod to set.
	 */
	public void setInfoPeriod(InfoPeriod infoPeriod) {
		this.infoPeriod = infoPeriod;
	}

	/**
	 * @return Returns the infoRoom.
	 */
	public InfoRoom getInfoRoom() {
		return infoRoom;
	}

	/**
	 * @param infoRoom The infoRoom to set.
	 */
	public void setInfoRoom(InfoRoom infoRoom) {
		this.infoRoom = infoRoom;
	}

    /* (non-Javadoc)
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IRoomOccupation roomOccupation) {
        super.copyFromDomain(roomOccupation);
        if(roomOccupation != null) {
            setDayOfWeek(roomOccupation.getDayOfWeek());
            setStartTime(roomOccupation.getStartTime());
            setEndTime(roomOccupation.getEndTime());
        }
    }
    
    public static InfoRoomOccupation newInfoFromDomain(IRoomOccupation roomOccupation) {
        InfoRoomOccupation infoRoomOccupation = null;
        if(roomOccupation != null) {
            infoRoomOccupation = new InfoRoomOccupation();
            infoRoomOccupation.copyFromDomain(roomOccupation);
        }
        return infoRoomOccupation;
    }
}
