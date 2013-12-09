/*
 * @(#)RequestUtils.java Created on Oct 24, 2004
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Oct 24, 2004
 * @since 1.1
 * 
 */
public class RequestUtils {

    private static final boolean STORE_PENDING_REQUEST = FenixConfigurationManager.getConfiguration().getStorePendingRequest();

    public static String getAndSetStringToRequest(HttpServletRequest request, String name) {
        String parameter = request.getParameter(name);
        if (parameter == null) {
            parameter = (String) request.getAttribute(name);
        }
        request.setAttribute(name, parameter);
        return parameter;
    }

    public static Collection buildExecutionDegreeLabelValueBean(Collection executionDegrees) {
        final Map duplicateDegreesMap = new HashMap();
        for (Iterator iterator = executionDegrees.iterator(); iterator.hasNext();) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
            String degreeName = infoDegree.getNome();

            if (duplicateDegreesMap.get(degreeName) == null) {
                duplicateDegreesMap.put(degreeName, new Boolean(false));
            } else {
                duplicateDegreesMap.put(degreeName, new Boolean(true));
            }
        }

        Collection lableValueList = CollectionUtils.collect(executionDegrees, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
                //InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
                //InfoDegree infoDegree = infoDegreeCurricularPlan.getInfoDegree();
                /*
                TODO: DUPLICATE check really needed?
                StringBuilder label = new StringBuilder();
                label.append(infoDegree.getDegreeType().toString());
                label.append(" " + BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.in") + " ");
                label.append(infoDegree.getNome());
                if (((Boolean) duplicateDegreesMap.get(infoDegree.getNome())).booleanValue()) {
                    label.append(" - ");
                    label.append(infoDegreeCurricularPlan.getName());
                }*/

                String label =
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getDegreeCurricularPlan()
                                .getPresentationName(infoExecutionDegree.getInfoExecutionYear().getExecutionYear());

                String value = infoExecutionDegree.getExternalId().toString();

                return new LabelValueBean(label, value);
            }

        });

        Comparator comparator = new BeanComparator("label", Collator.getInstance());
        Collections.sort((List) lableValueList, comparator);

        return lableValueList;
    }

    public static final List<LabelValueBean> buildCurricularYearLabelValueBean() {
        final List<LabelValueBean> curricularYears = new ArrayList<LabelValueBean>();
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.RendererResources",
                "renderers.menu.default.title"), ""));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.EnumerationResources",
                "1.ordinal.short"), "1"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.EnumerationResources",
                "2.ordinal.short"), "2"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.EnumerationResources",
                "3.ordinal.short"), "3"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.EnumerationResources",
                "4.ordinal.short"), "4"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.EnumerationResources",
                "5.ordinal.short"), "5"));
        return curricularYears;
    }

    /**
     * Redirects the user to the login page saving the request state. This can
     * be used to force the user to login before a certain request is fulfilled.
     * 
     * @param request
     *            the current request
     * @param response
     *            the reponse were the redirect will be sent
     * 
     * @throws IOException
     *             when it's not possible to send the redirect to the client
     */
    public static void sendLoginRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.getSession(true);

        final PendingRequest pendingRequest = STORE_PENDING_REQUEST ? storeRequest(request) : null;
        response.sendRedirect(generateRedirectLink(
                FenixConfigurationManager.getHostRedirector().getRedirectPageLogin(request.getRequestURL().toString()),
                pendingRequest));
    }

    public static String generateRedirectLink(String url, PendingRequest pendingRequest) {
        return generateRedirectLink(url, pendingRequest == null ? null : pendingRequest.getExternalId());
    }

    public static String generateRedirectLink(String url, String externalId) {
        if (externalId == null || externalId.length() == 0 || externalId.equals("null")) {
            return url;
        }
        String param = "pendingRequest=" + externalId;
        if (url.contains("?")) {
            if (url.contains("&")) {
                return url + "&" + param;
            } else if (url.charAt(url.length() - 1) != '?') {
                return url + "&" + param;
            } else {
                return url + param;
            }
        } else {
            return url + "?" + param;
        }
    }

    @Atomic
    public static PendingRequest storeRequest(HttpServletRequest request) {
        return new PendingRequest(request);
    }

}
