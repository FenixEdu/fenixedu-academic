/*
 * PessoaOJB.java
 * 
 * Created on 15 de Outubro de 2002, 15:16
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 * 
 */
public class PessoaOJB extends PersistentObjectOJB implements IPessoaPersistente {

    public IPerson lerPessoaPorUsername(String username) throws ExcepcaoPersistencia {
        IPerson person = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("username", username);
        person = (IPerson) queryObject(Person.class, criteria);
        return person;
    }

    public List<IPerson> findPersonByName(String name) throws ExcepcaoPersistencia {
        List<IPerson> personList = null;

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        personList = queryList(Person.class, criteria);
        return personList;
    }

    public List<IPerson> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);

        if (startIndex != null && numberOfElementsInSpan != null) {
            return readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex);
        }
        return queryList(Person.class, criteria);

    }

    public Integer countAllPersonByName(String name) {
        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        return new Integer(count(Person.class, criteria));
    }

    public IPerson lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
        criteria.addEqualTo("idDocumentType", tipoDocumentoIdentificacao);
        return (IPerson) queryObject(Person.class, criteria);
    }

    /*
     * This method return a list with the elents returned by the search.
     */
    public List<IPerson> readActivePersonByNameAndEmailAndUsernameAndDocumentId(String name,
            String email, String username, String documentIdNumber, Integer startIndex,
            Integer numberOfElementsInSpan,IRole role,IDegree degree,DegreeType degreeType,IDepartment department) throws ExcepcaoPersistencia {

        List<IPerson> personsList = null;
    	Boolean groupBy = null;
    	Criteria criteria = new Criteria();
        Criteria criteria2 = new Criteria();
        
        if (name != null && name.length() > 0) {
            criteria.addLike("nome", name);
            criteria2.addLike("nome", name);
        }
        if (email != null && email.length() > 0) {
            criteria.addLike("email", email);
            criteria2.addLike("email", email);
        }
        if (username != null && username.length() > 0) {
            criteria.addLike("username", username);
            
        }

        if (role != null) {
        	if (role.getRoleType().equals(RoleType.EMPLOYEE)){
        		criteria.addLike("username", "F%");
        	}
        	if (role.getRoleType().equals(RoleType.TEACHER)){
        		criteria.addLike("username", "D%");
        	}
        	if (role.getRoleType().equals(RoleType.STUDENT)){
        		if(degree == null && degreeType == null){
	        		criteria.addLike("username", "L%");
	        		criteria2.addLike("username", "M%");
        		}else if(degree == null && degreeType != null){
        			if(degreeType.equals(degreeType.DEGREE)){
        				criteria.addLike("username", "L%");
        			}else{
        				criteria.addLike("username", "M%");
        			}
        			
        		}
        		
        		
        	}
        	if (role.getRoleType().equals(RoleType.GRANT_OWNER)){
        		criteria.addLike("username", "B%");
        	}

        	groupBy=false;
        	 
        }
        if(degree != null){
    		criteria.addEqualTo("students.studentCurricularPlans.degreeCurricularPlan.degree.idInternal", degree.getIdInternal());
    		criteria.addEqualTo("students.degreeType",degree.getTipoCurso());
    		
    		List<StudentCurricularPlanState> currentStateList =new ArrayList<StudentCurricularPlanState>();
    		currentStateList.add(StudentCurricularPlanState.ACTIVE);
    		currentStateList.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);
    		
    		criteria.addIn("students.studentCurricularPlans.currentState", currentStateList);
    		
  
    		if(degree.getTipoCurso().equals(DegreeType.DEGREE) ){
    			criteria.addLike("username", "L%");
    		}else if(degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE)){
    			criteria.addLike("username", "M%");
    		}
    		groupBy = true;
    	}
        
        
        criteria.addNotLike("username", "INA%");
        criteria2.addNotLike("username", "INA%");
        
        if (documentIdNumber != null && documentIdNumber.length() > 0) {
            criteria.addLike("numeroDocumentoIdentificacao", documentIdNumber);
            criteria2.addLike("numeroDocumentoIdentificacao", documentIdNumber);
            
        }
        if (role != null ){
        	if(role.getRoleType().equals(RoleType.STUDENT) && degree == null && degreeType == null){
        		criteria.addOrCriteria(criteria2);
        	}	
        }
        if (startIndex == null && numberOfElementsInSpan == null) {
            personsList = queryList(Person.class, criteria, "nome", false);
        } else if (startIndex != null && numberOfElementsInSpan != null) {
            personsList = readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex,
                    "nome", true,groupBy);
        }
        return personsList;
    }
    
   
    
    

    public Integer countActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer startIndex,IRole role, IDegree degree,DegreeType degreeType,IDepartment department) {
    	Boolean groupBy = null;
    	Criteria criteria = new Criteria();
        Criteria criteria2 = new Criteria();
        
        if (name != null && name.length() > 0) {
            criteria.addLike("nome", name);
            criteria2.addLike("nome", name);
        }
        if (email != null && email.length() > 0) {
            criteria.addLike("email", email);
            criteria2.addLike("email", email);
        }
        if (username != null && username.length() > 0) {
            criteria.addLike("username", username);
            
        }

        if (role != null) {
        	if (role.getRoleType().equals(RoleType.EMPLOYEE)){
        		criteria.addLike("username", "F%");
        	}
        	if (role.getRoleType().equals(RoleType.TEACHER)){
        		criteria.addLike("username", "D%");
        	}
        	if (role.getRoleType().equals(RoleType.STUDENT)){
        	      
        		if(degree == null && degreeType == null){
        			
	        		criteria.addLike("username", "L%");
	        		criteria2.addLike("username", "M%");
        		}else if(degree == null && degreeType != null){
        			
        			if(degreeType.equals(DegreeType.DEGREE)){
        				criteria.addLike("username", "L%");
        			}else{
        				criteria.addLike("username", "M%");
        			}
        			
        		}
        		
        		
        	}
        	if (role.getRoleType().equals(RoleType.GRANT_OWNER)){
        		criteria.addLike("username", "B%");
        	}
        	//criteria.addEqualTo("personRoles.roleType", role.getRoleType());
        	groupBy=false;
        	 
        }
        if(degree != null){
        	criteria.addEqualTo("students.studentCurricularPlans.degreeCurricularPlan.degree.idInternal", degree.getIdInternal());
    		criteria.addEqualTo("students.degreeType",degree.getTipoCurso());
    		
    		List<StudentCurricularPlanState> currentStateList =new ArrayList<StudentCurricularPlanState>();
    		currentStateList.add(StudentCurricularPlanState.ACTIVE);
    		currentStateList.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);
    		criteria.addIn("students.studentCurricularPlans.currentState", currentStateList);
    		
    		if(degree.getTipoCurso().equals(DegreeType.DEGREE) ){
    			criteria.addLike("username", "L%");
    		}else if(degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE)){
    			criteria.addLike("username", "M%");
    		}
    		groupBy = true;
    	}
        
        
        criteria.addNotLike("username", "INA%");
        criteria2.addNotLike("username", "INA%");
        
        if (documentIdNumber != null && documentIdNumber.length() > 0) {
            criteria.addLike("numeroDocumentoIdentificacao", documentIdNumber);
            criteria2.addLike("numeroDocumentoIdentificacao", documentIdNumber);
            
        }
        if (role != null ){
	        if (role.getRoleType().equals(RoleType.STUDENT) && degree == null && degreeType == null){
	        	criteria.addOrCriteria(criteria2);
	        }
        }
        
        return new Integer(count(Person.class, criteria ,true));
    }

    public List<IPerson> readPersonsBySubName(String subName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addLike("name", subName);
        return queryList(Person.class, criteria);
    }

    public Collection<IPerson> readByIdentificationDocumentNumber(String identificationDocumentNumber)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("numeroDocumentoIdentificacao", identificationDocumentNumber);
        return queryList(Person.class, criteria);
    }
    
    public List<IPerson> PersonByDepartment(String name,List<ITeacher> teachers,Integer startIndex,Integer numberOfElementsInSpan) throws ExcepcaoPersistencia {
        List<IPerson> personsList = null;
        
        Collection teacherList = CollectionUtils.collect(teachers, new Transformer() {
            public Object transform(Object input) {
                ITeacher teacher = (ITeacher) input;
                return teacher.getIdInternal();
            }
        });
        
        Criteria criteria = new Criteria();
        if (name != null && name.length() > 0) {
            criteria.addLike("nome", name);
        }
        criteria.addLike("username", "D%");

        criteria.addIn("keyTeacher", teacherList);
        if (startIndex == null && numberOfElementsInSpan == null) {
            personsList = queryList(Person.class, criteria, "nome", false);
        } else if (startIndex != null && numberOfElementsInSpan != null) {
            personsList = readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex,
                    "nome", true,false);
        }
        return personsList;
    }
    
    public Integer CountPersonByDepartment(String name,List<ITeacher> teacher,Integer startIndex,Integer numberOfElementsInSpan) throws ExcepcaoPersistencia {
        List<IPerson> personsList = null;
        
        Collection teacherList = CollectionUtils.collect(teacher, new Transformer() {
            public Object transform(Object input) {
                ITeacher teacher = (ITeacher) input;
                return teacher.getIdInternal();
            }
        });
        Criteria criteria = new Criteria();
        if (name != null && name.length() > 0) {
            criteria.addLike("nome", name);
        }
        criteria.addLike("username", "D%");
        criteria.addIn("keyTeacher", teacherList);
        return new Integer(count(Person.class, criteria ,true));

    }
}