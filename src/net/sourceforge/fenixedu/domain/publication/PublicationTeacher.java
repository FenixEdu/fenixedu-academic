/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *
 */
public class PublicationTeacher extends DomainObject implements IPublicationTeacher {
	
	private Integer keyPublication;
    private Integer keyTeacher;
    private PublicationArea publicationArea;
    private ITeacher teacher;
    private IPublication publication;   
	
    
    /**
	 * 
	 */
	public PublicationTeacher() {	
	}

	/**
	 * @return Returns the keyPublication.
	 */
	public Integer getKeyPublication() {
		return keyPublication;
	}
	/**
	 * @param keyPublication The keyPublication to set.
	 */
	public void setKeyPublication(Integer keyPublication) {
		this.keyPublication = keyPublication;
	}
	/**
	 * @return Returns the keyTeacher.
	 */
	public Integer getKeyTeacher() {
		return keyTeacher;
	}
	/**
	 * @param keyTeacher The keyTeacher to set.
	 */
	public void setKeyTeacher(Integer keyTeacher) {
		this.keyTeacher = keyTeacher;
	}
	/**
	 * @return Returns the publication.
	 */
	public IPublication getPublication() {
		return publication;
	}
	/**
	 * @param publication The publication to set.
	 */
	public void setPublication(IPublication publication) {
		this.publication = publication;
	}
	/**
	 * @return Returns the publicationArea.
	 */
	public PublicationArea getPublicationArea() {
		return publicationArea;
	}
	/**
	 * @param publicationArea The publicationArea to set.
	 */
	public void setPublicationArea(PublicationArea publicationArea) {
		this.publicationArea = publicationArea;
	}
	/**
	 * @return Returns the teacher.
	 */
	public ITeacher getTeacher() {
		return teacher;
	}
	/**
	 * @param teacher The teacher to set.
	 */
	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
	}
}
