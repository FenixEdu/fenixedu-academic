/*
 * SitioOJB.java Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DepartmentCourse;
import Dominio.IDepartmentCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;

public class DisciplinaDepartamentoOJB extends PersistentObjectOJB implements
        IDisciplinaDepartamentoPersistente {

    public DisciplinaDepartamentoOJB() {
    }

    public IDepartmentCourse lerDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        crit.addEqualTo("sigla", sigla);
        return (IDepartmentCourse) queryObject(DepartmentCourse.class, crit);

    }

    public void apagarDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        crit.addEqualTo("sigla", sigla);
        List result = queryList(DepartmentCourse.class, crit);
        if (result != null) {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext()) {
                super.delete(iterator.next());
            }
        }

    }

    public List lerTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia {
        return queryList(DepartmentCourse.class, new Criteria());
    }

    public void apagarDisciplinaDepartamento(IDepartmentCourse disciplina)
            throws ExcepcaoPersistencia {
        super.delete(disciplina);
    }

}