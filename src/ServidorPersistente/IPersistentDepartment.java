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
    public void apagarDepartamentoPorNome(String nome) throws ExcepcaoPersistencia;
    public void apagarDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia;
    public void apagarDepartamento(IDepartment departamento) throws ExcepcaoPersistencia;
    public void apagarTodosOsDepartamentos() throws ExcepcaoPersistencia;
    public List readAllDepartments() throws ExcepcaoPersistencia;
	public IDepartment readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}
