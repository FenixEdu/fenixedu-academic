/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package ServidorPersistente;

import Dominio.Funcionario;

/**
 * @author jpvl
 */
public interface IPersistentEmployee extends IPersistentObject {
	Funcionario readByNumber(Integer number)  throws ExcepcaoPersistencia;
}
