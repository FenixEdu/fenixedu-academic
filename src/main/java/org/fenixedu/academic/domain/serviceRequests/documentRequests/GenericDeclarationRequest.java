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
package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;

public class GenericDeclarationRequest extends GenericDeclarationRequest_Base {

    public GenericDeclarationRequest() {
        super();
    }

    public GenericDeclarationRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.GENERIC_DECLARATION;
    }

    @Override
    protected boolean hasFreeDeclarationRequests() {
        return false;
    }

    @Override
    public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    public EventType getEventType() {
        return EventType.GENERIC_DECLARATION_REQUEST;
    }

    public EntryType getEntryType() {
        return EntryType.GENERIC_DECLARATION_REQUEST_FEE;
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {

    }

}
