/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.projectsManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class ProjectAccessVO extends VersionedObjectsBase implements IPersistentProjectAccess {

    public ProjectAccess readByPersonIdAndProjectAndDate(Integer personId, Integer keyProject) throws ExcepcaoPersistencia {
        final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
        for (ProjectAccess projectAccess : projectAccessList) {
            if (projectAccess.getKeyPerson().equals(personId) && projectAccess.getKeyProject().equals(keyProject)
                    && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())) {
                return projectAccess;
            }
        }
        return null;
    }

    public List<ProjectAccess> readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia {
        final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
        List<ProjectAccess> result = new ArrayList<ProjectAccess>();
        for (ProjectAccess projectAccess : projectAccessList) {
            if (projectAccess.getKeyProjectCoordinator().equals(coordinatorCode)
                    && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())) {
                result.add(projectAccess);
            }
        }
        return result;
    }

    public List<ProjectAccess> readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode) throws ExcepcaoPersistencia {
        Integer personId = null;
        for (final Person person : Party.readAllPersons()) {
            if (person.getUsername().equalsIgnoreCase(username)) {
                personId = person.getIdInternal();
            }
        }
        List<ProjectAccess> result = new ArrayList<ProjectAccess>();
        if (personId != null) {
            final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
            for (ProjectAccess projectAccess : projectAccessList) {
                if (projectAccess.getKeyPerson().equals(personId) && projectAccess.getKeyProjectCoordinator().equals(coordinatorCode)
                        && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())) {
                    result.add(projectAccess);
                }
            }
        }
        return result;
    }

    public List<Integer> readProjectCodesByPersonUsernameAndDateAndCC(String username, String cc) throws ExcepcaoPersistencia {
        Integer personId = null;
        for (final Person person : Party.readAllPersons()) {
            if (person.getUsername().equalsIgnoreCase(username)) {
                personId = person.getIdInternal();
            }
        }
        List<Integer> result = new ArrayList<Integer>();
        if (personId != null) {
            final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
            for (ProjectAccess projectAccess : projectAccessList) {
                if (projectAccess.getKeyPerson().equals(personId) && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                        && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime()))
                    if (cc != null && !cc.equals("")) {
                        if (projectAccess.getCostCenter().equals(true) && projectAccess.getKeyProjectCoordinator().equals(cc)) {
                            result.add(projectAccess.getKeyProject());
                        }
                    } else if (projectAccess.getCostCenter().equals(false)) {
                        result.add(projectAccess.getKeyProject());
                    }
            }
        }
        return result;
    }

    public List<Integer> readCCCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia {
        Integer personId = null;
        for (final Person person : Party.readAllPersons()) {
            if (person.getUsername().equalsIgnoreCase(username)) {
                personId = person.getIdInternal();
            }
        }
        List<Integer> result = new ArrayList<Integer>();
        if (personId != null) {
            final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
            for (ProjectAccess projectAccess : projectAccessList) {
                if (projectAccess.getKeyPerson().equals(personId) && projectAccess.getCostCenter().equals(true)
                        && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                        && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())
                        && result.contains(projectAccess.getKeyProjectCoordinator())) {
                    result.add(projectAccess.getKeyProjectCoordinator());
                }
            }

        }
        return result;
    }

    public List<Integer> readProjectCodesByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all)
            throws ExcepcaoPersistencia {
        Integer personId = null;
        for (final Person person : Party.readAllPersons()) {
            if (person.getUsername().equalsIgnoreCase(username)) {
                personId = person.getIdInternal();
            }
        }
        List<Integer> result = new ArrayList<Integer>();
        if (personId != null) {
            final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
            for (ProjectAccess projectAccess : projectAccessList) {
                if (projectAccess.getKeyPerson().equals(personId) && projectAccess.getKeyProjectCoordinator().equals(coordinatorCode)
                        && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                        && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())) {
                    result.add(projectAccess.getKeyProject());
                }
            }
        }
        return result;
    }

    public List<Integer> readCoordinatorsCodesByPersonUsernameAndDatesAndCC(String username, String cc) throws ExcepcaoPersistencia {
        Integer personId = null;
        for (final Person person : Party.readAllPersons()) {
            if (person.getUsername().equalsIgnoreCase(username)) {
                personId = person.getIdInternal();
            }
        }
        List<Integer> result = new ArrayList<Integer>();
        if (personId != null) {
            final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
            for (ProjectAccess projectAccess : projectAccessList) {
                if (projectAccess.getKeyPerson().equals(personId) && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                        && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())
                        && !result.contains(projectAccess.getKeyProjectCoordinator())) {
                    if (cc != null && !cc.equals("")) {
                        if (projectAccess.getCostCenter().equals(true) && projectAccess.getKeyProjectCoordinator().equals(cc)) {

                            result.add(projectAccess.getKeyProjectCoordinator());
                        }
                    } else if (projectAccess.getCostCenter().equals(false)) {
                        result.add(projectAccess.getKeyProject());
                    }

                }
            }
        }
        return result;
    }

    public boolean hasPersonProjectAccess(String personUsername, Integer projectCode) {
        Integer personId = null;
        for (final Person person : Party.readAllPersons()) {
            if (person.getUsername().equalsIgnoreCase(personUsername)) {
                personId = person.getIdInternal();
            }
        }
        if (personId != null) {
            final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
            for (ProjectAccess projectAccess : projectAccessList) {
                if (projectAccess.getKeyPerson().equals(personId) && projectAccess.getKeyProject().equals(projectCode)
                        && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                        && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countByPersonAndProject(Person person, Integer keyProject) {
        int result = 0;
        final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
        for (ProjectAccess projectAccess : projectAccessList) {
            if (projectAccess.getKeyPerson().equals(person.getIdInternal()) && projectAccess.getPerson().equals(person)
                    && projectAccess.getKeyProject().equals(keyProject)
                    && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                    && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime())) {
                result++;
            }
        }
        return result;
    }

    public int countByPersonAndCC(Person person, Boolean cc) {
        int result = 0;
        final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
        for (ProjectAccess projectAccess : projectAccessList) {
            if (projectAccess.getKeyPerson().equals(person.getIdInternal())
                    && Calendar.getInstance().getTime().after(projectAccess.getBeginDate().getTime())
                    && Calendar.getInstance().getTime().before(projectAccess.getEndDate().getTime()) && projectAccess.getCostCenter().equals(cc)) {
                result++;
            }
        }
        return result;
    }

    public void deleteByPersonAndCC(Person person, Boolean cc) throws ExcepcaoPersistencia {
        final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
        for (ProjectAccess projectAccess : projectAccessList) {
            if (projectAccess.getKeyPerson().equals(person.getIdInternal()) && projectAccess.getCostCenter().equals(cc)) {
                delete(projectAccess);
            }
        }
    }

    public void deleteByPersonAndDate(Person person) throws ExcepcaoPersistencia {
        final List<ProjectAccess> projectAccessList = (List<ProjectAccess>) readAll(ProjectAccess.class);
        for (ProjectAccess projectAccess : projectAccessList) {
            if (projectAccess.getKeyPerson().equals(person.getIdInternal())
                    && Calendar.getInstance().getTime().after(projectAccess.getEndDate().getTime())) {
            	projectAccess.removePerson();
                delete(projectAccess);
            }
        }
    }

    public void delete(ProjectAccess projectAccess) throws ExcepcaoPersistencia {
        deleteByOID(ProjectAccess.class, projectAccess.getIdInternal());
    }
}