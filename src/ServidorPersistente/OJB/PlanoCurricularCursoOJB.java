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

import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;
import Dominio.PlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPlanoCurricularCursoPersistente;

public class PlanoCurricularCursoOJB extends ObjectFenixOJB implements IPlanoCurricularCursoPersistente {
    
    public PlanoCurricularCursoOJB() {
    }  
    
    public void apagarTodosOsPlanosCurriculares() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public void escreverPlanoCurricular(IPlanoCurricularCurso planoCurricularCurso) throws ExcepcaoPersistencia {
        super.lockWrite(planoCurricularCurso);
    }
    
    public IPlanoCurricularCurso lerPlanoCurricularPorNomeESigla(String nome, String sigla) throws ExcepcaoPersistencia {
        try {
            IPlanoCurricularCurso de = null;
            String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
            oqlQuery += " where nome = $1 and sigla = $2";
            query.create(oqlQuery);
            query.bind(nome);
            query.bind(sigla);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0)
                de = (IPlanoCurricularCurso) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public IPlanoCurricularCurso lerPlanoCurricularPorNomeSiglaCurso(String nome, String sigla, ICurso curso) throws ExcepcaoPersistencia {
        try {
            IPlanoCurricularCurso de = null;
            String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
            oqlQuery += " where nome = $1 and sigla = $2 and curso.sigla = $3";
            query.create(oqlQuery);
            query.bind(nome);
            query.bind(sigla);
            query.bind(curso.getSigla());

            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0)
                de = (IPlanoCurricularCurso) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarPlanoCurricularPorNomeESigla(String nome, String sigla) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
            oqlQuery += " where nome = $1 and sigla = $2";
            query.create(oqlQuery);
            query.bind(nome);
            query.bind(sigla);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while(iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public ArrayList lerTodosOsPlanosCurriculares() throws ExcepcaoPersistencia {
        try {
            ArrayList listade = new ArrayList();
            String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listade.add((IPlanoCurricularCurso)iterator.next());
            }
            return listade;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarPlanoCurricular(IPlanoCurricularCurso planoCurricularCurso) throws ExcepcaoPersistencia {
        super.delete(planoCurricularCurso);
    }
}
