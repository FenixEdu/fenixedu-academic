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
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

public class ServerBean implements Serializable {

    LocalDate date;
    String serverName;
    int minTimeSpent;
    int maxTimeSpent;
    int invocationCount;
    int averageTimeSpent;
    int totalTimeSpent;

    List<ModuleBean> modules;

    public ServerBean() {
        modules = new ArrayList<ModuleBean>();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

    public int getInvocationCount() {
        return invocationCount;
    }

    public void setInvocationCount(int invocationCount) {
        this.invocationCount = invocationCount;
    }

    public int getAverageTimeSpent() {
        return averageTimeSpent;
    }

    public void setAverageTimeSpent(int averageTimeSpent) {
        this.averageTimeSpent = averageTimeSpent;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public List<ModuleBean> getModules() {
        return modules;
    }

    public void addModuleBean(ModuleBean module) {
        module.setServerName(getServerName());
        this.modules.add(module);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
