package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class SearchPerson implements IService {

	/*
     * This service return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List run(HashMap searchParameters) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPersistentRole persistentRole = sp.getIPersistentRole();
        ICursoPersistente persistentDegree = sp.getICursoPersistente();
        IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
        
        String name = (String) searchParameters.get(new String("name"));
        String email = (String) searchParameters.get(new String("email"));
        String username = (String) searchParameters.get(new String("username"));
        String documentIdNumber = (String) searchParameters.get(new String("documentIdNumber"));
        Integer startIndex = (Integer) searchParameters.get(new String("startIndex"));
        Integer numberOfElementsInSpan = (Integer) searchParameters.get(new String("numberOfElements"));
        String roleType = (String) searchParameters.get(new String("roleType"));
        String degreeType = (String) searchParameters.get(new String("degreeType"));
        Integer degreeId = (Integer) searchParameters.get(new String("degreeId"));
        Integer departmentId= (Integer) searchParameters.get(new String("departmentId"));
        IRole roleBd = null;
        IDegree degree = null;
        IDepartment department = null;
        DegreeType degreetype = null;
        List<IPerson> persons = new ArrayList<IPerson>();
        Integer totalPersons= null;
        
        if (roleType != null && roleType.length() > 0){
        	roleBd = persistentRole.readByRoleType(RoleType.valueOf(roleType));	
        }
      
        if(degreeId != null){
		 degree = (IDegree)persistentDegree.readByOID(Degree.class,degreeId);
        }
        if (degreeType != null && degreeType.length() > 0){
        	degreetype = DegreeType.valueOf(degreeType);
        }
   
    	if(departmentId != null){
     		 department = (IDepartment)persistentDepartment.readByOID(Department.class,departmentId);
        }
		if(department != null){		    
	        List<ITeacher> teachers = department.getTeachers();
	        persons = persistentPerson.PersonByDepartment(name,teachers,startIndex,numberOfElementsInSpan);
	        totalPersons = persistentPerson.CountPersonByDepartment(name,teachers,startIndex,numberOfElementsInSpan);
	    }else{
	        persons = persistentPerson.readActivePersonByNameAndEmailAndUsernameAndDocumentId(name, email,
	                    username, documentIdNumber, startIndex, numberOfElementsInSpan,roleBd,degree,degreetype,department);
	       
	
	        totalPersons = persistentPerson.countActivePersonByNameAndEmailAndUsernameAndDocumentId(name, email,
	                username, documentIdNumber, startIndex,roleBd,degree,degreetype,department);
	    
	    }

  
        List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();

        for (IPerson person : persons) {
            	infoPersons.add(InfoPerson.newInfoFromDomain(person));
        }
        
        
       
        List<Object> result = new ArrayList<Object>(2);
        result.add(totalPersons);
        result.add(infoPersons);

        return result;
    }
}