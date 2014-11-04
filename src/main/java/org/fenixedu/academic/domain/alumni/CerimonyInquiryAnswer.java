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
        if (getCerimonyInquiryPersonSet().isEmpty()) {
            setCerimonyInquiry(null);
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

}
