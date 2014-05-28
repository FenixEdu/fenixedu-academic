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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class InfoSiteEvaluations extends DataTranferObject implements ISiteComponent {

    private static final ComparatorChain comparator = new ComparatorChain();
    static {
        comparator.addComparator(new BeanComparator("dayDate"));
        comparator.addComparator(new BeanComparator("beginningDate.time"));
        comparator.addComparator(new BeanComparator("endDate.time"));
    }

    private Collection<Evaluation> evaluations;

    public Collection<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Collection<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public Collection<WrittenEvaluation> getSortedWrittenEvaluations() {
        final Collection<WrittenEvaluation> sortedWrittenEvaluations = new TreeSet<WrittenEvaluation>(comparator);
        for (final Evaluation evaluation : getEvaluations()) {
            if (evaluation instanceof WrittenEvaluation) {
                sortedWrittenEvaluations.add((WrittenEvaluation) evaluation);
            }
        }
        return sortedWrittenEvaluations;
    }

}