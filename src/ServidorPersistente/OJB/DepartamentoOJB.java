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

import Dominio.Departamento;
import Dominio.IDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DepartamentoOJB extends ObjectFenixOJB implements IDepartamentoPersistente {
    
    public DepartamentoOJB() {
    }
    
    public void apagarTodosOsDepartamentos() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Departamento.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public void escreverDepartamento(IDepartamento departmentToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IDepartamento departmentFromDB = null;

		// If there is nothing to write, simply return.
		if (departmentToWrite == null)
			return;

		// Read department from database.
		departmentFromDB = this.lerDepartamentoPorNome(departmentToWrite.getNome());

		// If department is not in database, then write it.
		if (departmentFromDB == null)
			super.lockWrite(departmentToWrite);
		// else If the department is mapped to the database, then write any existing changes.
		else if (
			(departmentToWrite instanceof Departamento)
				&& ((Departamento) departmentFromDB)
					.getCodigoInterno()
					.equals(
					((Departamento) departmentToWrite)
						.getCodigoInterno())) {
			super.lockWrite(departmentToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
    }
    
    public IDepartamento lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia {
        try {
            IDepartamento de = null;
            String oqlQuery = "select all from " + Departamento.class.getName();
            oqlQuery += " where nome = $1";
            query.create(oqlQuery);
            query.bind(nome);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                de = (IDepartamento) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IDepartamento lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia {
        try {
            IDepartamento de = null;
            String oqlQuery = "select all from " + Departamento.class.getName();
            oqlQuery += " where sigla = $1";
            query.create(oqlQuery);
            query.bind(sigla);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                de = (IDepartamento) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarDepartamentoPorNome(String nome) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Departamento.class.getName();
            oqlQuery += " where nome = $1";
            query.create(oqlQuery);
            query.bind(nome);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void apagarDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Departamento.class.getName();
            oqlQuery += " where sigla = $1";
            query.create(oqlQuery);
            query.bind(sigla);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public ArrayList lerTodosOsDepartamentos() throws ExcepcaoPersistencia {
        try {
            ArrayList listade = new ArrayList();
            String oqlQuery = "select all from " + Departamento.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listade.add((IDepartamento)iterator.next());
            }
            return listade;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarDepartamento(IDepartamento disciplina) throws ExcepcaoPersistencia {
        super.delete(disciplina);
    }
    
}
