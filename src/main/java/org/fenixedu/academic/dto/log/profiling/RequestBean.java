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
package net.sourceforge.fenixedu.dataTransferObject.log.profiling;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class RequestBean implements Serializable {

    String serverName;
    String moduleName;
    LocalDate date;
    String name;
    int invocationCount;
    int minTimeSpent;
    int maxTimeSpent;
    int totalTimeSpent;
    int averageSpent;
    boolean alert;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInvocationCount() {
        return invocationCount;
    }

    public void setInvocationCount(int invocationCount) {
        this.invocationCount = invocationCount;
    }

    public int getMinTimeSpent() {
        return minTimeSpent;
    }

    public void setMinTimeSpent(int minTimeSpent) {
        this.minTimeSpent = minTimeSpent;
    }

    public int getMaxTimeSpent() {
        return maxTimeSpent;
    }

    public void setMaxTimeSpent(int maxTimeSpent) {
        this.maxTimeSpent = maxTimeSpent;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public int getAverageSpent() {
        return averageSpent;
    }

    public void setAverageSpent(int averageSpent) {
        this.averageSpent = averageSpent;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

}
