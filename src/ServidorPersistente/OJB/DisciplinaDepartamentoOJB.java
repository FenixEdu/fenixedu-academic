/*
 * SitioOJB.java Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DisciplinaDepartamento;
import Dominio.IDisciplinaDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;

public class DisciplinaDepartamentoOJB extends PersistentObjectOJB implements
        IDisciplinaDepartamentoPersistente {

    public DisciplinaDepartamentoOJB() {
    }

    public IDisciplinaDepartamento lerDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        crit.addEqualTo("sigla", sigla);
        return (IDisciplinaDepartamento) queryObject(DisciplinaDepartamento.class, crit);

    }

    public void apagarDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        crit.addEqualTo("sigla", sigla);
        List result = queryList(DisciplinaDepartamento.class, crit);
        if (result != null) {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext()) {
                super.delete(iterator.next());
            }
        }

    }

    public List lerTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia {
        return queryList(DisciplinaDepartamento.class, new Criteria());
    }

    public void apagarDisciplinaDepartamento(IDisciplinaDepartamento disciplina)
            throws ExcepcaoPersistencia {
        super.delete(disciplina);
    }

}