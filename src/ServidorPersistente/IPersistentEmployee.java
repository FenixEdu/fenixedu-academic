/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.Employee;

/**
 * @author jpvl
 */
public interface IPersistentEmployee extends IPersistentObject {
	public Employee readByNumber(Integer number) throws ExcepcaoPersistencia;
	public Employee readByIdInternal(int idInternal) throws ExcepcaoPersistencia;
	public Employee readByPerson(int keyPerson) throws ExcepcaoPersistencia;
	public List readHistoricByKeyEmployee(int keyEmployee) throws ExcepcaoPersistencia;
}
