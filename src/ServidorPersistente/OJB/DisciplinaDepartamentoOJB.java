/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.DisciplinaDepartamento;
import Dominio.IDisciplinaDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DisciplinaDepartamentoOJB extends ObjectFenixOJB implements IDisciplinaDepartamentoPersistente {
    
    public DisciplinaDepartamentoOJB() {
    }
    
    public void apagarTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + DisciplinaDepartamento.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public void escreverDisciplinaDepartamento(IDisciplinaDepartamento departmentCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

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
		// else If the department course is mapped to the database, then write any existing changes.
		else if (
			(departmentCourseToWrite instanceof DisciplinaDepartamento)
				&& ((DisciplinaDepartamento) departmentCourseFromDB).getCodigoInterno().equals(
					((DisciplinaDepartamento) departmentCourseToWrite).getCodigoInterno())) {
			super.lockWrite(departmentCourseToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
    }
    
    public IDisciplinaDepartamento lerDisciplinaDepartamentoPorNomeESigla(String nome, String sigla) throws ExcepcaoPersistencia {
        try {
            IDisciplinaDepartamento de = null;
            String oqlQuery = "select all from " + DisciplinaDepartamento.class.getName();
            oqlQuery += " where nome = $1 and sigla = $2";
            query.create(oqlQuery);
            query.bind(nome);
            query.bind(sigla);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                de = (IDisciplinaDepartamento) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarDisciplinaDepartamentoPorNomeESigla(String nome, String sigla) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + DisciplinaDepartamento.class.getName();
            oqlQuery += " where nome = $1 and sigla = $2";
            query.create(oqlQuery);
            query.bind(nome);
            query.bind(sigla);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public ArrayList lerTodasAsDisciplinasDepartamento() throws ExcepcaoPersistencia {
        try {
            ArrayList listade = new ArrayList();
            String oqlQuery = "select all from " + DisciplinaDepartamento.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listade.add((IDisciplinaDepartamento)iterator.next());
            }
            return listade;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarDisciplinaDepartamento(IDisciplinaDepartamento disciplina) throws ExcepcaoPersistencia {
        super.delete(disciplina);
    }
    
}
