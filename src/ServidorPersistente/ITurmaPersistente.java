/*
 * ITurmaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 18:38
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.ITurma;

public interface ITurmaPersistente extends IPersistentObject {
	public ITurma readByNome(String nome) throws ExcepcaoPersistencia;
	public void lockWrite(ITurma turma) throws ExcepcaoPersistencia;
	public void delete(ITurma turma) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public List readBySemestreAndAnoCurricularAndSiglaLicenciatura(
		Integer semestre,
		Integer anoCurricular,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia;
}
