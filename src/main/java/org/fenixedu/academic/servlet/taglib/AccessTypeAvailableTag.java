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
package org.fenixedu.academic.servlet.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.taglib.logic.ConditionalTagBase;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.access.PhdProcessAccessType;

public class AccessTypeAvailableTag extends ConditionalTagBase {

    static private final long serialVersionUID = 1L;

    private PhdIndividualProgramProcess mainProcess;
    private PhdProcessAccessType accessType;

    public PhdIndividualProgramProcess getMainProcess() {
        return mainProcess;
    }

    public void setMainProcess(PhdIndividualProgramProcess mainProcess) {
        this.mainProcess = mainProcess;
    }

    public PhdProcessAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(PhdProcessAccessType accessType) {
        this.accessType = accessType;
    }

    @Override
    protected boolean condition() throws JspException {
        return !getAccessType().hasAcceptedTypes() || hasProcessNecessaryStateTypes();
    }

    private boolean hasProcessNecessaryStateTypes() {
        return !CollectionUtils.intersection(getAccessType().getAcceptedTypes(), getMainProcess().getAllPhdProcessStateTypes())
                .isEmpty();
    }
}
