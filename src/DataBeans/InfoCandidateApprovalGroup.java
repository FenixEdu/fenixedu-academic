package DataBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateApprovalGroup extends InfoObject {

    protected String situationName;

    protected List candidates;

    public InfoCandidateApprovalGroup() {
        this.candidates = new ArrayList();
    }

    /**
     * @return
     */
    public List getCandidates() {
        return candidates;
    }

    /**
     * @return
     */
    public String getSituationName() {
        return situationName;
    }

    /**
     * @param list
     */
    public void setCandidates(List list) {
        candidates = list;
    }

    /**
     * @param string
     */
    public void setSituationName(String string) {
        situationName = string;
    }

}