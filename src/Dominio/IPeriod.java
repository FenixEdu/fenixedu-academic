/*
 * Created on 14/Out/2003
 *
  */
package Dominio;

import java.util.Calendar;
import java.util.List;

/**
 * @author Ana e Ricardo
 *
 */
public interface IPeriod extends IDomainObject{

	public Calendar getEndDate();
	public Calendar getStartDate();
	
	public void setEndDate(Calendar calendar);
	public void setStartDate(Calendar calendar);
	
	public List getRoomOccupations();
	public void setRoomOccupations(List roomOccupations);

    public IPeriod getNextPeriod();

    public void setNextPeriod(IPeriod nextPeriod);
    
    public Calendar getEndDateOfComposite();
    
    public boolean intersectPeriods(IPeriod period);
}
