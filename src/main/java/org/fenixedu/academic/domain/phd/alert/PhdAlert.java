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
package org.fenixedu.academic.domain.phd.alert;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.util.email.UnitBasedSender;
import org.fenixedu.academic.util.MultiLanguageString;

abstract public class PhdAlert extends PhdAlert_Base {

    protected PhdAlert() {
        super();
    }

    protected void init(PhdIndividualProgramProcess process, MultiLanguageString subject, MultiLanguageString body) {
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.phd.alert.PhdAlert.process.cannot.be.null", args);
        }
        super.setProcess(process);
        super.init(subject, body);
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
        throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.process");
    }

    protected String buildMailBody() {
        final StringBuilder result = new StringBuilder();

        for (final String eachContent : getFormattedBody().getAllContents()) {
            result.append(eachContent).append("\n").append(" ------------------------- ");
        }

        result.delete(result.lastIndexOf("\n") + 1, result.length());

        return result.toString();

    }

    protected String buildMailSubject() {
        final StringBuilder result = new StringBuilder();

        for (final String eachContent : getFormattedSubject().getAllContents()) {
            result.append(eachContent).append(" / ");
        }

        if (result.toString().endsWith(" / ")) {
            result.delete(result.length() - 3, result.length());
        }

        return result.toString();
    }

    public boolean isSystemAlert() {
        return false;
    }

    public boolean isCustomAlert() {
        return false;
    }

    protected void disconnect() {
        super.setProcess(null);
        setRootDomainObjectForActiveAlerts(null);
        setRootDomainObject(null);
    }

    public void delete() {
        disconnect();
        super.deleteDomainObject();
    }

    public boolean isActive() {
        return getActive().booleanValue();
    }

    protected UnitBasedSender getSender() {
        AdministrativeOffice administrativeOffice = this.getProcess().getAdministrativeOffice();
        return administrativeOffice.getUnit().getUnitBasedSenderSet().iterator().next();
    }

}
