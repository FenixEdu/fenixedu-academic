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
package org.fenixedu.academic.domain.alumni;

import java.text.Collator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.CerimonyInquiryGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CerimonyInquiry extends CerimonyInquiry_Base implements Comparable<CerimonyInquiry> {

    public CerimonyInquiry() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setBegin(new DateTime().plusDays(1));
        setEnd(getBegin().plusDays(15));
    }

    @Override
    public int compareTo(final CerimonyInquiry cerimonyInquiry) {
        if (getDescription() == null || cerimonyInquiry.getDescription() == null) {
            return 1;
        }
        final int c = Collator.getInstance().compare(getDescription(), cerimonyInquiry.getDescription());
        return c == 0 ? getExternalId().compareTo(cerimonyInquiry.getExternalId()) : c;
    }

    public SortedSet<CerimonyInquiryAnswer> getOrderedCerimonyInquiryAnswer() {
        return new TreeSet<CerimonyInquiryAnswer>(getCerimonyInquiryAnswerSet());
    }

    @Atomic
    public static CerimonyInquiry createNew() {
        return new CerimonyInquiry();
    }

    @Atomic
    public CerimonyInquiryAnswer createNewAnswer() {
        return new CerimonyInquiryAnswer(this);
    }

    @Atomic
    public void addPeople(final Set<String> usernames) {
        for (final String username : usernames) {
            final User user = User.findByUsername(username);
            if (user != null) {
                final Person person = user.getPerson();
                if (!containsPerson(person)) {
                    new CerimonyInquiryPerson(this, person);
                }
            }
        }
    }

    private boolean containsPerson(final Person person) {
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : person.getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.getCerimonyInquiry() == this) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public void delete() {
        if (getGroup() != null) {
            throw new DomainException("error.cerimonyInquiry.cannotDeleteCerimonyInquiryUsedInAccessControl");
        }
        for (final CerimonyInquiryAnswer cerimonyInquiryAnswer : getCerimonyInquiryAnswerSet()) {
            cerimonyInquiryAnswer.delete();
        }
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : getCerimonyInquiryPersonSet()) {
            cerimonyInquiryPerson.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean isOpen() {
        return getBegin() != null && getBegin().isBeforeNow() && (getEnd() == null || getEnd().isAfterNow());
    }

    public Recipient createRecipient() {
        return Recipient.newInstance("Inquiridos: " + getDescription(), CerimonyInquiryGroup.get(this));
    }

    @Atomic
    public void toggleObservationFlag() {
        final Boolean allowComments = getAllowComments();
        final boolean value = !(allowComments != null && allowComments.booleanValue());
        setAllowComments(Boolean.valueOf(value));
    }

}
