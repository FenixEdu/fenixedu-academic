package net.sourceforge.fenixedu.tools.enrollment;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum AreaType {
    SPECIALIZATION,

    SECONDARY,

    BASE;
    public static List toLabelValueBeanList() {
      List result = new ArrayList();
      result.add(new LabelValueBean(AreaType.SPECIALIZATION.name(),AreaType.SPECIALIZATION.name() ));
      result.add(new LabelValueBean(AreaType.SECONDARY.name(), AreaType.SECONDARY.name()));
      result.add(new LabelValueBean(AreaType.BASE.name(), AreaType.BASE.name()));
      return result;
    }
    public String getName(){
        return name();
    }
}
