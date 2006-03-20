/*
 * Created on Dec 12, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.LegalRegimenType;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public TeacherLegalRegimen() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean belongsToPeriod(Date beginDate, Date endDate) {
        return (!this.getBeginDate().after(endDate)
                && (this.getEndDate() == null || !this.getEndDate().before(beginDate)));
    }
    
    public boolean isActive(Date currentDate) {
        return (this.getEndDate() == null
                || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(), currentDate) || this
                        .getEndDate().after(currentDate)));
    }
    
    public void delete(){
        removeCategory();
        removeTeacher();
        super.deleteDomainObject();
    }
    
    public boolean isEndSituation() {
       return (this.getLegalRegimenType().equals(LegalRegimenType.DEATH)
                || this.getLegalRegimenType().equals(LegalRegimenType.EMERITUS)
                || this.getLegalRegimenType().equals(LegalRegimenType.RETIREMENT)
                || this.getLegalRegimenType().equals(LegalRegimenType.RETIREMENT_IN_PROGRESS)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS)                
                || this.getLegalRegimenType().equals(LegalRegimenType.CONTRACT_END)
                || this.getLegalRegimenType().equals(LegalRegimenType.DENUNCIATION)
                || this.getLegalRegimenType().equals(LegalRegimenType.IST_OUT_NOMINATION)
                || this.getLegalRegimenType().equals(LegalRegimenType.SERVICE_TURN_OFF)
                || this.getLegalRegimenType().equals(LegalRegimenType.TEMPORARY_SUBSTITUTION_CONTRACT_END)
                || this.getLegalRegimenType().equals(LegalRegimenType.EXONERATION)                
                || this.getLegalRegimenType().equals(LegalRegimenType.RESCISSION)
                || this.getLegalRegimenType().equals(LegalRegimenType.TRANSFERENCE));
     }               
}
