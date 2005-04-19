/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *
 */
public class PublicationTeacher extends PublicationTeacher_Base implements IPublicationTeacher {	

    private PublicationArea publicationArea;


	public PublicationArea getPublicationArea() {
		return publicationArea;
	}
	/**
	 * @param publicationArea The publicationArea to set.
	 */
	public void setPublicationArea(PublicationArea publicationArea) {
		this.publicationArea = publicationArea;
	}

}
