/*
 * StudentOJB.java
 *
 * Created on 28 December 2002, 17:19
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Ricardo Nortadas
 */

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Student;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import Util.TipoCurso;

public class StudentOJB extends ObjectFenixOJB implements IPersistentStudent {
     
    
     public IStudent readByNumeroAndEstado(Integer numero, Integer estado, TipoCurso degreeType) throws ExcepcaoPersistencia {
        try {
            IStudent aluno = null;
            String oqlQuery = "select all from " + Student.class.getName();
            oqlQuery += " where number = $1 and state = $2 and degreeType = $3";
            query.create(oqlQuery);
            query.bind(numero);
            query.bind(estado);
            query.bind(degreeType.getTipoCurso());
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0)
                aluno = (IStudent) result.get(0);
            return aluno;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	public IStudent readByUsername(String username) throws ExcepcaoPersistencia {
		try {
			IStudent student = null;
			String oqlQuery = "select all from " + Student.class.getName();
			oqlQuery += " where person.username = $1";
			query.create(oqlQuery);
			query.bind(username);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
			student = (IStudent) result.get(0);
			return student;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
	}
}
    
    public IStudent readByNumero (Integer numero, TipoCurso degreeType) throws ExcepcaoPersistencia {
        try {
            IStudent aluno = null;
            String oqlQuery = "select aluno from " + Student.class.getName();
            oqlQuery += " where number = $1 and degreeType = $2";
            query.create(oqlQuery);
            //query.bind(.getNome());
            query.bind(numero);
            query.bind(degreeType.getTipoCurso());            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                aluno = (IStudent) result.get(0);
            return aluno;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
     
    
    public ArrayList readAllAlunos() throws ExcepcaoPersistencia {
        try {
            ArrayList listade = new ArrayList();
            String oqlQuery = "select all from " + Student.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listade.add((IStudent)iterator.next());
            }
            return listade;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void lockWrite(IStudent aluno) throws ExcepcaoPersistencia {
        super.lockWrite(aluno);
    }
    
    
     
    public void delete(IStudent aluno) throws ExcepcaoPersistencia {
        super.delete(aluno);
    }

/*    
     public void deleteByNumeroAndEstado(Integer numero, Integer estado) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Student.class.getName();
            oqlQuery += " where numero = $1 and estado = $2";
            query.create(oqlQuery);
            query.bind(numero);
            query.bind(estado);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while(iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
*/ 
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Student.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public IStudent readByNumeroAndEstadoAndPessoa(Integer numero, Integer estado, IPessoa pessoa, TipoCurso degreeType) throws ExcepcaoPersistencia {
     try {
            IStudent aluno = null;
            String oqlQuery = "select all from " + Student.class.getName();
            oqlQuery += " where number = $1 and state = $2 and person.numeroDocumentoIdentificacao = $3";
            oqlQuery += " and degreeType = $4";
            
            query.create(oqlQuery);
            query.bind(numero);
            query.bind(estado);
            //query.bind(pessoa.getNome());
            query.bind(pessoa.getNumeroDocumentoIdentificacao());
            query.bind(degreeType.getTipoCurso());            
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0)
                aluno = (IStudent) result.get(0);
            return aluno;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    
    
}
