/*
 * Created on 20/Fev/2004
 */
package net.sourceforge.fenixedu.util;

import java.io.Serializable;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 20/Fev/2004
 *  
 */
public abstract class FenixValuedEnum extends ValuedEnum implements Serializable {

    /**
     * @param arg0
     * @param arg1
     */
    public FenixValuedEnum(String arg0, int arg1) {
        super(arg0, arg1);
    }

}