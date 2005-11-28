/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;

/**
 * @author Ana e Ricardo
 * 
 */
public interface IPersistentPeriod extends IPersistentObject {
    public Object readByCalendarAndNextPeriod(Date startDate, Date endDate, Integer keyNextPeriod)
            throws ExcepcaoPersistencia;

}