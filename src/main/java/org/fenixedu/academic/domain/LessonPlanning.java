/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class LessonPlanning extends LessonPlanning_Base {

    public static final Comparator<LessonPlanning> COMPARATOR_BY_ORDER = new Comparator<LessonPlanning>() {

        @Override
        public int compare(LessonPlanning o1, LessonPlanning o2) {
            return o1.getOrderOfPlanning().compareTo(o2.getOrderOfPlanning());
        }

    };

    public LessonPlanning(LocalizedString title, LocalizedString planning, ShiftType lessonType,
            ExecutionCourse executionCourse) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setLastOrder(executionCourse, lessonType);
        setTitle(title);
        setPlanning(planning);
        setLessonType(lessonType);
        setExecutionCourse(executionCourse);

        CurricularManagementLog.createLog(executionCourse, Bundle.MESSAGING, "log.executionCourse.curricular.planning.added",
                title.getContent(), lessonType.getFullNameTipoAula(), executionCourse.getNome(),
                executionCourse.getDegreePresentationString());
    }

    public void delete() {
        reOrderLessonPlannings();
        deleteWithoutReOrder();
    }

    public void deleteWithoutReOrder() {
        CurricularManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.curricular.planning.removed", getTitle().getContent(),
                getLessonType().getFullNameTipoAula(), getExecutionCourse().getNome(), getExecutionCourse()
                        .getDegreePresentationString());
        super.setExecutionCourse(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getLessonType() != null && getPlanning() != null && !getPlanning().isEmpty() && getTitle() != null
                && !getTitle().isEmpty() && getOrderOfPlanning() != null;
    }

    @Override
    public void setLessonType(ShiftType lessonType) {
        if (lessonType == null) {
            throw new DomainException("error.LessonPlanning.no.lessonType");
        }
        super.setLessonType(lessonType);
    }

    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        if (executionCourse == null) {
            throw new DomainException("error.LessonPlanning.no.executionCourse");
        }
        super.setExecutionCourse(executionCourse);
    }

    @Override
    public void setPlanning(LocalizedString planning) {
        if (planning == null || planning.getLocales().isEmpty()) {
            throw new DomainException("error.LessonPlanning.no.planning");
        }
        super.setPlanning(planning);
    }

    @Override
    public void setTitle(LocalizedString title) {
        if (title == null || title.getLocales().isEmpty()) {
            throw new DomainException("error.LessonPlanning.no.title");
        }
        super.setTitle(title);
    }

    @Override
    public void setOrderOfPlanning(Integer orderOfPlanning) {
        if (orderOfPlanning == null) {
            throw new DomainException("error.LessonPlanning.empty.order");
        }
        super.setOrderOfPlanning(orderOfPlanning);
    }

    public void moveTo(Integer order) {
        List<LessonPlanning> lessonPlannings = getExecutionCourse().getLessonPlanningsOrderedByOrder(getLessonType());
        if (!lessonPlannings.isEmpty() && order != getOrderOfPlanning() && order <= lessonPlannings.size() && order >= 1) {
            LessonPlanning posPlanning = lessonPlannings.get(order - 1);
            Integer posOrder = posPlanning.getOrderOfPlanning();
            posPlanning.setOrderOfPlanning(getOrderOfPlanning());
            setOrderOfPlanning(posOrder);
        }
    }

    private void reOrderLessonPlannings() {
        List<LessonPlanning> lessonPlannings = getExecutionCourse().getLessonPlanningsOrderedByOrder(getLessonType());
        if (!lessonPlannings.isEmpty() && !lessonPlannings.get(lessonPlannings.size() - 1).equals(this)) {
            for (int i = getOrderOfPlanning(); i < lessonPlannings.size(); i++) {
                LessonPlanning planning = lessonPlannings.get(i);
                planning.setOrderOfPlanning(planning.getOrderOfPlanning() - 1);
            }
        }
    }

    private void setLastOrder(ExecutionCourse executionCourse, ShiftType lessonType) {
        List<LessonPlanning> lessonPlannings = executionCourse.getLessonPlanningsOrderedByOrder(lessonType);
        Integer order =
                (!lessonPlannings.isEmpty()) ? (lessonPlannings.get(lessonPlannings.size() - 1).getOrderOfPlanning() + 1) : 1;
        setOrderOfPlanning(order);
    }

    public String getLessonPlanningLabel() {
        StringBuilder builder = new StringBuilder();
        builder.append(BundleUtil.getString(Bundle.APPLICATION, "label.lesson")).append(" ");
        builder.append(getOrderOfPlanning()).append(" (");
        builder.append(BundleUtil.getString(Bundle.ENUMERATION, getLessonType().getName())).append(") - ");
        builder.append(getTitle().getContent());
        return builder.toString();
    }

    public void logEditEditLessonPlanning() {
        CurricularManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.curricular.planning.edited", getTitle().getContent(), getLessonType().getFullNameTipoAula(),
                getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
    }

}
