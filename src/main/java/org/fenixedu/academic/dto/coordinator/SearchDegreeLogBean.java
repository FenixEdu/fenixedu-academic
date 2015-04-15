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
package org.fenixedu.academic.dto.coordinator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeLog;
import org.fenixedu.academic.domain.DegreeLog.DegreeLogTypes;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class SearchDegreeLogBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Degree degree;
    private Boolean viewPhoto;

    private Collection<DegreeLogTypes> degreeLogsTypes;
    private Collection<DegreeLog> degreeLogs;

    public String getEnumerationResourcesString(String name) {
        return BundleUtil.getString(Bundle.ENUMERATION, name);
    }

    public String getApplicationResourcesString(String name) {
        return BundleUtil.getString(Bundle.APPLICATION, name);
    }

    public SearchDegreeLogBean(Degree degree) {
        setDegree(degree);
        setViewPhoto(true);
        setDegreeLogTypes(DegreeLogTypes.valuesAsList());
        degreeLogs = new ArrayList<DegreeLog>();
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Boolean getViewPhoto() {
        return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
        this.viewPhoto = viewPhoto;
    }

    public Collection<DegreeLogTypes> getDegreeLogTypes() {
        return degreeLogsTypes;
    }

    public Collection<DegreeLogTypes> getDegreeLogTypesAll() {
        return DegreeLogTypes.valuesAsList();
    }

    public void setDegreeLogTypes(Collection<DegreeLogTypes> degreeLogsTypes) {
        this.degreeLogsTypes = degreeLogsTypes;
    }

    public Collection<DegreeLog> getDegreeLogs() {
        Collection<DegreeLog> dlogs = new ArrayList<DegreeLog>();
        for (DegreeLog degreeLog : degreeLogs) {
            dlogs.add(degreeLog);
        }
        return dlogs;
    }

    public void setDegreeLogs(Collection<DegreeLog> degreeLogs) {
        Collection<DegreeLog> dlogs = new ArrayList<DegreeLog>();
        for (DegreeLog dlog : degreeLogs) {
            dlogs.add(dlog);
        }
        this.degreeLogs = dlogs;
    }

    public Predicate<DegreeLog> getFilters() {

        Collection<Predicate<DegreeLog>> filters = new ArrayList<Predicate<DegreeLog>>();

        if (getDegreeLogTypes().size() < DegreeLogTypes.values().length) {
            filters.add(new InlinePredicate<DegreeLog, Collection<DegreeLogTypes>>(getDegreeLogTypes()) {

                @Override
                public boolean test(DegreeLog degreeLog) {
                    return getValue().contains(degreeLog.getDegreeLogType());
                }

            });
        }

        return new AndPredicate<DegreeLog>(filters);
    }

    public String getLabel() {

        String logTypeValues = "";

        for (DegreeLogTypes logType : DegreeLogTypes.values()) {
            if (!logTypeValues.isEmpty()) {
                logTypeValues += ", ";
            }
            logTypeValues += getEnumerationResourcesString(logType.getQualifiedName());
        }

        return String.format("%s : %s", getApplicationResourcesString("log.label.selectLogType"), logTypeValues);

    }

    public String getSearchElementsAsParameters() {
        String parameters = "";

        parameters += "&amp;degree=" + getDegree().getExternalId();
        if (viewPhoto) {
            parameters += "&amp;viewPhoto=true";
        }
        if (getDegreeLogTypes() != null && !getDegreeLogTypes().isEmpty()) {
            parameters += "&amp;degreeLogTypes=";
            for (DegreeLogTypes logType : getDegreeLogTypes()) {
                parameters += logType.toString() + ":";
            }
        }

        return parameters;
    }
}
