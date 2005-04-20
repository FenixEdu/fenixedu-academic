/*
 * Created on May 5, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Calendar;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantContractRegime extends GrantContractRegime_Base {

    public Boolean getContractRegimeActive() {
        if (getDateEndContract().after(Calendar.getInstance().getTime())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
