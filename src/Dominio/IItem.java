/*
 * IItem.java
 *
 * Created on 21 de Agosto de 2002, 23:09
 */

package Dominio;

import fileSuport.INode;

/**
 * 
 * @author ars
 */
public interface IItem extends IDomainObject, INode {
    public String getName();

    public ISection getSection();

    public Integer getItemOrder();

    public String getInformation();

    public Boolean getUrgent();

    public void setName(String name);

    public void setSection(ISection section);

    public void setItemOrder(Integer order);

    public void setInformation(String information);

    public void setUrgent(Boolean urgent);

}