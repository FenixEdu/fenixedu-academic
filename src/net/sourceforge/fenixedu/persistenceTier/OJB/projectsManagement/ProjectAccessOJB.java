/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.projectsManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Susana Fernandes
 */
public class ProjectAccessOJB extends PersistentObjectOJB implements IPersistentProjectAccess {

    public ProjectAccessOJB() {
    }

    public ProjectAccess readByPersonIdAndProjectAndDate(Integer personId, Integer keyProject) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", personId);
        criteria.addEqualTo("keyProject", keyProject);
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        return (ProjectAccess) queryObject(ProjectAccess.class, criteria);
    }

    public List<ProjectAccess> readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        return queryList(ProjectAccess.class, criteria);
    }

    public List<ProjectAccess> readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        return queryList(ProjectAccess.class, criteria);
    }

    public List<Integer> readProjectCodesByPersonUsernameAndDateAndCC(String username, String cc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        if (cc != null && !cc.equals("")) {
            criteria.addEqualTo("keyProjectCoordinator", cc);
            criteria.addEqualTo("costCenter", true);
        } else
            criteria.addEqualTo("costCenter", false);
        List<ProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (ProjectAccess projectAccess : allPersonProjects)
            result.add(projectAccess.getKeyProject());
        return result;
    }

    public List<Integer> readCCCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        criteria.addEqualTo("costCenter", true);
        List<ProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (ProjectAccess projectAccess : allPersonProjects)
            if (!result.contains(projectAccess.getKeyProjectCoordinator()))
                result.add(projectAccess.getKeyProjectCoordinator());
        return result;
    }

    public List<Integer> readProjectCodesByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        if (!all)
            criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        List<ProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (ProjectAccess projectAccess : allPersonProjects)
            result.add(projectAccess.getKeyProject());
        return result;
    }

    public List<Integer> readCoordinatorsCodesByPersonUsernameAndDatesAndCC(String username, String cc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        if (cc != null && !cc.equals("")) {
            criteria.addEqualTo("keyProjectCoordinator", cc);
            criteria.addEqualTo("costCenter", true);
        } else
            criteria.addEqualTo("costCenter", false);
        List<ProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (ProjectAccess projectAccess : allPersonProjects)
            if (!result.contains(projectAccess.getKeyProjectCoordinator()))
                result.add(projectAccess.getKeyProjectCoordinator());
        return result;
    }

    public boolean hasPersonProjectAccess(String personUsername, Integer projectCode) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", personUsername);
        criteria.addEqualTo("keyProject", projectCode);
        criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        if (count(ProjectAccess.class, criteria) == 0)
            return false;
        return true;

    }

    public int countByPersonAndProject(Person person, Integer keyProject) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addEqualTo("keyProject", keyProject);
        criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        return count(ProjectAccess.class, criteria);
    }

    public int countByPersonAndCC(Person person, Boolean cc) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addLessOrEqualThan("begin", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("end", Calendar.getInstance().getTime());
        criteria.addEqualTo("costCenter", cc);
        return count(ProjectAccess.class, criteria);
    }

    public void deleteByPersonAndCC(Person person, Boolean cc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addEqualTo("costCenter", cc);
        List<ProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        for (ProjectAccess projectAccess : allPersonProjects) {
            delete(projectAccess);
        }
    }

    public void deleteByPersonAndDate(Person person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addLessOrEqualThan("end", Calendar.getInstance().getTime());
        List<ProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        for (ProjectAccess projectAccess : allPersonProjects) {
        	projectAccess.removePerson();
            delete(projectAccess);
        }
    }

    public void delete(ProjectAccess projectAccess) throws ExcepcaoPersistencia {
        super.delete(projectAccess);
    }
}