/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IPlanoCurricularCurso;
import Dominio.PlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class PlanoCurricularCursoOJB extends ObjectFenixOJB implements IPlanoCurricularCursoPersistente {
    
    public PlanoCurricularCursoOJB() {
    }  
    
    public void apagarTodosOsPlanosCurriculares() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			Iterator iter = result.iterator();
			while (iter.hasNext()){
				IPlanoCurricularCurso curricularPlan= (IPlanoCurricularCurso) iter.next();
				delete(curricularPlan);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia();
			
		}
    }
    
	public void escreverPlanoCurricular(IPlanoCurricularCurso degreeCurricularPlanToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IPlanoCurricularCurso degreeCurricularPlanFromDB = null;

		// If there is nothing to write, simply return.
		if (degreeCurricularPlanToWrite == null)
			return;

		// Read degreeCurricularPlan from database.
		degreeCurricularPlanFromDB =
			this.readByNameAndDegree(
				degreeCurricularPlanToWrite.getName(),
				degreeCurricularPlanToWrite.getCurso());

		// If degreeCurricularPlan is not in database, then write it.
		if (degreeCurricularPlanFromDB == null)
			super.lockWrite(degreeCurricularPlanToWrite);
		// else If the degreeCurricularPlan is mapped to the database, then write any existing changes.
		else if (
			(degreeCurricularPlanToWrite instanceof PlanoCurricularCurso)
				&& ((PlanoCurricularCurso) degreeCurricularPlanFromDB).getCodigoInterno().equals(
					((PlanoCurricularCurso) degreeCurricularPlanToWrite).getCodigoInterno())) {
			super.lockWrite(degreeCurricularPlanToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
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
		try {
		  // Delete all Execution Degree
		  CursoExecucaoOJB executionDegreeOJB = new CursoExecucaoOJB();
		  String oqlQuery = "select all from " + CursoExecucao.class.getName();
		  oqlQuery += " where curricularPlan.name = $1 ";
		  oqlQuery += " and curricularPlan.curso.sigla = $2 ";
		  query.create(oqlQuery);
		  query.bind(planoCurricularCurso.getName());
		  query.bind(planoCurricularCurso.getCurso().getSigla());
		  List result = (List) query.execute();
		  Iterator iter = result.iterator();
		  while(iter.hasNext()){
			  ICursoExecucao executionDegree = (ICursoExecucao) iter.next();
			  executionDegreeOJB.delete(executionDegree);
		  }
		  
		  // Delete All Curricular Courses
		  CurricularCourseOJB curricularCourseOJB = new CurricularCourseOJB();
		  oqlQuery = "select all from " + CurricularCourse.class.getName();
		  oqlQuery += " where degreeCurricularPlan.name = $1 ";
		  oqlQuery += " and degreeCurricularPlan.curso.sigla = $2 ";
		  query.create(oqlQuery);
		  query.bind(planoCurricularCurso.getName());
		  query.bind(planoCurricularCurso.getCurso().getSigla());
		  result = (List) query.execute();
		  iter = result.iterator();
		  while(iter.hasNext()){
			  ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
			  curricularCourseOJB.delete(curricularCourse);
		  }
		  
		  super.delete(planoCurricularCurso);
	  } catch (Exception e) {
		  e.printStackTrace();
	  throw new ExcepcaoPersistencia();
	  }
    }
    
    
    public IPlanoCurricularCurso readByNameAndDegree(String name, ICurso degree)throws ExcepcaoPersistencia{
		try {
			IPlanoCurricularCurso de = null;

			String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
			oqlQuery += " where name = $1 "
					 + " and curso.sigla = $2 ";
			
			query.create(oqlQuery);
			query.bind(name);
			query.bind(degree.getSigla());

			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				de = (IPlanoCurricularCurso) result.get(0);
			return de;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
    	
    }
}
