package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

public class PathElementsProvider {

    private String[] elements;
    private int index;

    public PathElementsProvider(String path) {
        super();
        
        this.elements = path.split("/");
        this.index = 0;
    }
    
    public int getElementCount() {
        return this.elements.length;
    }

    public String peek(int offset) {
        if (this.index + offset >= getElementCount()) {
            return null;
        }
        else {
            return this.elements[this.index + offset];
        }
    }
    
    public String current() {
        return peek(0);
    }
    
    public boolean hasNext() {
        return peek(1) != null;
    }
    
    /**
     * Advances the path provider and returns the current element, that is, the
     * same as {@link #current()} before the class to {@link #next()}.
     * 
     * @return the current element
     */
    public String next() {
        String next = current();
        this.index++;
        
        return next;
    }
    
}
