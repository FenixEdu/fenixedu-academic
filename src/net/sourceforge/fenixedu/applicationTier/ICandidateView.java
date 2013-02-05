package net.sourceforge.fenixedu.applicationTier;

import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface ICandidateView {
    List getInfoApplicationInfos();

    boolean changeablePersonalInfo();
}