/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.sms;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentSentSms extends IPersistentObject {
    public abstract List readByPerson(IPerson person) throws ExcepcaoPersistencia;

    public abstract List readByPerson(IPerson person, Integer interval) throws ExcepcaoPersistencia;

    public abstract Integer countByPersonAndDatePeriod(Integer personId, Date startDate, Date endDate)
            throws ExcepcaoPersistencia;
}