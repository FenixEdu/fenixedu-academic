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
package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.messaging.core.domain.Message;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;

abstract public class PublicCandidacyHashCode extends PublicCandidacyHashCode_Base {

    protected PublicCandidacyHashCode() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    @Atomic
    public void sendEmail(final String fromSubject, final String body) {
        Message.fromSystem().replyToSender()
                .singleBcc(getEmail())
                .subject(fromSubject)
                .textBody(body)
                .send();
    }

    public boolean isFromDegreeOffice() {
        return false;
    }

    public boolean isFromPhdProgram() {
        return false;
    }

    public boolean isFromPhdReferee() {
        return false;
    }

    abstract public boolean hasCandidacyProcess();

    static public PublicCandidacyHashCode getPublicCandidacyCodeByHash(final String hash) {
        if (isEmpty(hash)) {
            return null;
        }

        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hash.equals(hashCode.getValue())) {
                return hashCode;
            }
        }

        return null;
    }

    protected static List<PublicCandidacyHashCode> getHashCodesAssociatedWithEmail(final String email) {
        final List<PublicCandidacyHashCode> result = new ArrayList<>();
        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hashCode.getEmail().equals(email)) {
                result.add(hashCode);
            }
        }
        return result;
    }

}
