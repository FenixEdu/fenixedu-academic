package relations;

import net.sourceforge.fenixedu.domain.publication.IAuthorship;

public class PublicationAuthorship extends PublicationAuthorship_Base {

	/*
	 * This method is responsible for, after removing an authorship from a publication, having all 
	 * the others authorships associated with the same publication have their order rearranged.
	 * @param publicationAuthorship the authorship being removed from the publication
	 * @param publication the publication from whom the authorship will be removed
	 * @see relations.PublicationAuthorship_Base#remove(net.sourceforge.fenixedu.domain.publication.IAuthorship, net.sourceforge.fenixedu.domain.publication.IPublication)
	 */
    public static void remove(net.sourceforge.fenixedu.domain.publication.IAuthorship publicationAuthorships, net.sourceforge.fenixedu.domain.publication.IPublication publication) {
    	PublicationAuthorship_Base.remove(publicationAuthorships, publication);
	int removedOrder = publicationAuthorships.getOrder();
	if (publication != null) {
	    for(IAuthorship authorship : publication.getPublicationAuthorships()) {
		if (authorship.getOrder() > removedOrder) {
		    authorship.setOrder(authorship.getOrder()-1);
		}
	    }
	}
    }
}
