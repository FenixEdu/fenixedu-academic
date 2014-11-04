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
package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.phd.ThesisSubject;

public class PhdThesisSubjectOrderBean implements Serializable {

    public static Comparator<PhdThesisSubjectOrderBean> COMPARATOR_BY_ORDER = new Comparator<PhdThesisSubjectOrderBean>() {
        @Override
        public int compare(PhdThesisSubjectOrderBean bean1, PhdThesisSubjectOrderBean bean2) {
            return bean1.getOrder() - bean2.getOrder();
        }
    };

    public PhdThesisSubjectOrderBean(int order, ThesisSubject thesisSubject) {
        this.order = order;
        this.thesisSubject = thesisSubject;
    }

    private static final long serialVersionUID = 1L;

    private int order;

    private ThesisSubject thesisSubject;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ThesisSubject getThesisSubject() {
        return thesisSubject;
    }

    public void setThesisSubject(ThesisSubject thesisSubject) {
        this.thesisSubject = thesisSubject;
    }

    public void increaseOrder() {
        order++;
    }

    public void decreaseOrder() {
        order--;
    }
}
