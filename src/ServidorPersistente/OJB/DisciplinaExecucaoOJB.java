/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;

public class DisciplinaExecucaoOJB extends ObjectFenixOJB implements IDisciplinaExecucaoPersistente {
    
    public DisciplinaExecucaoOJB() {
    }
    
    public boolean apagarTodasAsDisciplinasExecucao() {
        try {
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            super.deleteAll(oqlQuery);
            return true;
        } catch(ExcepcaoPersistencia ex){
            return false;
        }
    }
    
    public boolean escreverDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao) {
        try {
            super.lockWrite(disciplinaExecucao);
            return true;
        } catch(ExcepcaoPersistencia ex){
            return false;
        }
    }
    
    public IDisciplinaExecucao lerDisciplinaExecucao(int chaveLicenciaturaExecucao, String sigla) {
        try {
            IDisciplinaExecucao de = null;
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            oqlQuery += " where sigla = $1 and chaveLicenciaturaExecucao = $2";
            query.create(oqlQuery);
            query.bind(sigla);
            query.bind(new Integer(chaveLicenciaturaExecucao));
            List result = (List) query.execute();
            try{
                lockRead(result);
            } catch (ExcepcaoPersistencia ex) {
                return null;
            }
            if (result.size() != 0)
                de = (IDisciplinaExecucao) result.get(0);
            return de;
        } catch (QueryException ex) {
            return null;
        }
    }
    
    public boolean apagarDisciplinaExecucao(int chaveLicenciaturaExecucao, String sigla) {
        try {
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            oqlQuery += " where sigla = $1 and chaveLicenciaturaExecucao = $2";
            query.create(oqlQuery);
            query.bind(sigla);
            query.bind(new Integer(chaveLicenciaturaExecucao));
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            try {
                while (iterator.hasNext())
                    super.delete(iterator.next());
            } catch(ExcepcaoPersistencia ex){
                return false;
            }
            return true;
        } catch (QueryException ex) {
            return false;
        }
    }
    
    public ArrayList lerTodasDisciplinaExecucao() {
        try {
            ArrayList listade = new ArrayList();
            String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            try{
                lockRead(result);
            } catch (ExcepcaoPersistencia ex) {
                return null;
            }
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listade.add((IDisciplinaExecucao)iterator.next());
            }
            return listade;
        } catch (QueryException ex) {
            return null;
        }
    }

    public IDisciplinaExecucao readBySiglaAndAnoLectivAndSiglaLicenciatura(String sigla, String anoLectivo, String siglaLicenciatura)
                 throws ExcepcaoPersistencia {
        try {
            IDisciplinaExecucao disciplinaExecucao = null;
            String oqlQuery = "select  disciplinaExecucao  from " + DisciplinaExecucao.class.getName();
            oqlQuery += " where sigla = $1";
            oqlQuery += " and licenciaturaExecucao.anoLectivo = $2";
            oqlQuery += " and licenciaturaExecucao.curso.sigla = $3";
            query.create(oqlQuery);
            query.bind(sigla);
            query.bind(anoLectivo);
            query.bind(siglaLicenciatura);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                disciplinaExecucao = (IDisciplinaExecucao) result.get(0);
            return disciplinaExecucao;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

  	public List readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(Integer anoCurricular,
	                                                                 String anoLectivo,
	                                                                 Integer semestre,
	                                                                 String siglaLicenciatura)
	            throws ExcepcaoPersistencia{

//		ICurricularCourse iC = new CurricularCourse();
//		iC.setSemester(semestre);
//		iC.setCurricularYear(anoCurricular);
//		System.out.println("Read marado 1");	            	
////		Criteria c2 = new Criteria();
////		c2.addEqualTo("semester",semestre);
////		c2.addEqualTo("curricularYear",anoCurricular);
////		System.out.println("Read marado 2");
////		System.out.println("query = " + c2.toString());
//		List dcList = null;
////		dcList = readByCriteria(CurricularCourse.class, c2);
//		dcList = readByCriteria(iC);
//		System.out.println("Read marado 4");
//	    Criteria c = new Criteria();
//        c.addEqualTo("licenciaturaExecucao.curso.sigla",siglaLicenciatura);
//        c.addEqualTo("licenciaturaExecucao.anoLectivo", anoLectivo);
//	    //c.addIn("associatedCurricularCourses", dcList);
//	    System.out.println("Read marado 5");
//		System.out.println("query = " + c.toString());
//		List result = null;
//		result = readByCriteria(DisciplinaExecucao.class, c);
//
//	    System.out.println("query = " + c.toString());
//
//	     
//	return result;
	            	
        try {



            String oqlQuery = "select distinct all from " + DisciplinaExecucao.class.getName();
            oqlQuery += " where licenciaturaExecucao.curso.sigla = $1";
            oqlQuery += " and licenciaturaExecucao.anoLectivo = $2";
            oqlQuery += " and semester = $3 ";


//            oqlQuery += " and disciplinaCurricular.semestre = $3";
//            oqlQuery += " and disciplinaCurricular.anoCurricular = $4";
            query.create(oqlQuery);
            query.bind(siglaLicenciatura);
            query.bind(anoLectivo);
            query.bind(semestre);
//            query.bind(semestre);
//            query.bind(anoCurricular);

            List result = (List) query.execute();
            
            Iterator iterator = result.listIterator();
            
            List resultList = new LinkedList();
            lockRead(result);
            
            while(iterator.hasNext()) {
            	IDisciplinaExecucao disciplinaExecucao = (IDisciplinaExecucao) iterator.next();
            	Iterator iterator2 = disciplinaExecucao.getAssociatedCurricularCourses().iterator();
            	while(iterator2.hasNext()) {
            		ICurricularCourse curricularCourse = (ICurricularCourse) iterator2.next();
            		//if ((curricularCourse.getSemester().equals(semestre)) &&			
            		if	(curricularCourse.getCurricularYear().equals(anoCurricular)) //)
            			resultList.add(disciplinaExecucao);
            			break;
            	}
            }
            return resultList;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
	}



}
