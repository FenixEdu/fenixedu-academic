package DataBeans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernanda Quitério
 * 10/Jan/2004
 *
 */
public class InfoPaymentPhase extends InfoObject implements Serializable
{
	private Date startDate;
	private Date endDate;
	private Float value;
	private String description;

	private InfoGratuityValues infoGratuityValues;
	
}
