/*
 * ICursoPersistente.java
 * 
 * Created on 31 de Outubro de 2002, 15:48
 */

package ServidorPersistente;

/**
 * @author rpfi
 */

import java.util.List;

import Dominio.ICurso;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public interface ICursoPersistente extends IPersistentObject {
	ICurso readBySigla(String sigla) throws ExcepcaoPersistencia;
	ICurso readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia;
	void lockWrite(ICurso degree) throws ExcepcaoPersistencia, ExistingPersistentException;
	void delete(ICurso degree) throws ExcepcaoPersistencia;
	void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public List readAllByDegreeType(TipoCurso degreeType) throws ExcepcaoPersistencia;
	public ICurso readByNameAndDegreeType(String name, TipoCurso degreeType) throws ExcepcaoPersistencia;
}
