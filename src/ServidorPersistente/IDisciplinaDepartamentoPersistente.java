/*
 * IDisciplinaDepartamento.java
 * 
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaDepartamento;

public interface IDisciplinaDepartamentoPersistente extends IPersistentObject {

    public IDisciplinaDepartamento lerDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia;

    public List lerTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia;

    public void apagarDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia;

    public void apagarDisciplinaDepartamento(IDisciplinaDepartamento disciplina)
            throws ExcepcaoPersistencia;

}