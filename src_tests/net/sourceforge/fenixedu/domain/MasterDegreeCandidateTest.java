package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;


public class MasterDegreeCandidateTest extends DomainTestBase {

    MasterDegreeCandidate masterDegreeCandidate;
    ExecutionDegree executionDegree;
    DegreeCurricularPlan degreeCurricularPlan;
    Degree degree;
    Specialization specialization;
    
    Person person1;
    Person person2;
    Person person3;
    
    List<Person> onePerson;
    List<Person> twoPersons;
    List<Person> allPersons;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        degree = new Degree();
        degree.setSigla("abc");
        degreeCurricularPlan = new DegreeCurricularPlan();
        degreeCurricularPlan.setDegree(degree);
        executionDegree = degreeCurricularPlan.createExecutionDegree(null, null, false);
        specialization = Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE;
        masterDegreeCandidate = new MasterDegreeCandidate();
        masterDegreeCandidate.setExecutionDegree(executionDegree);
        masterDegreeCandidate.setSpecialization(specialization);
        masterDegreeCandidate.setCandidateNumber(1);
        
        person1 = new Person();
        person1.setUsername(RoleType.MASTER_DEGREE_CANDIDATE);
        
        person2 = new Person();
        person2.setUsername(RoleType.MASTER_DEGREE_CANDIDATE);
        
        person3 = new Person();
        person3.setUsername(RoleType.MASTER_DEGREE_CANDIDATE);
        
        onePerson = new ArrayList<Person>();
        onePerson.add(person1);
        
        twoPersons = new ArrayList<Person>();
        twoPersons.add(person1);
        twoPersons.add(person2);
        
        allPersons = new ArrayList<Person>();
        allPersons.add(person1);
        allPersons.add(person2);
        allPersons.add(person3);
        
    }

}
