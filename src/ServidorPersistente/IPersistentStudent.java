package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IPessoa;
import Dominio.IStudent;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentStudent extends IPersistentObject {
	

// feitos por David \ Ricardo
	public IStudent readStudentByNumberAndDegreeType(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia;
	public IStudent readStudentByDegreeTypeAndPerson(TipoCurso degreeType, IPessoa person) throws ExcepcaoPersistencia;
	public ArrayList readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IStudent student) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IStudent student) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
		
// feitos pelo Nortadas
	public IStudent readByNumero(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia;
	public IStudent readByUsername(String username) throws ExcepcaoPersistencia;

	/**
	 * @deprecated
	 */
	public IStudent readByNumeroAndEstado(Integer numero, Integer estado, TipoCurso degreeType) throws ExcepcaoPersistencia;
	/**
	 * @deprecated
	 */
	public IStudent readByNumeroAndEstadoAndPessoa(Integer numero, Integer estado, IPessoa pessoa, TipoCurso degreeType) throws ExcepcaoPersistencia;
//	public void lockWrite(IStudent student) throws ExcepcaoPersistencia;
//	public void delete(IStudent aluno) throws ExcepcaoPersistencia;
//	public void deleteByNumeroAndEstado(Integer numero,Integer estado) throws ExcepcaoPersistencia;
//	public void deleteAll() throws ExcepcaoPersistencia;

}
