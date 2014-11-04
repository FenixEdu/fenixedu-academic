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
package org.fenixedu.academic.domain.administrativeOffice.curriculumValidation;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class DocumentPrintRequest extends DocumentPrintRequest_Base {

    public DocumentPrintRequest() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public DocumentPrintRequest(String conclusionDateValue, String degreeDescriptionValue, String graduatedTitleValue,
            DocumentRequest request) {
        this();

        if (AccessControl.getPerson() == null) {
            throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.person.cannot.be.null");
        }

        if (request == null) {
            throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.person.cannot.be.null");
        }

        super.setWhoRequested(AccessControl.getPerson());
        super.setDocumentRequest(request);
        super.setNewConclusionDateValue(conclusionDateValue);
        super.setNewDegreeDescriptionValue(degreeDescriptionValue);
        super.setWhenRequested(new DateTime());
        super.setNewGraduatedTitleValue(graduatedTitleValue);
    }

    @Atomic
    public static DocumentPrintRequest logRequest(String conclusionDateValue, String degreeDescriptionValue,
            String graduatedTitleValue, DocumentRequest request) {
        return new DocumentPrintRequest(conclusionDateValue, degreeDescriptionValue, graduatedTitleValue, request);
    }

    @Override
    public void setDocumentRequest(DocumentRequest documentRequest) {
        throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
    }

    @Override
    public void setNewConclusionDateValue(String newConclusionDateValue) {
        throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
    }

    @Override
    public void setNewDegreeDescriptionValue(String newDegreeDescriptionValue) {
        throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
    }

    @Override
    public void setNewGraduatedTitleValue(String newGraduatedTitleValue) {
        throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
    }

    @Override
    public void setWhenRequested(DateTime when) {
        throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
    }

    @Override
    public void setWhoRequested(Person whoRequested) {
        throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
    }

}
