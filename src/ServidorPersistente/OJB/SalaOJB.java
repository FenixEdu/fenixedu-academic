/*
 * SalaOJB.java
 *
 * Created on 21 de Agosto de 2002, 16:36
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.IAula;
import Dominio.ISala;
import Dominio.Sala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;

public class SalaOJB extends ObjectFenixOJB implements ISalaPersistente {
   
    public ISala readByNome(String nome) throws ExcepcaoPersistencia {
        try {
            ISala sala = null;
            String oqlQuery = "select salanome from " + Sala.class.getName();
            oqlQuery += " where nome = $1";
            query.create(oqlQuery);
            query.bind(nome);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                sala = (ISala) result.get(0);
            return sala;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
        
    public void lockWrite(ISala sala) throws ExcepcaoPersistencia {
        super.lockWrite(sala);
    }
    
    public void delete(ISala sala) throws ExcepcaoPersistencia {
		try {
					IAula aula = null;
					String oqlQuery = "select all from " + Aula.class.getName();
					oqlQuery += " where sala.nome = $1";
					query.create(oqlQuery);
					query.bind(sala.getNome());
					List result = (List) query.execute();
					lockRead(result);
					if (result.size() != 0) {
					throw new ExcepcaoPersistencia("Não é possível apagar salas com aulas associadas");
					}
					
				} catch (QueryException ex) {
					throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
				}
        super.delete(sala);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Sala.class.getName();
        super.deleteAll(oqlQuery);
    }

    public List readAll() throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Sala.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	/**
	 * Reads all salas that with certains properties. The properties are
	 * specified by the arguments of this method. If an argument is
	 * null, then the sala can have any value concerning that
	 * argument. In what concerns the capacidadeNormal and
	 * capacidadeExame, this two arguments specify the minimal value that a sala
	 * can have in order to be selected.
	 *
	 * @return a list with all salas that satisfy the conditions specified by the 
	 * non-null arguments.
	 **/  
	public List readSalas(String nome, String edificio, Integer piso, Integer tipo,
			  Integer capacidadeNormal, Integer capacidadeExame)
	  throws ExcepcaoPersistencia {
	  if (nome == null && edificio == null && piso == null && tipo == null &&
	  capacidadeExame == null && capacidadeNormal == null)
		return readAll();

	  try {
		StringBuffer oqlQuery = new StringBuffer("select sala from ");
		boolean hasPrevious = false;

		oqlQuery.append(Sala.class.getName()).append(" where ");
		if (nome != null) {
	  hasPrevious = true;
	  oqlQuery.append("nome = \"").append(nome).append("\"");
		}

		if (edificio != null) {
	  if (hasPrevious)
		oqlQuery.append(" and ");
	  else
		hasPrevious = true;

	  oqlQuery.append(" edificio = \"").append(edificio).append("\"");
		}

		if (piso != null) {
	  if (hasPrevious)
		oqlQuery.append(" and ");
	  else
		hasPrevious = true;

	  oqlQuery.append(" piso = \"").append(piso).append("\"");
		}

		if (tipo != null) {
	  if (hasPrevious)
		oqlQuery.append(" and ");
	  else
		hasPrevious = true;

	  oqlQuery.append(" tipo = \"").append(tipo).append("\"");
		}

		if (capacidadeNormal != null) {
	  if (hasPrevious)
		oqlQuery.append(" and ");
	  else
		hasPrevious = true;

	  oqlQuery.append(" capacidadeNormal > \"").append(capacidadeNormal.intValue() - 1).append("\"");
		}

		if (capacidadeExame != null) {
	  if (hasPrevious)
		oqlQuery.append(" and ");
	  else
		hasPrevious = true;

	  oqlQuery.append(" capacidadeExame > \"").append(capacidadeExame.intValue() - 1).append("\"");
		}

		query.create(oqlQuery.toString());
		List result = (List) query.execute();
		lockRead(result);
		return result;
	  } catch (QueryException ex) {
		throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
	  }
	}
    

	

}
