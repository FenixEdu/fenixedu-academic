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
package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceEventBean;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import pt.ist.fenixframework.Atomic;

public class CreateResidenceEvents {

    @Atomic
    public static void run(List<ResidenceEventBean> beans, ResidenceMonth month) {
        for (ResidenceEventBean bean : beans) {
            if (!month.isEventPresent(bean.getStudent().getPerson())) {
                new ResidenceEvent(month, bean.getStudent().getPerson(), bean.getRoomValue(), bean.getRoom());
            }
        }
    }
}