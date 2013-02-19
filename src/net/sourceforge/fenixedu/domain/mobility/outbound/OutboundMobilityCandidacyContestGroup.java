package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.commons.Transformer;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.StringUtils;

public class OutboundMobilityCandidacyContestGroup extends OutboundMobilityCandidacyContestGroup_Base implements
        Comparable<OutboundMobilityCandidacyContestGroup> {

    public OutboundMobilityCandidacyContestGroup(final OutboundMobilityCandidacyContest contest,
            final Set<ExecutionDegree> executionDegrees) {
        if (contest == null) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.contest");
        }
        if (executionDegrees.isEmpty()) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.execution.degree");
        }
        setRootDomainObject(RootDomainObject.getInstance());
        addOutboundMobilityCandidacyContest(contest);
        getExecutionDegreeSet().addAll(executionDegrees);

        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        getOutboundMobilityCandidacyContestCount();
        getExecutionDegreeCount();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyContestGroup o) {
        final int dc = getDescription().compareTo(o.getDescription());
        return dc == 0 ? getExternalId().compareTo(o.getExternalId()) : dc;
    }

    public String getDescription() {
        return StringUtils.join(getSortedExecutionDegrees(), ", ", new Transformer<ExecutionDegree, String>() {
            @Override
            public String transform(final ExecutionDegree executionDegree) {
                return executionDegree.getDegree().getSigla();
            }
        });
    }

    public SortedSet<ExecutionDegree> getSortedExecutionDegrees() {
        final SortedSet<ExecutionDegree> result = new TreeSet<ExecutionDegree>(ExecutionDegree.COMPARATOR_BY_DEGREE_CODE);
        result.addAll(getExecutionDegreeSet());
        return result;
    }

}
