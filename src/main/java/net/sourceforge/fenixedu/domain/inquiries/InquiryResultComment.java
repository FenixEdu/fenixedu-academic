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
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.domain.Bennu;

public class InquiryResultComment extends InquiryResultComment_Base {

    public InquiryResultComment(InquiryResult questionResult, Person person, ResultPersonCategory personCategory,
            Integer resultOrder) {
        super();
        setGeneralAttributes(person, personCategory, resultOrder);
        setInquiryResult(questionResult);
    }

    public InquiryResultComment(InquiryGlobalComment globalComment, Person person, ResultPersonCategory personCategory,
            Integer resultOrder, String comment) {
        super();
        setGeneralAttributes(person, personCategory, resultOrder);
        setInquiryGlobalComment(globalComment);
        setComment(comment);
    }

    private void setGeneralAttributes(Person person, ResultPersonCategory personCategory, Integer resultOrder) {
        setRootDomainObject(Bennu.getInstance());
        setPerson(person);
        setPersonCategory(personCategory);
        setResultOrder(resultOrder);
    }

    public void delete() {
        setInquiryGlobalComment(null);
        setInquiryResult(null);
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasInquiryResult() {
        return getInquiryResult() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasResultOrder() {
        return getResultOrder() != null;
    }

    @Deprecated
    public boolean hasComment() {
        return getComment() != null;
    }

    @Deprecated
    public boolean hasInquiryGlobalComment() {
        return getInquiryGlobalComment() != null;
    }

    @Deprecated
    public boolean hasPersonCategory() {
        return getPersonCategory() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
