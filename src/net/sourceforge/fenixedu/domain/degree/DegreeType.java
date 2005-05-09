/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.degree;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public enum DegreeType {
    
    DEGREE,
    
    MASTER_DEGREE;
    
    public String getName() {
        return name();
    }
 
}