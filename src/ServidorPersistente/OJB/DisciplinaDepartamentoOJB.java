/*
 * SitioOJB.java
 * 
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DisciplinaDepartamento;
import Dominio.IDisciplinaDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DisciplinaDepartamentoOJB
    extends ObjectFenixOJB
    implements IDisciplinaDepartamentoPersistente
{

    public DisciplinaDepartamentoOJB()
    {
    }

    public void escreverDisciplinaDepartamento(IDisciplinaDepartamento departmentCourseToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IDisciplinaDepartamento departmentCourseFromDB = null;

        // If there is nothing to write, simply return.
        if (departmentCourseToWrite == null)
            return;

        // Read department course from database.
        departmentCourseFromDB =
            this.lerDisciplinaDepartamentoPorNomeESigla(
                departmentCourseToWrite.getNome(),
                departmentCourseToWrite.getSigla());

        // If department course is not in database, then write it.
        if (departmentCourseFromDB == null)
            super.lockWrite(departmentCourseToWrite);
        // else If the department course is mapped to the database, then write
        // any existing changes.
        else if (
            (departmentCourseToWrite instanceof DisciplinaDepartamento)
                && ((DisciplinaDepartamento) departmentCourseFromDB).getCodigoInterno().equals(
                    ((DisciplinaDepartamento) departmentCourseToWrite).getCodigoInterno()))
        {
            super.lockWrite(departmentCourseToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public IDisciplinaDepartamento lerDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        crit.addEqualTo("sigla", sigla);
        return (IDisciplinaDepartamento) queryObject(DisciplinaDepartamento.class, crit);

    }

    public void apagarDisciplinaDepartamentoPorNomeESigla(String nome, String sigla)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        crit.addEqualTo("sigla", sigla);
        List result = queryList(DisciplinaDepartamento.class, crit);
        if (result != null)
        {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
            {
                super.delete(iterator.next());
            }
        }

    }

    public List lerTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia
    {
        return queryList(DisciplinaDepartamento.class, new Criteria());
    }

    public void apagarDisciplinaDepartamento(IDisciplinaDepartamento disciplina)
        throws ExcepcaoPersistencia
    {
        super.delete(disciplina);
    }

}
