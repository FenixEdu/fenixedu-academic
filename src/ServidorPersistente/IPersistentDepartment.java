/*
 * IDepartment.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;


import java.util.List;

import Dominio.IDepartment;
import Dominio.ITeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface IPersistentDepartment extends IPersistentObject {

    public IDepartment lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia;
    public IDepartment lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia;
   
    public void escreverDepartamento(IDepartment departamento) throws ExcepcaoPersistencia, ExistingPersistentException;

    public List readAllDepartments() throws ExcepcaoPersistencia;
	public IDepartment readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}
