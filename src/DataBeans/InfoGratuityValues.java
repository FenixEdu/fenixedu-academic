package DataBeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Fernanda Quitério
 * 10/Jan/2004
 *
 */
public class InfoGratuityValues extends InfoObject implements Serializable
{
	private Double anualValue;
	private Double scholarShipValue;
	private Double finalProofValue;
	private Double courseValue;
	private Double creditValue;
	private Boolean proofRequestPayment;
	private Date startPayment;
	private Date endPayment;
	
	private InfoExecutionDegree infoExecutionDegree;
	
	private InfoEmployee infoEmployee;
	
	private List infoPaymentPhases;
	
}
