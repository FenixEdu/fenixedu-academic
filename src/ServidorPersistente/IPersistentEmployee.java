/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IEmployee;
import Dominio.IPessoa;

/**
 * @author jpvl
 */
public interface IPersistentEmployee extends IPersistentObject {
	public IEmployee readByNumber(Integer number) throws ExcepcaoPersistencia;
	public IEmployee readByIdInternal(int idInternal) throws ExcepcaoPersistencia;
	public IEmployee readByPerson(int keyPerson) throws ExcepcaoPersistencia;
    public IEmployee readByPerson(IPessoa person) throws ExcepcaoPersistencia;
	public List readHistoricByKeyEmployee(int keyEmployee) throws ExcepcaoPersistencia;
}
