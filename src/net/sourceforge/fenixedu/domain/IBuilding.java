package net.sourceforge.fenixedu.domain;

import java.util.List;

public interface IBuilding extends IDomainObject {

    public String getName();

    public void setName(String name);

    public List getRooms();

    public void setRooms(List rooms);

}
