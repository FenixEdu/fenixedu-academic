package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

/**
 * @author jpvl
 */
public class DayColumn {

    private Integer maxColisionSize;

    private int index;

    private String label;

    public DayColumn(int index, String label) {
        maxColisionSize = new Integer(1);
        this.index = index;
        this.label = label;
    }

    /**
     * Returns the maxColisionSize.
     * 
     * @return Integer
     */
    public Integer getMaxColisionSize() {
        return maxColisionSize;
    }

    /**
     * Sets the maxColisionSize.
     * 
     * @param maxColisionSize
     *            The maxColisionSize to set
     */
    public void setMaxColisionSize(Integer maxColisionSize) {
        this.maxColisionSize = new Integer(Math.max(maxColisionSize.intValue(), this.maxColisionSize
                .intValue()));
    }

    /**
     * Returns the index.
     * 
     * @return int
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index.
     * 
     * @param index
     *            The index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the label.
     * 
     * @return String
     */
    public String getLabel() {
        return label;
    }

}