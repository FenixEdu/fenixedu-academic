/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.projectsManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
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

    public IProjectAccess readByPersonIdAndProjectAndDate(Integer personId, Integer keyProject) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", personId);
        criteria.addEqualTo("keyProject", keyProject);
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return (IProjectAccess) queryObject(ProjectAccess.class, criteria);
    }

    public List<IProjectAccess> readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return queryList(ProjectAccess.class, criteria);
    }

    public List<IProjectAccess> readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return queryList(ProjectAccess.class, criteria);
    }

    public List<Integer> readProjectCodesByPersonUsernameAndDateAndCC(String username, String cc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        if (cc != null && !cc.equals("")) {
            criteria.addEqualTo("keyProjectCoordinator", cc);
            criteria.addEqualTo("costCenter", true);
        } else
            criteria.addEqualTo("costCenter", false);
        List<IProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (IProjectAccess projectAccess : allPersonProjects)
            result.add(projectAccess.getKeyProject());
        return result;
    }

    public List<Integer> readCCCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        criteria.addEqualTo("costCenter", true);
        List<IProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (IProjectAccess projectAccess : allPersonProjects)
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
            criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        List<IProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (IProjectAccess projectAccess : allPersonProjects)
            result.add(projectAccess.getKeyProject());
        return result;
    }

    public List<Integer> readCoordinatorsCodesByPersonUsernameAndDatesAndCC(String username, String cc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        if (cc != null && !cc.equals("")) {
            criteria.addEqualTo("keyProjectCoordinator", cc);
            criteria.addEqualTo("costCenter", true);
        } else
            criteria.addEqualTo("costCenter", false);
        List<IProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        List<Integer> result = new ArrayList<Integer>();
        for (IProjectAccess projectAccess : allPersonProjects)
            if (!result.contains(projectAccess.getKeyProjectCoordinator()))
                result.add(projectAccess.getKeyProjectCoordinator());
        return result;
    }

    public boolean hasPersonProjectAccess(String personUsername, Integer projectCode) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", personUsername);
        criteria.addEqualTo("keyProject", projectCode);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        if (count(ProjectAccess.class, criteria) == 0)
            return false;
        return true;

    }

    public int countByPersonAndProject(IPerson person, Integer keyProject) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addEqualTo("keyProject", keyProject);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return count(ProjectAccess.class, criteria);
    }

    public int countByPersonAndCC(IPerson person, Boolean cc) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        criteria.addEqualTo("costCenter", cc);
        return count(ProjectAccess.class, criteria);
    }

    public void deleteByPersonAndCC(IPerson person, Boolean cc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addEqualTo("costCenter", cc);
        List<IProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        for (IProjectAccess projectAccess : allPersonProjects) {
            delete(projectAccess);
        }
    }

    public void deleteByPersonAndDate(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addLessOrEqualThan("endDate", Calendar.getInstance().getTime());
        List<IProjectAccess> allPersonProjects = queryList(ProjectAccess.class, criteria);
        for (IProjectAccess projectAccess : allPersonProjects) {
            delete(projectAccess);
        }
    }

    public void delete(IProjectAccess projectAccess) throws ExcepcaoPersistencia {
        super.delete(projectAccess);
    }
}