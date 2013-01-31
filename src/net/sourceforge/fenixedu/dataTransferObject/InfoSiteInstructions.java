package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author Fernanda Quitério
 * 
 */
public class InfoSiteInstructions extends DataTranferObject implements ISiteComponent {

	@Override
	public boolean equals(Object objectToCompare) {

		return (objectToCompare instanceof InfoSiteInstructions);

	}
}