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
package net.sourceforge.fenixedu.domain.alumni;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class CerimonyInquiryAnswer extends CerimonyInquiryAnswer_Base implements Comparable<CerimonyInquiryAnswer> {

    @Override
    public int compareTo(final CerimonyInquiryAnswer cerimonyInquiryAnswer) {
        final int c = getOrder().compareTo(cerimonyInquiryAnswer.getOrder());
        return c == 0 ? getExternalId().compareTo(cerimonyInquiryAnswer.getExternalId()) : c;
    }

    public CerimonyInquiryAnswer(final CerimonyInquiry cerimonyInquiry) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCerimonyInquiry(cerimonyInquiry);
    }

    @Override
    public void setCerimonyInquiry(final CerimonyInquiry cerimonyInquiry) {
        super.setCerimonyInquiry(cerimonyInquiry);
        if (cerimonyInquiry == null) {
            setOrder(Integer.valueOf(0));
        } else {
            setOrder(Integer.valueOf(cerimonyInquiry.getCerimonyInquiryAnswerSet().size()));
        }
    }

    public Integer getOrder() {
        return getAnswerOrder();
    }

    public void setOrder(Integer order) {
        setAnswerOrder(order);
    }

    @Atomic
    public void delete() {
        if (!hasAnyCerimonyInquiryPerson()) {
            setCerimonyInquiry(null);
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson> getCerimonyInquiryPerson() {
        return getCerimonyInquiryPersonSet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiryPerson() {
        return !getCerimonyInquiryPersonSet().isEmpty();
    }

    @Deprecated
    public boolean hasText() {
        return getText() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCerimonyInquiry() {
        return getCerimonyInquiry() != null;
    }

    @Deprecated
    public boolean hasAnswerOrder() {
        return getAnswerOrder() != null;
    }

}
