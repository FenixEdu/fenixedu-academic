/*
 * ICursoExecucaoPersistente.java
 *
 * Created on 2 de Novembro de 2002, 21:14
 */

package ServidorPersistente;

/**
 *
 * @author  rpfi
 */
import Dominio.ICurso;
import Dominio.ICursoExecucao;

public interface ICursoExecucaoPersistente extends IPersistentObject {
    public ICursoExecucao readByCursoAndAnoLectivo(ICurso curso, String anoLectivo) throws ExcepcaoPersistencia;
    public void lockWrite(ICursoExecucao cursoExecucao) throws ExcepcaoPersistencia;
    public void delete(ICursoExecucao cursoExecucao) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
	public ICursoExecucao readBySigla(String sigla) throws ExcepcaoPersistencia;
}
