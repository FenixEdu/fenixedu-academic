package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;


public class MasterDegreeCandidateTest extends DomainTestBase {

    IMasterDegreeCandidate masterDegreeCandidate;
    IExecutionDegree executionDegree;
    IDegreeCurricularPlan degreeCurricularPlan;
    IDegree degree;
    Specialization specialization;
    
    IPerson person1;
    IPerson person2;
    IPerson person3;
    
    List<IPerson> onePerson;
    List<IPerson> twoPersons;
    List<IPerson> allPersons;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        degree = new Degree();
        degree.setSigla("abc");
        degreeCurricularPlan = new DegreeCurricularPlan();
        degreeCurricularPlan.setDegree(degree);
        executionDegree = new ExecutionDegree();
        executionDegree.setDegreeCurricularPlan(degreeCurricularPlan);
        specialization = Specialization.MASTER_DEGREE;
        masterDegreeCandidate = new MasterDegreeCandidate();
        masterDegreeCandidate.setExecutionDegree(executionDegree);
        masterDegreeCandidate.setSpecialization(specialization);
        masterDegreeCandidate.setCandidateNumber(1);
        
        person1 = new Person();
        person1.setUsername("1MASabc");
        
        person2 = new Person();
        person2.setUsername("1MASabc1");
        
        person3 = new Person();
        person3.setUsername("1MASabc2");
        
        onePerson = new ArrayList();
        onePerson.add(person1);
        
        twoPersons = new ArrayList();
        twoPersons.add(person1);
        twoPersons.add(person2);
        
        allPersons = new ArrayList();
        allPersons.add(person1);
        allPersons.add(person2);
        allPersons.add(person3);
        
    }


    public void testGenerateUsername() {
        
        String username;
        
        username = MasterDegreeCandidate.generateUsernameForNewCandidate(masterDegreeCandidate, new ArrayList());
        assertEquals(username, "1MASabc");
        
        username = MasterDegreeCandidate.generateUsernameForNewCandidate(masterDegreeCandidate, onePerson);
        assertEquals(username, "1MASabc1");
        
        username = MasterDegreeCandidate.generateUsernameForNewCandidate(masterDegreeCandidate, twoPersons);
        assertEquals(username, "1MASabc2");
        
        username = MasterDegreeCandidate.generateUsernameForNewCandidate(masterDegreeCandidate, allPersons);
        assertEquals(username, "1MASabc3");
        
    }

}
