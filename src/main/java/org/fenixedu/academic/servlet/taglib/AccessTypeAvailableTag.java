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
package net.sourceforge.fenixedu.presentationTier.TagLib.phd;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.taglib.logic.ConditionalTagBase;

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
