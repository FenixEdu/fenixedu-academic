/*
 * @(#)RequestUtils.java Created on Oct 24, 2004
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

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

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;

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

            public Object transform(Object arg0) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
                InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
                InfoDegree infoDegree = infoDegreeCurricularPlan.getInfoDegree();

                StringBuffer label = new StringBuffer();
                label.append(infoDegree.getTipoCurso().toString());
                label.append(" em ");
                label.append(infoDegree.getNome());
                if (((Boolean) duplicateDegreesMap.get(infoDegree.getNome())).booleanValue()) {
                    label.append(" - ");
                    label.append(infoDegreeCurricularPlan.getName());
                }

                String value = infoExecutionDegree.getIdInternal().toString();

                return new LabelValueBean(label.toString(), value);
            }
            
        });

        Comparator comparator = new BeanComparator("label", Collator.getInstance());
        Collections.sort((List) lableValueList, comparator);

        return lableValueList;
    }

    public static final List buildCurricularYearLabelValueBean() {
        final List curricularYears = new ArrayList();
        curricularYears.add(new LabelValueBean("escolher", ""));
        curricularYears.add(new LabelValueBean("1 º", "1"));
        curricularYears.add(new LabelValueBean("2 º", "2"));
        curricularYears.add(new LabelValueBean("3 º", "3"));
        curricularYears.add(new LabelValueBean("4 º", "4"));
        curricularYears.add(new LabelValueBean("5 º", "5"));
        return curricularYears;
    }

}
