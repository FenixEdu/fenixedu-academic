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
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class LessonPlanning extends LessonPlanning_Base {

    public static final Comparator<LessonPlanning> COMPARATOR_BY_ORDER = new Comparator<LessonPlanning>() {

        @Override
        public int compare(LessonPlanning o1, LessonPlanning o2) {
            return o1.getOrderOfPlanning().compareTo(o2.getOrderOfPlanning());
        }

    };

    public LessonPlanning(MultiLanguageString title, MultiLanguageString planning, ShiftType lessonType,
            ExecutionCourse executionCourse) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setLastOrder(executionCourse, lessonType);
        setTitle(title);
        setPlanning(planning);
        setLessonType(lessonType);
        setExecutionCourse(executionCourse);

        CurricularManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.curricular.planning.added", title.getContent(), lessonType.getFullNameTipoAula(),
                executionCourse.getNome(), executionCourse.getDegreePresentationString());
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
    public void setPlanning(MultiLanguageString planning) {
        if (planning == null || planning.getAllContents().isEmpty()) {
            throw new DomainException("error.LessonPlanning.no.planning");
        }
        super.setPlanning(planning);
    }

    @Override
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.getAllContents().isEmpty()) {
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
        builder.append(BundleUtil.getString(Bundle.ENUMERATION, getLessonType().getName()))
                .append(") - ");
        builder.append(getTitle().getContent());
        return builder.toString();
    }

    public void logEditEditLessonPlanning() {
        CurricularManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.curricular.planning.edited", getTitle().getContent(), getLessonType().getFullNameTipoAula(),
                getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasOrderOfPlanning() {
        return getOrderOfPlanning() != null;
    }

    @Deprecated
    public boolean hasPlanning() {
        return getPlanning() != null;
    }

    @Deprecated
    public boolean hasLessonType() {
        return getLessonType() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
