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
package org.fenixedu.academic.domain.phd;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

abstract public class PhdStudyPlanEntry extends PhdStudyPlanEntry_Base {

    static {
        getRelationPhdStudyPlanPhdStudyPlanEntry().addListener(new RelationAdapter<PhdStudyPlan, PhdStudyPlanEntry>() {
            @Override
            public void beforeAdd(PhdStudyPlan studyPlan, PhdStudyPlanEntry entry) {
                if (entry != null && studyPlan != null) {
                    if (studyPlan.hasSimilarEntry(entry)) {
                        throw new DomainException("error.phd.PhdStudyPlanEntry.found.similar.entry");
                    }
                }
            }
        });
    }

    protected PhdStudyPlanEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
        if (AccessControl.getPerson() != null) {
            setCreatedBy(AccessControl.getPerson().getUsername());
        }
    }

    protected PhdStudyPlanEntry(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan) {
        this();

        init(type, studyPlan);

    }

    protected void init(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan) {
        String[] args = {};
        if (type == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.PhdStudyPlanEntry.type.cannot.be.null", args);
        }
        String[] args1 = {};
        if (studyPlan == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.PhdStudyPlanEntry.studyPlan.cannot.be.null", args1);
        }

        super.setType(type);
        super.setStudyPlan(studyPlan);
    }

    public boolean isNormal() {
        return getType() == PhdStudyPlanEntryType.NORMAL;
    }

    public boolean isPropaedeutic() {
        return getType() == PhdStudyPlanEntryType.PROPAEDEUTIC;
    }

    public boolean isExtraCurricular() {
        return getType() == PhdStudyPlanEntryType.EXTRA_CURRICULAR;
    }

    public boolean isInternalEntry() {
        return false;
    }

    public boolean isExternalEntry() {
        return false;
    }

    public void delete() {
        super.setRootDomainObject(null);
        super.setStudyPlan(null);

        super.deleteDomainObject();

    }

    abstract public String getCourseDescription();

    abstract public boolean isSimilar(PhdStudyPlanEntry entry);

}
