/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Nov 14, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputHidden;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.EnrolStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.student.UnEnrollStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.spaces.domain.Space;

public class ManageEvaluationsForStudent extends DisplayEvaluationsForStudentToEnrol {

    private List<Evaluation> evaluationsWithEnrolmentPeriodOpened;
    private List<Evaluation> evaluationsWithEnrolmentPeriodClosed;
    private HtmlInputHidden evaluationTypeHidden;
    private Map<String, Boolean> enroledEvaluationsForStudent;
    private Map<String, String> studentRooms;

    public List<Evaluation> getEvaluationsWithEnrolmentPeriodClosed() {
        if (this.evaluationsWithEnrolmentPeriodClosed == null) {
            processEvaluations();
        }
        return this.evaluationsWithEnrolmentPeriodClosed;
    }

    public void setEvaluationsWithEnrolmentPeriodClosed(List<Evaluation> evaluationsWithEnrolmentPeriodClosed) {
        this.evaluationsWithEnrolmentPeriodClosed = evaluationsWithEnrolmentPeriodClosed;
    }

    public List<Evaluation> getEvaluationsWithEnrolmentPeriodOpened() {
        if (this.evaluationsWithEnrolmentPeriodOpened == null) {
            processEvaluations();
        }
        return this.evaluationsWithEnrolmentPeriodOpened;
    }

    public void setEvaluationsWithEnrolmentPeriodOpened(List<Evaluation> evaluationsWithEnrolmentPeriodOpened) {
        this.evaluationsWithEnrolmentPeriodOpened = evaluationsWithEnrolmentPeriodOpened;
    }

    private void processEvaluations() {
        this.evaluationsWithEnrolmentPeriodClosed = new ArrayList();
        this.evaluationsWithEnrolmentPeriodOpened = new ArrayList();

        final String evaluationType = getEvaluationTypeString();
        for (final Registration registration : getStudent().getStudent().getRegistrations()) {

            if (!registration.hasStateType(getExecutionPeriod(), RegistrationStateType.REGISTERED)) {
                continue;
            }

            for (final WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(getExecutionPeriod())) {
                if (writtenEvaluation instanceof Exam) {
                    final Exam exam = (Exam) writtenEvaluation;
                    if (!exam.isExamsMapPublished()) {
                        continue;
                    }
                }

                if (writtenEvaluation.getClass().getName().equals(evaluationType)) {
                    try {
                        if (writtenEvaluation.isInEnrolmentPeriod()) {
                            this.evaluationsWithEnrolmentPeriodOpened.add(writtenEvaluation);
                        } else {
                            this.evaluationsWithEnrolmentPeriodClosed.add(writtenEvaluation);
                            final Space room = registration.getRoomFor(writtenEvaluation);
                            String roomName = room != null ? room.getName() : "-";
                            getStudentRooms().put(writtenEvaluation.getExternalId(), roomName);
                        }
                    } catch (final DomainException e) {
                        getEvaluationsWithoutEnrolmentPeriod().add(writtenEvaluation);
                        final Space room = registration.getRoomFor(writtenEvaluation);
                        String value = room != null ? room.getName() : "-";
                        getStudentRooms().put(writtenEvaluation.getExternalId(), value);
                    } finally {
                        getEnroledEvaluationsForStudent().put(writtenEvaluation.getExternalId(),
                                Boolean.valueOf(registration.isEnroledIn(writtenEvaluation)));
                        getExecutionCourses().put(writtenEvaluation.getExternalId(),
                                writtenEvaluation.getAttendingExecutionCoursesFor(registration));
                    }
                }
            }
        }
        Collections.sort(this.evaluationsWithEnrolmentPeriodClosed, new BeanComparator("dayDate"));
        Collections.sort(this.evaluationsWithEnrolmentPeriodOpened, new BeanComparator("dayDate"));
        Collections.sort(getEvaluationsWithoutEnrolmentPeriod(), new BeanComparator("dayDate"));
    }

    public String enrolStudent() {
        try {
            EnrolStudentInWrittenEvaluation.runEnrolStudentInWrittenEvaluation(getUserView().getUsername(), getEvaluationID());
            clearAttributes();
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        } catch (NotAuthorizedException e) {
            setErrorMessage("errors.impossible.operation");
        } catch (FenixServiceException e) {
            setErrorMessage("errors.impossible.operation");
        }
        return "";
    }

    public String unenrolStudent() {
        try {
            UnEnrollStudentInWrittenEvaluation.runUnEnrollStudentInWrittenEvaluation(getUserView().getUsername(),
                    getEvaluationID());
            clearAttributes();
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        } catch (NotAuthorizedException e) {
            setErrorMessage("errors.impossible.operation");
        } catch (FenixServiceException e) {
            setErrorMessage("errors.impossible.operation");
        }
        return "";
    }

    public String getEvaluationID() {
        return getRequestParameter("evaluationID");
    }

    @Override
    protected void clearAttributes() {
        setEvaluationsWithEnrolmentPeriodClosed(null);
        setEvaluationsWithEnrolmentPeriodOpened(null);
        setEvaluationsWithoutEnrolmentPeriod(null);
        setExecutionCourses(null);
    }

    @Override
    public Integer getEvaluationType() {
        if (this.evaluationType == null) {
            if (this.getRequestParameter("evaluationType") != null) {
                this.evaluationType = Integer.valueOf(this.getRequestParameter("evaluationType"));
            } else if (this.getEvaluationTypeHidden().getValue() != null) {
                this.evaluationType = Integer.valueOf(this.getEvaluationTypeHidden().getValue().toString());
            }
        }
        return this.evaluationType;
    }

    public HtmlInputHidden getEvaluationTypeHidden() {
        if (this.evaluationTypeHidden == null) {
            this.evaluationTypeHidden = new HtmlInputHidden();
            this.evaluationTypeHidden.setValue(getEvaluationType());
        }
        return this.evaluationTypeHidden;
    }

    public void setEvaluationTypeHidden(HtmlInputHidden evaluationTypeHidden) {
        if (evaluationTypeHidden != null) {
            setEvaluationType(Integer.valueOf(evaluationTypeHidden.getValue().toString()));
        }
        this.evaluationTypeHidden = evaluationTypeHidden;
    }

    public Map<String, Boolean> getEnroledEvaluationsForStudent() {
        if (this.enroledEvaluationsForStudent == null) {
            this.enroledEvaluationsForStudent = new HashMap<String, Boolean>();
        }
        return this.enroledEvaluationsForStudent;
    }

    public void setEnroledEvaluationsForStudent(Map<String, Boolean> enroledEvaluationsForStudent) {
        this.enroledEvaluationsForStudent = enroledEvaluationsForStudent;
    }

    public Map<String, String> getStudentRooms() {
        if (this.studentRooms == null) {
            this.studentRooms = new HashMap<String, String>();
        }
        return this.studentRooms;
    }

    public void setStudentRooms(Map<String, String> studentRooms) {
        this.studentRooms = studentRooms;
    }
}
