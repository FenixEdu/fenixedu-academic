/*
 * Created on 24/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 * 24/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteDegreeCurricularPlans  extends DataTranferObject implements ISiteComponent {
private List degreeCurricularPlans;


	/**
	 * 
	 */
	public InfoSiteDegreeCurricularPlans() {
	}

/**
 * @return
 */
public List getDegreeCurricularPlans() {
	return degreeCurricularPlans;
}

/**
 * @param degreeCurricularPlans
 */
public void setDegreeCurricularPlans(List degreeCurricularPlans) {
	this.degreeCurricularPlans = degreeCurricularPlans;
}

}
