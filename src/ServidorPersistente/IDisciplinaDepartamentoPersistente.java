/*
 * IDepartmentCourse.java
 * 
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IDepartmentCourse;

public interface IDisciplinaDepartamentoPersistente extends IPersistentObject {

    public IDepartmentCourse lerDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia;

    public List lerTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia;

    public void apagarDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia;

    public void apagarDisciplinaDepartamento(IDepartmentCourse disciplina)
            throws ExcepcaoPersistencia;

}