package org.fenixedu.academic.ui.struts.action;

import java.util.Collection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.ExternalContract;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduGiafContractsContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel().registerDeletionBlockerListener(
                Person.class,
                (person, blockers) -> {
                    if (person.getEmployee() != null) {
                        blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
                    }
                    if (((Collection<PersonFunction>) person.getParentAccountabilities(
                            AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)).isEmpty()) {
                        blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
                    }
                });
        FenixFramework.getDomainModel().registerDeletionListener(Person.class, (person) -> {
            if (person.getResearcher() != null) {
                person.getResearcher().delete();
            }
        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(Unit.class, (unit, blockers) -> {
            if (unit.getFunctionsSet().isEmpty()) {
                blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.unit.cannot.be.deleted"));
            }
        });
        Signal.register("academic.thesis.participant.created",
                FenixEduGiafContractsContextListener::fillParticipantAffiliationAndCategory);
    }

    private static void fillParticipantAffiliationAndCategory(DomainObjectEvent<ThesisEvaluationParticipant> event) {
        ThesisEvaluationParticipant participation = event.getInstance();
        Person person = participation.getPerson();

        Teacher teacher = person.getTeacher();
        if (teacher != null && teacher.getDepartment() != null) {
            if (teacher.getLastCategory() == null) {
                participation.setCategory("-");
            } else {
                participation.setCategory(teacher.getLastCategory().getName().getContent());
            }
            participation.setAffiliation(teacher.getDepartment().getRealName());
        } else {
            Employee employee = person.getEmployee();
            if (employee != null) {
                Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
                if (currentWorkingPlace != null) {
                    participation.setAffiliation(currentWorkingPlace.getNameWithAcronym());
                }
            } else {
                ExternalContract contract = ExternalContract.getExternalContract(person);
                if (contract != null) {
                    participation.setAffiliation(contract.getInstitutionUnit().getName());
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
