package Dominio;

/**
 * @author Tânia Pousão Create on 10/Nov/2003
 */
public class Campus extends DomainObject implements ICampus {
    private String name;

    public Campus() {
        super();
    }

    public Campus(Integer campusId) {
        super(campusId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ICampus) {
            ICampus campus = (ICampus) obj;
            result = getName().equals(campus.getName());
        }
        return result;
    }

    public String toString() {
        String result = "[INFODEGREE_INFO:";
        result += " codigo interno= " + getIdInternal();
        result += " name= " + getName();
        result += "]";
        return result;
    }
}