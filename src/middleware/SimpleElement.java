package middleware;

import java.util.ArrayList;

public class SimpleElement implements Comparable {
    
    private int key;
    private String sKey;
    private ArrayList list;
    private boolean unique;
    
    /* Construtores */
    
    public SimpleElement() {
        this.sKey = null;
        this.list = null;
    }

    public SimpleElement(int key, Object o) {
        this.key = key;
        this.list = new ArrayList();
        this.list.add(o);
    }

    public SimpleElement(String sKey, Object o) {
        this.sKey = sKey;
        this.list = new ArrayList();
        this.list.add(o);
    }

    public SimpleElement(String sKey, ArrayList list) {
        this.sKey = sKey;
        this.list = list;
    }
    
    /* Selectores */
    
    public int getKey() {
        return key;
    }

    public String getSKey() {
        return sKey;
    }
    
    public ArrayList getList() {
        return list;
    }

    public boolean getUnique() {
        return unique;
    }
    
    /* Modificadores */
    
    public void setKey(int key) {
        this.key = key;
    }

    public void setSKey(String sKey) {
        this.sKey = sKey;
    }
    
    public void setList(ArrayList list) {
        this.list = list;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }
    
    public void add(Object obj) {
        this.list.add(obj);
    }
    
    /* Comparadores */
    
    public int compareTo(Object obj) {
        return getSKey().compareTo(((SimpleElement)obj).getSKey());
    }    
    
}
