package DataBeans;

/**
 * @author Fernanda Quitério
 *  
 */
public class InfoSiteInstructions extends DataTranferObject implements ISiteComponent {

    public boolean equals(Object objectToCompare) {

        return (objectToCompare instanceof InfoSiteInstructions);

    }
}