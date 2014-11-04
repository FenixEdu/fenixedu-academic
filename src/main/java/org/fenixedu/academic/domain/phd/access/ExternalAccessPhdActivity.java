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
package net.sourceforge.fenixedu.domain.phd.access;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;

import org.fenixedu.bennu.core.domain.User;

abstract public class ExternalAccessPhdActivity<T extends PhdProgramProcess> extends Activity<T> {

    @Override
    protected T executeActivity(T process, User userView, Object object) {
        final PhdExternalOperationBean bean = (PhdExternalOperationBean) object;
        bean.getParticipant().checkAccessCredentials(bean.getEmail(), bean.getPassword());

        return internalExecuteActivity(process, userView, bean);
    }

    abstract protected T internalExecuteActivity(T process, User userView, PhdExternalOperationBean bean);

    @Override
    protected void log(PhdProgramProcess process, User userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}
