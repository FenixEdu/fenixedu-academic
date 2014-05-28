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
package pt.utl.ist.scripts.process.updateData;

import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;

@Task(englishTitle = "Check if an Unit is internal and is marked as external")
public class CheckIfIsInternalUnitTask extends CronTask {

    @Override
    @Atomic
    public void runTask() throws Exception {
        Set<UnitName> units = Bennu.getInstance().getUnitNameSet();
        int count = 0;
        for (UnitName unitName : units) {
            if (unitName.getIsExternalUnit() && unitName.getUnit().isInternal()) {
                taskLog(unitName.getExternalId() + " -> " + unitName.getName());
                count++;
                unitName.setIsExternalUnit(false);
            }
        }
        taskLog(count + " changes.");
    }

}
