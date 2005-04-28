/*
 * Created on Apr 18, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

public class EnumTagLib extends BodyTagSupport {

    private final static Map<String, Map<String, Collection<LabelValueBean>>> labelValueBeansMap = new HashMap<String, Map<String, Collection<LabelValueBean>>>();

    private String id;

    private String enumeration;

    private String locale = Globals.LOCALE_KEY;

    private String bundle;

    protected static MessageResources messages = MessageResources
            .getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    public int doStartTag() throws JspException {
        Collection<LabelValueBean> labelValueBeans = getLabelValues();
        pageContext.getRequest().setAttribute(id, labelValueBeans);
        // pageContext.getRequest().setAttribute(id, new ArrayList());

        return super.doStartTag();
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Collection<LabelValueBean> getLabelValues() {
        Map<String, Collection<LabelValueBean>> map = labelValueBeansMap.get(this.enumeration);
        if (map == null) {
            map = new HashMap<String, Collection<LabelValueBean>>();
            labelValueBeansMap.put(this.enumeration, map);
        } else {
            Collection<LabelValueBean> labelValueBeans = map.get(this.locale + this.bundle);
            if (labelValueBeans != null) {
                return labelValueBeans;
            }
        }

        Collection<LabelValueBean> labelValueBeans = getLabelValuesLookup();
        map.put(this.locale + this.bundle, labelValueBeans);

        return labelValueBeans;
    }

    public Collection<LabelValueBean> getLabelValuesLookup() {
        try {
            Class clazz = Class.forName(this.enumeration);
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("Expected an enum type, got: " + this.enumeration);
            }

            final Method method = clazz.getMethod("values", (Class[]) null);
            final Object[] objects = (Object[]) method.invoke(clazz, (Object[]) null);
            final Collection<LabelValueBean> labelValueBeans = new ArrayList<LabelValueBean>(
                    objects.length);
            for (int i = 0; i < objects.length; i++) {
                String message = RequestUtils.message(pageContext, this.bundle,
                        this.locale, objects[i].toString(), null);

                labelValueBeans.add(new LabelValueBean(message,
                        objects[i].toString()));
            }
            return labelValueBeans;
        } catch (Exception e) {
            throw new IllegalArgumentException("Expected an enum type, got: " + this.enumeration);
        }

    }

}
