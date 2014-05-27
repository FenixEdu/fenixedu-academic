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
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Oct 24, 2004
 * @since 1.1
 * 
 */
public class RequestUtils {

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
                label.append(" " + BundleUtil.getStringFromResourceBundle(BundleUtil.APPLICATION_BUNDLE, "label.in") + " ");
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
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle(BundleUtil.ENUMERATION_BUNDLE,
                "1.ordinal.short"), "1"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle(BundleUtil.ENUMERATION_BUNDLE,
                "2.ordinal.short"), "2"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle(BundleUtil.ENUMERATION_BUNDLE,
                "3.ordinal.short"), "3"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle(BundleUtil.ENUMERATION_BUNDLE,
                "4.ordinal.short"), "4"));
        curricularYears.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle(BundleUtil.ENUMERATION_BUNDLE,
                "5.ordinal.short"), "5"));
        return curricularYears;
    }

    public static void sendLoginRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession(true);
        response.sendRedirect(FenixConfigurationManager.getConfiguration().getLoginPage());
    }

}
