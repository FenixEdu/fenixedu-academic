/*
 * Created on 7/Jun/2004
 *  
 */
package ServidorPersistente.sms;

import java.util.Date;
import java.util.List;

import Dominio.IPessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public interface IPersistentSentSms extends IPersistentObject
{
	public abstract List readByPerson(IPessoa person) throws ExcepcaoPersistencia;
	public abstract List readByPerson(IPessoa person, Integer interval) throws ExcepcaoPersistencia;
	public abstract Integer countByPersonAndDatePeriod(Integer personId, Date startDate, Date endDate)
		throws ExcepcaoPersistencia;
}