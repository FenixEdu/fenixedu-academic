/*
 * SitioOJB.java Created on 25 de Agosto de 2002, 1:02
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.domain.DepartmentCourse;
import net.sourceforge.fenixedu.domain.IDepartmentCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IDisciplinaDepartamentoPersistente;

import org.apache.ojb.broker.query.Criteria;

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