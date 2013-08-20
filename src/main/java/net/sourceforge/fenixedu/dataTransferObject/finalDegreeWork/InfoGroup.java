/*
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGroup extends InfoObject {

    private final FinalDegreeWorkGroup groupDomainReference;

    public InfoGroup(final FinalDegreeWorkGroup group) {
        groupDomainReference = group;
    }

    public static InfoGroup newInfoFromDomain(final FinalDegreeWorkGroup group) {
        return group == null ? null : new InfoGroup(group);
    }

    public FinalDegreeWorkGroup getGroup() {
        return groupDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoGroup && getGroup() == ((InfoGroup) obj).getGroup();
    }

    @Override
    public int hashCode() {
        return getGroup().hashCode();
    }

    @Override
    public String getExternalId() {
        return getGroup().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    @Override
    public String toString() {
        return getGroup().toString();
    }

    /**
     * @return Returns the executionDegree.
     */
    public InfoExecutionDegree getExecutionDegree() {
        return InfoExecutionDegree.newInfoFromDomain(getGroup().getExecutionDegree());
    }

    /**
     * @return Returns the groupProposals.
     */
    public List<InfoGroupProposal> getGroupProposals() {
        List<InfoGroupProposal> result = new ArrayList<InfoGroupProposal>();

        for (final GroupProposal groupProposal : getGroup().getGroupProposals()) {
            result.add(InfoGroupProposal.newInfoFromDomain(groupProposal));
        }

        return result;
    }

    /**
     * @return Returns the groupStudents.
     */
    public List<InfoGroupStudent> getGroupStudents() {
        List<InfoGroupStudent> result = new ArrayList<InfoGroupStudent>();

        for (final GroupStudent groupStudent : getGroup().getGroupStudents()) {
            result.add(InfoGroupStudent.newInfoFromDomain(groupStudent));
        }

        return result;
    }

}
