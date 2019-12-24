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
package org.fenixedu.academic.dto;

import java.util.Comparator;
import java.util.Date;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.util.PeriodState;

import pt.ist.fenixframework.DomainObject;

/**
 * @author Nuno &amp; Joana
 */
public class InfoExecutionPeriod extends InfoObject implements Comparable {

    public static final Comparator<InfoExecutionPeriod> COMPARATOR_BY_YEAR_AND_SEMESTER = new Comparator<InfoExecutionPeriod>() {

        @Override
        public int compare(InfoExecutionPeriod o1, InfoExecutionPeriod o2) {
            final int c = o2.getInfoExecutionYear().getYear().compareTo(o1.getInfoExecutionYear().getYear());
            return c == 0 ? o2.getSemester().compareTo(o1.getSemester()) : c;
        }

    };

    private ExecutionInterval executionPeriodDomainReference;

    private String qualifiedName;

    public InfoExecutionPeriod(final ExecutionInterval executionInterval) {
        executionPeriodDomainReference = executionInterval;
    }

    private InfoExecutionYear infoExecutionYear = null;

    public InfoExecutionYear getInfoExecutionYear() {
        if (infoExecutionYear == null) {
            infoExecutionYear = new InfoExecutionYear(getExecutionPeriod().getExecutionYear());
        }
        return infoExecutionYear;
    }

    public String getName() {
        return getExecutionPeriod().getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionPeriod) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) obj;
            return (getInfoExecutionYear().equals(infoExecutionPeriod.getInfoExecutionYear())
                    && getName().equals(infoExecutionPeriod.getName()));

        }
        return false;
    }

    @Override
    public String toString() {
        return getExecutionPeriod().toString();
    }

    public PeriodState getState() {
        return getExecutionPeriod().getState();
    }

    public Integer getSemester() {
        return getExecutionPeriod().getChildOrder();
    }

    @Override
    public int compareTo(Object arg0) {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;
        int yearCmp = this.getInfoExecutionYear().compareTo(infoExecutionPeriod.getInfoExecutionYear());
        if (yearCmp != 0) {
            return yearCmp;
        } else {
            return this.getSemester().intValue() - infoExecutionPeriod.getSemester().intValue();
        }
    }

    public Date getBeginDate() {
        return getExecutionPeriod().getBeginDate();
    }

    public Date getEndDate() {
        return getExecutionPeriod().getEndDate();
    }

    /**
     * Method created for presentation matters. Concatenates execution period
     * name with execution year name.
     */
    public String getDescription() {
        StringBuilder buffer = new StringBuilder();
        if (getName() != null) {
            buffer.append(getName());
        }
        if (getInfoExecutionYear() != null) {
            buffer.append(" - ").append(getInfoExecutionYear().getYear());
        }
        return buffer.toString();
    }

    public InfoExecutionPeriod getPreviousInfoExecutionPeriod() {
        final ExecutionInterval previousInfoExecutionPeriod = getExecutionPeriod().getPrevious();
        return previousInfoExecutionPeriod == null ? null : new InfoExecutionPeriod(previousInfoExecutionPeriod);
    }

    public static InfoExecutionPeriod newInfoFromDomain(ExecutionInterval executionInterval) {
        return executionInterval == null ? null : new InfoExecutionPeriod(executionInterval);
    }

    public String getQualifiedName() {
        return getDescription();
    }

    @Override
    public void copyFromDomain(DomainObject domainObject) {
        throw new Error("Method should not be called!");
    }

    @Override
    public String getExternalId() {
        return getExecutionPeriod().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public ExecutionInterval getExecutionPeriod() {
        return executionPeriodDomainReference;
    }

    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        executionPeriodDomainReference = executionInterval;
    }

}
