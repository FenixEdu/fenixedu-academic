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
    
    public List readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return queryList(ProjectAccess.class, criteria);
    }

    public List readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return queryList(ProjectAccess.class, criteria);
    }

    public List readProjectCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        List allPersonProjects = queryList(ProjectAccess.class, criteria);
        List result = new ArrayList();
        for (int i = 0; i < allPersonProjects.size(); i++)
            result.add(((IProjectAccess) allPersonProjects.get(i)).getKeyProject());
        return result;
    }

    public List readProjectCodesByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addEqualTo("keyProjectCoordinator", coordinatorCode);
        if (!all)
            criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        List allPersonProjects = queryList(ProjectAccess.class, criteria);
        List result = new ArrayList();
        for (int i = 0; i < allPersonProjects.size(); i++)
            result.add(((IProjectAccess) allPersonProjects.get(i)).getKeyProject());
        return result;
    }

    public List readCoordinatorsCodesByPersonUsernameAndDates(String username) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", username);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        List allPersonProjects = queryList(ProjectAccess.class, criteria);
        List result = new ArrayList();
        for (int i = 0; i < allPersonProjects.size(); i++)
            if (!result.contains(((IProjectAccess) allPersonProjects.get(i)).getKeyProjectCoordinator()))
                result.add(((IProjectAccess) allPersonProjects.get(i)).getKeyProjectCoordinator());
        return result;
    }

    public boolean hasPersonProjectAccess(String personUsername, Integer projectCode) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", personUsername);
        criteria.addEqualTo("keyProject", projectCode);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        if (count(ProjectAccess.class, criteria) == 0)
            return false;
        return true;

    }

    public int countByPersonAndProject(IPerson person, Integer keyProject) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addEqualTo("keyProject", keyProject);
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return count(ProjectAccess.class, criteria);
    }

    public int countByPerson(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());
        return count(ProjectAccess.class, criteria);
    }

    public void deleteByPerson(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        List allPersonProjects = queryList(ProjectAccess.class, criteria);
        for (int i = 0; i < allPersonProjects.size(); i++) {
            delete((IProjectAccess) allPersonProjects.get(i));
        }
    }

    public void deleteByPersonAndDate(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", person.getIdInternal());
        criteria.addLessOrEqualThan("endDate", Calendar.getInstance().getTime());
        List allPersonProjects = queryList(ProjectAccess.class, criteria);
        for (int i = 0; i < allPersonProjects.size(); i++) {
            delete((IProjectAccess) allPersonProjects.get(i));
        }
    }

    public void delete(IProjectAccess projectAccess) throws ExcepcaoPersistencia {
        super.delete(projectAccess);
    }
}