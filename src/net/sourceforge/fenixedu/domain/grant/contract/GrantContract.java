/*
 * Created on 18/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantContract extends GrantContract_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IGrantContract) {
            final IGrantContract grantContract = (IGrantContract) obj;
            return this.getIdInternal().equals(grantContract.getIdInternal());
        }
        return false;
    }
	
}