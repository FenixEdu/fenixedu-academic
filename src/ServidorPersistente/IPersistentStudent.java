/*
 * IPersistentStudent.java
 *
 * Created on 23 de Outubro de 2002, 20:50
 */

package ServidorPersistente;

/**
 *
 * @author  Ricardo Nortadas
 */
//import Dominio.IPessoa;
import java.util.ArrayList;

import Dominio.IPessoa;
import Dominio.IStudent;
import Util.TipoCurso;

public interface IPersistentStudent extends IPersistentObject {
    public IStudent readByNumero(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia;
	public IStudent readByUsername(String username) throws ExcepcaoPersistencia;
    public IStudent readByNumeroAndEstado(Integer numero,Integer estado, TipoCurso degreeType) throws ExcepcaoPersistencia;
    public IStudent readByNumeroAndEstadoAndPessoa(Integer numero,Integer estado,IPessoa pessoa, TipoCurso degreeType) throws ExcepcaoPersistencia;
    public ArrayList readAllAlunos() throws ExcepcaoPersistencia;
    public void lockWrite(IStudent aluno) throws ExcepcaoPersistencia;
    public void delete(IStudent aluno) throws ExcepcaoPersistencia;
    //public void deleteByNumeroAndEstado(Integer numero,Integer estado) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
    
}
