/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *
 */
public interface IPublicationTeacher extends IDomainObject{
	
	public ITeacher getTeacher();
    public void setTeacher(ITeacher teacher);
    public Integer getKeyTeacher();
    
    public PublicationArea getPublicationArea();
    public void setPublicationArea(PublicationArea publicationArea);

    public Integer getKeyPublication();
    public void setKeyPublication(Integer keyPublication);
    public IPublication getPublication();
    public void setPublication(IPublication publication);

}
