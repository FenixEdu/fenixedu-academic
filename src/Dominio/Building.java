package Dominio;

import java.util.List;

public class Building extends DomainObject implements IBuilding {

    private String name;
    private List rooms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getRooms() {
        return rooms;
    }

    public void setRooms(List rooms) {
        this.rooms = rooms;
    }

}
