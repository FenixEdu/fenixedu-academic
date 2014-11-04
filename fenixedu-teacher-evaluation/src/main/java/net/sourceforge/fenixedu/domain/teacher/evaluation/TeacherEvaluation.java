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
package org.fenixedu.academic.domain.teacher.evaluation;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.util.EmailAddressList;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class TeacherEvaluation extends TeacherEvaluation_Base {

    public TeacherEvaluation() {
        super();
        setCreatedDate(new DateTime());
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherEvaluationState getState() {
        if (getTeacherEvaluationProcess().getFacultyEvaluationProcess().getAutoEvaluationInterval().getStart().isAfterNow()) {
            return null;
        } else if (getAutoEvaluationLock() == null) {
            return TeacherEvaluationState.AUTO_EVALUATION;
        } else if (getEvaluationLock() == null) {
            return TeacherEvaluationState.EVALUATION;
        } else {
            return TeacherEvaluationState.EVALUATED;
        }
    }

    public abstract TeacherEvaluationType getType();

    public abstract Set<TeacherEvaluationFileType> getAutoEvaluationFileSet();

    public abstract Set<TeacherEvaluationFileType> getEvaluationFileSet();

    public abstract String getFilenameTypePrefix();

    public TeacherEvaluationMark getApprovedEvaluationMark() {
        return getTeacherEvaluationProcess().getApprovedEvaluationMark();
    }

    public void setApprovedEvaluationMark(TeacherEvaluationMark mark) {
        getTeacherEvaluationProcess().setApprovedEvaluationMark(mark);
    }

    @Atomic
    public void lickAutoEvaluationStamp() {
        internalLickingBusiness();
    }

    protected void internalLickingBusiness() {
        setAutoEvaluationLock(new DateTime());
        final Person who = AccessControl.getPerson();
        setUserWhoLockedAutoEvaluation(who == null ? null : who.getUser());
    }

    @Atomic
    public void lickEvaluationStamp() {
        setEvaluationLock(new DateTime());
        final Person who = AccessControl.getPerson();
        setUserWhoLockedEvaluation(who == null ? null : who.getUser());

        final TeacherEvaluationProcess teacherEvaluationProcess = getTeacherEvaluationProcess();
        final Person evaluee = teacherEvaluationProcess.getEvaluee();
        if (evaluee != AccessControl.getPerson()) {
            final Person evaluator = teacherEvaluationProcess.getEvaluator();
            final Recipient recipient = new Recipient(Collections.singletonList(evaluee));
            final Recipient ccRecipient = new Recipient(Collections.singletonList(evaluator));
            final FacultyEvaluationProcess facultyEvaluationProcess = teacherEvaluationProcess.getFacultyEvaluationProcess();
            final String title = facultyEvaluationProcess.getTitle().getContent();
            final String body = BundleUtil.getString(Bundle.APPLICATION, "message.email.stamp.teacher.evaluation.process", title);
            final SystemSender systemSender = Bennu.getInstance().getSystemSender();
            final Message message =
                    new Message(systemSender, Collections.EMPTY_LIST, Collections.EMPTY_LIST, title, body, new EmailAddressList(
                            Collections.EMPTY_LIST).toString());
            message.addTos(recipient);
            message.addCcs(ccRecipient);
        }
    }

    public void delete() {
        setTeacherEvaluationProcess(null);
        for (final TeacherEvaluationFile teacherEvaluationFile : getTeacherEvaluationFileSet()) {
            teacherEvaluationFile.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Atomic
    public void rubAutoEvaluationStamp() {
        setAutoEvaluationLock(null);
    }

    @Atomic
    public void rubEvaluationStamp() {
        setEvaluationLock(null);
    }

    public abstract void copyAutoEvaluation();

    protected void internalCopyAutoEvaluation(TeacherEvaluation copy) {
        copy.setAutoEvaluationLock(getAutoEvaluationLock());
        for (TeacherEvaluationFile file : getTeacherEvaluationFileSet()) {
            if (file.getTeacherEvaluationFileType().isAutoEvaluationFile()) {
                file.copy(copy);
            }
        }
    }

}
