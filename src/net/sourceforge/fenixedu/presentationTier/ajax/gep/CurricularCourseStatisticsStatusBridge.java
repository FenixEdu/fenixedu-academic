/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.ajax.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseStatisticsStatusBridge {

    public static Map<IUserView, Collection<String>> processedDegreeCurricularPlans = new HashMap<IUserView, Collection<String>>();

    public static Map<IUserView, Collection<String>> processingDegreeCurricularPlans = new HashMap<IUserView, Collection<String>>();

    public static Map<IUserView, Collection<String>> toProcessDegreeCurricularPlans = new HashMap<IUserView, Collection<String>>();

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

    private static IUserView getUserVIew() {
	return UserView.getUser();
    }

}
