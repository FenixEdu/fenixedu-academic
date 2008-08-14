package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collection;
import java.util.List;
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

    private List<Evaluation> evaluations;

    public List<Evaluation> getEvaluations() {
	return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
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