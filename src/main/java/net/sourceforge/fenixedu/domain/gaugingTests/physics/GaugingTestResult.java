/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.gaugingTests.physics;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 26/Nov/2003
 * 
 */
public class GaugingTestResult extends GaugingTestResult_Base {

    public GaugingTestResult() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Double getCf() {
        Long rounded = new Long(Math.round(Double.valueOf(getCfString().replace(',', '.')).doubleValue() * 10));
        return new Double(rounded.doubleValue() / 10);
    }

    public void setCf(Double cf) {
        setCfString(cf.toString());
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasP28() {
        return getP28() != null;
    }

    @Deprecated
    public boolean hasUnanswered() {
        return getUnanswered() != null;
    }

    @Deprecated
    public boolean hasP26() {
        return getP26() != null;
    }

    @Deprecated
    public boolean hasP27() {
        return getP27() != null;
    }

    @Deprecated
    public boolean hasP24() {
        return getP24() != null;
    }

    @Deprecated
    public boolean hasP25() {
        return getP25() != null;
    }

    @Deprecated
    public boolean hasP22() {
        return getP22() != null;
    }

    @Deprecated
    public boolean hasP23() {
        return getP23() != null;
    }

    @Deprecated
    public boolean hasP21() {
        return getP21() != null;
    }

    @Deprecated
    public boolean hasP20() {
        return getP20() != null;
    }

    @Deprecated
    public boolean hasTest() {
        return getTest() != null;
    }

    @Deprecated
    public boolean hasCorrect() {
        return getCorrect() != null;
    }

    @Deprecated
    public boolean hasP12() {
        return getP12() != null;
    }

    @Deprecated
    public boolean hasP11() {
        return getP11() != null;
    }

    @Deprecated
    public boolean hasP14() {
        return getP14() != null;
    }

    @Deprecated
    public boolean hasP13() {
        return getP13() != null;
    }

    @Deprecated
    public boolean hasP9() {
        return getP9() != null;
    }

    @Deprecated
    public boolean hasP16() {
        return getP16() != null;
    }

    @Deprecated
    public boolean hasP8() {
        return getP8() != null;
    }

    @Deprecated
    public boolean hasP15() {
        return getP15() != null;
    }

    @Deprecated
    public boolean hasP18() {
        return getP18() != null;
    }

    @Deprecated
    public boolean hasP17() {
        return getP17() != null;
    }

    @Deprecated
    public boolean hasP5() {
        return getP5() != null;
    }

    @Deprecated
    public boolean hasP4() {
        return getP4() != null;
    }

    @Deprecated
    public boolean hasP19() {
        return getP19() != null;
    }

    @Deprecated
    public boolean hasP7() {
        return getP7() != null;
    }

    @Deprecated
    public boolean hasP6() {
        return getP6() != null;
    }

    @Deprecated
    public boolean hasP1() {
        return getP1() != null;
    }

    @Deprecated
    public boolean hasP3() {
        return getP3() != null;
    }

    @Deprecated
    public boolean hasP2() {
        return getP2() != null;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasWrong() {
        return getWrong() != null;
    }

    @Deprecated
    public boolean hasCfString() {
        return getCfString() != null;
    }

    @Deprecated
    public boolean hasP10() {
        return getP10() != null;
    }

}
