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

import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class AdHocEvaluation extends AdHocEvaluation_Base {

    public static final Comparator<AdHocEvaluation> AD_HOC_EVALUATION_CREATION_DATE_COMPARATOR =
            new Comparator<AdHocEvaluation>() {

                @Override
                public int compare(AdHocEvaluation e1, AdHocEvaluation e2) {
                    return e1.getCreationDateTime().compareTo(e2.getCreationDateTime());
                }

            };

    public AdHocEvaluation() {
        super();
        setCreationDateTime(new DateTime());
    }

    public AdHocEvaluation(final ExecutionCourse executionCourse, final String name, final String description,
            final GradeScale gradeScale) {
        this();

        if (name == null || executionCourse == null || gradeScale == null) {
            throw new NullPointerException();
        }

        addAssociatedExecutionCourses(executionCourse);
        setName(name);
        setDescription(description);
        setGradeScale(gradeScale);

        logCreate();
    }

    @Override
    public void delete() {
        logRemove();
        super.delete();
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.AD_HOC_TYPE;
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.adHocEvaluation") + " " + getName();
    }

    public void edit(final String name, final String description, final GradeScale gradeScale) {
        if (name == null || gradeScale == null) {
            throw new NullPointerException();
        }
        setName(name);
        setDescription(description);

        if (getGradeScale() != gradeScale) {
            setGradeScale(gradeScale);
        }

        logEdit();
    }

    @Deprecated
    public java.util.Date getCreation() {
        org.joda.time.DateTime dt = getCreationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreation(java.util.Date date) {
        if (date == null) {
            setCreationDateTime(null);
        } else {
            setCreationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasCreationDateTime() {
        return getCreationDateTime() != null;
    }

}
