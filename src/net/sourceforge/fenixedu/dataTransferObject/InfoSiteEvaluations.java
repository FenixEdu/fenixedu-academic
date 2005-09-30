package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class InfoSiteEvaluations extends DataTranferObject implements ISiteComponent {

    private static final ComparatorChain comparator = new ComparatorChain();
    static {
        comparator.addComparator(new BeanComparator("dayDate"));
        comparator.addComparator(new BeanComparator("beginningDate.time"));
        comparator.addComparator(new BeanComparator("endDate.time"));
    }

    private List<IEvaluation> evaluations;

    public List<IEvaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<IEvaluation> evaluations) {
        this.evaluations = evaluations;
    } 

    public Collection<IWrittenEvaluation> getSortedWrittenEvaluations() {
        final Collection<IWrittenEvaluation> sortedWrittenEvaluations = new TreeSet<IWrittenEvaluation>(comparator);
        for (final IEvaluation evaluation : getEvaluations()) {
            if (evaluation instanceof IWrittenEvaluation) {
                sortedWrittenEvaluations.add((IWrittenEvaluation) evaluation);
            }
        }
        return sortedWrittenEvaluations;
    }

}