/*
 * IDepartamento.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IDepartamento;

public interface IDepartamentoPersistente extends IPersistentObject {
    
    public IDepartamento lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia;
    public IDepartamento lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia;
    public ArrayList lerTodosOsDepartamentos() throws ExcepcaoPersistencia;
    public void escreverDepartamento(IDepartamento departamento) throws ExcepcaoPersistencia;
    public void apagarDepartamentoPorNome(String nome) throws ExcepcaoPersistencia;
    public void apagarDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia;
    public void apagarDepartamento(IDepartamento departamento) throws ExcepcaoPersistencia;
    public void apagarTodosOsDepartamentos() throws ExcepcaoPersistencia;
}
