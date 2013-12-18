/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.ajax.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseStatisticsStatusBridge {

    public static Map<User, Collection<String>> processedDegreeCurricularPlans =
            new HashMap<User, Collection<String>>();

    public static Map<User, Collection<String>> processingDegreeCurricularPlans =
            new HashMap<User, Collection<String>>();

    public static Map<User, Collection<String>> toProcessDegreeCurricularPlans =
            new HashMap<User, Collection<String>>();

    public static Collection<String> readProcessedDegreeCurricularPlans() {
        Collection<String> degreeCurricularPlans = processedDegreeCurricularPlans.get(getUserVIew());
        return (degreeCurricularPlans != null) ? degreeCurricularPlans : new ArrayList<String>();
    }

    public static Collection<String> readProcessingDegreeCurricularPlans() {
        Collection<String> degreeCurricularPlans = processingDegreeCurricularPlans.get(getUserVIew());
        return (degreeCurricularPlans != null) ? degreeCurricularPlans : new ArrayList<String>();
    }

    public static Collection<String> readToProcessDegreeCurricularPlans() {
        Collection<String> degreeCurricularPlans = toProcessDegreeCurricularPlans.get(getUserVIew());
        return (degreeCurricularPlans != null) ? degreeCurricularPlans : new ArrayList<String>();
    }

    private static User getUserVIew() {
        return Authenticate.getUser();
    }

}
