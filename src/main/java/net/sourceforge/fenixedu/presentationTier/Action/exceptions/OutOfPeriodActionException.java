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
/*
 * Created on 8/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

import java.util.Date;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * @author jpvl
 */
public abstract class OutOfPeriodActionException extends FenixActionException {
    private String messageKey;

    private Date startDate;

    private Date endDate;

    public OutOfPeriodActionException(String messageKey, Date startDate, Date endDate, ActionForward actionForward) {
        super(actionForward);
        this.startDate = startDate;
        this.endDate = endDate;
        this.messageKey = messageKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.exceptions.FenixActionException#getError()
     */
    @Override
    public ActionError getError() {
        ActionError actionError = null;
        if (startDate == null || endDate == null) {
            actionError = new ActionError(this.getDefaultMessageKey());
        } else {
            actionError = new ActionError(this.messageKey, startDate, endDate);
        }
        return actionError;
    }

    /**
     *  
     */
    abstract protected String getDefaultMessageKey();

}