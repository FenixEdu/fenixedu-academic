/*
 * TurmaTurnoOJB.java
 *
 * Created on 19 de Outubro de 2002, 15:23
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.List;

import org.odmg.QueryException;

import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.TurmaTurno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaTurnoPersistente;

public class TurmaTurnoOJB extends ObjectFenixOJB implements ITurmaTurnoPersistente {
   
    public ITurmaTurno readByTurmaAndTurno(ITurma turma, ITurno turno) throws ExcepcaoPersistencia {
        try {
            ITurmaTurno turmaTurno = null;
            String oqlQuery = "select turmaturno from " + TurmaTurno.class.getName();
            oqlQuery += " where turma.nome = $1 and turno.nome = $2";
            query.create(oqlQuery);
            query.bind(turma.getNome());
            query.bind(turno.getNome());
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                turmaTurno = (ITurmaTurno) result.get(0);
            return turmaTurno;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
        
    public void lockWrite(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia {
        super.lockWrite(turmaTurno);
    }
    
    public void delete(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia {
        super.delete(turmaTurno);
    }
    
    public void delete(String nomeTurma, String nomeTurno) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + TurmaTurno.class.getName();
                oqlQuery += " where turma.nome = $1 and turno.nome = $2";
                query.create(oqlQuery);
                query.bind(nomeTurma);
                query.bind(nomeTurno);
                List result = (List) query.execute();
                delete( ((ITurmaTurno)(result.get(0))) );
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + TurmaTurno.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public List readTurnosDeTurma(String nomeTurma) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + TurmaTurno.class.getName();
                oqlQuery += " where turma.nome = $1";
                query.create(oqlQuery);
                query.bind(nomeTurma);
                List result = (List) query.execute();
                lockRead(result);
                
                List finalResult = new ArrayList();
                for(int i = 0; i != result.size(); i++)
                    finalResult.add( ((ITurmaTurno)(result.get(i))).getTurno() );
                
                return finalResult;                
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
      
    public List readAulasByTurma(String nomeTurma) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select turnos from " + TurmaTurno.class.getName();
            oqlQuery += " where turma.nome = $1";
            query.create(oqlQuery);
            query.bind(nomeTurma);
            List result = (List) query.execute();
            lockRead(result);
            
            List finalResult = new ArrayList();
            for(int i = 0; i != result.size(); i++) {
                oqlQuery = "select aulas from " + TurnoAula.class.getName();
                oqlQuery += " where turno.nome = $1";
				oqlQuery += " and turno.disciplinaExecucao.sigla = $2";
				oqlQuery += " and turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $3";
				oqlQuery += " and turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4";
				
                
                query.create(oqlQuery);
                query.bind(((ITurmaTurno)(result.get(i))).getTurno().getNome());
				query.bind(((ITurmaTurno)(result.get(i))).getTurno().getDisciplinaExecucao().getSigla());
				query.bind(((ITurmaTurno)(result.get(i))).getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo());
				query.bind(((ITurmaTurno)(result.get(i))).getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla());
				
                List resultTmp = (List) query.execute();
                lockRead(resultTmp);
                
                for(int j = 0; j != resultTmp.size(); j++) {
                    IAula xpto = ((ITurnoAula)(resultTmp.get(j))).getAula();
                                        finalResult.add(xpto);
                }
            }
            
            return finalResult;                
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	public List readClassesWithShift(ITurno turno) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + TurmaTurno.class.getName()
					 + " where turno.nome = $1"
					 + " and  turno.disciplinaExecucao.sigla = $2"
					 + " and  turno.disciplinaExecucao.executionPeriod.name = $3"
					 + " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $4";
			query.create(oqlQuery);
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());			
			query.bind(turno.getDisciplinaExecucao().getExecutionPeriod().getName());
			query.bind(turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());			
			
			List classesList = (List) query.execute();
			lockRead(classesList);

			return classesList;                
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
