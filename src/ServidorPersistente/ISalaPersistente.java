/*
 * ISalaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 15:09
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.ISala;

public interface ISalaPersistente extends IPersistentObject {
	public ISala readByName(String nome) throws ExcepcaoPersistencia;
	public void lockWrite(ISala sala) throws ExcepcaoPersistencia;
	public void delete(ISala sala) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public List readSalas(
		String nome,
		String edificio,
		Integer piso,
		Integer tipo,
		Integer capacidadeNormal,
		Integer capacidadeExame)
		throws ExcepcaoPersistencia;
}
