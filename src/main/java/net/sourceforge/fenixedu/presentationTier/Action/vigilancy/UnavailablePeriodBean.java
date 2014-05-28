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
package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

import org.joda.time.DateTime;

public class UnavailablePeriodBean implements Serializable {

    private DateTime beginDate;

    private DateTime endDate;

    private String justification;

    private VigilantWrapper vigilantWrapper;

    private ExamCoordinator coordinator;

    private final List<UnavailablePeriod> unavailablePeriods = new ArrayList<UnavailablePeriod>();

    private VigilantGroup selectedVigilantGroup;

    private UnavailablePeriod unavailablePeriod;

    public UnavailablePeriodBean() {
        setVigilantWrapper(null);
        setCoordinator(null);
        setSelectedVigilantGroup(null);
        setUnavailablePeriod(null);
    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public VigilantWrapper getVigilantWrapper() {
        return this.vigilantWrapper;
    }

    public void setVigilantWrapper(VigilantWrapper vigilantWrapper) {
        this.vigilantWrapper = vigilantWrapper;
    }

    public ExamCoordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(ExamCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public String getExternalId() {
        return getUnavailablePeriod() != null ? getUnavailablePeriod().getExternalId() : null;
    }

    public Collection getUnavailablePeriods() {
        Collection periods = new ArrayList<UnavailablePeriod>();
        for (UnavailablePeriod unavailablePeriod : this.unavailablePeriods) {
            if (unavailablePeriod != null) {
                periods.add(unavailablePeriod);
            }
        }
        return periods;
    }

    public void setUnavailablePeriods(List<UnavailablePeriod> unavailablePeriods) {
        for (UnavailablePeriod unavailablePeriod : unavailablePeriods) {
            if (unavailablePeriod != null) {
                this.unavailablePeriods.add(unavailablePeriod);
            }
        }
    }

    public VigilantGroup getSelectedVigilantGroup() {
        return this.selectedVigilantGroup;
    }

    public void setSelectedVigilantGroup(VigilantGroup group) {
        this.selectedVigilantGroup = group;
    }

    public UnavailablePeriod getUnavailablePeriod() {
        return this.unavailablePeriod;
    }

    public void setUnavailablePeriod(UnavailablePeriod period) {
        this.unavailablePeriod = period;
    }
}
