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

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;

import org.apache.struts.taglib.logic.ConditionalTagBase;
import org.fenixedu.bennu.core.security.Authenticate;

public class ActivityAvailableTag extends ConditionalTagBase {

    static private final long serialVersionUID = 1L;

    private Process process;
    private Class<? extends Activity> activity;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setProcess(Object object) {
        setProcess((Process) object);
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = (Class<? extends Activity>) activity;
    }

    @Override
    protected boolean condition() throws JspException {
        final Activity activity = getProcess().getActivity(getActivity());

        if (activity == null) {
            throw new JspException("ActivityAvailableTag: activity not found");
        }

        try {
            activity.checkPreConditions(getProcess(), Authenticate.getUser());
            return true;
        } catch (final PreConditionNotValidException e) {
            return false;
        }
    }
}
