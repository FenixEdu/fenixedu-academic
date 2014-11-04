package org.fenixedu.academic.ui.struts.action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Student;
import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduDelegatesContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel().registerDeletionListener(PersonFunction.class, (personFunction) -> {
            if (personFunction.getDelegate() != null) {
                personFunction.setDelegate(null);
            }
            if (personFunction.getSender() != null) {
                personFunction.setSender(null);
            }
            if (!personFunction.getDelegateStudentsGroupSet().isEmpty()) {
                throw new DomainException("error.personFunction.cannotDeletePersonFunctionUsedInAccessControl");
            }
        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(Degree.class, (degree, blockers) -> {
            if (!degree.getDelegatesGroupSet().isEmpty()) {
                throw new DomainException("error.degree.cannotDeleteDegreeUsedInAccessControl");
            }
        });

        FenixFramework.getDomainModel().registerDeletionListener(Degree.class, (degree) -> {
            for (; !degree.getDelegateElectionsSet().isEmpty(); degree.getDelegateElectionsSet().iterator().next().delete()) {
                ;
            }
        });

        FenixFramework.getDomainModel().registerDeletionListener(Student.class, (student) -> {
            for (; !student.getVotesSet().isEmpty(); student.getVotesSet().iterator().next().delete()) {
                ;
            }

            student.getElectedElectionsSet().clear();
            student.getDelegateElectionsSet().clear();
        });

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}