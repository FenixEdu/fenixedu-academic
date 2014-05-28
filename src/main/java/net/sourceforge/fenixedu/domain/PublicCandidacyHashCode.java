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
package net.sourceforge.fenixedu.domain;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

abstract public class PublicCandidacyHashCode extends PublicCandidacyHashCode_Base {

    protected PublicCandidacyHashCode() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    @Atomic
    public void sendEmail(final String fromSubject, final String body) {
        SystemSender systemSender = getRootDomainObject().getSystemSender();
        new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, fromSubject, body, getEmail());
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
        final List<PublicCandidacyHashCode> result = new ArrayList<PublicCandidacyHashCode>();
        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hashCode.getEmail().equals(email)) {
                result.add(hashCode);
            }
        }
        return result;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEmail() {
        return getEmail() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

}
