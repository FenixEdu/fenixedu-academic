package DataBeans;

import java.util.List;

/**
 * @author tfc130
 *  
 */
public class InfoShiftWithAssociatedInfoClassesAndInfoLessons extends InfoObject {

    private InfoShift infoShift;

    private List infoLessons;

    private List infoClasses;

    /**
     * Constructor for InfoShiftWithAssociatedInfoClassesAndInfoLessons.
     */
    public InfoShiftWithAssociatedInfoClassesAndInfoLessons() {
        super();
    }

    /**
     * Constructor for InfoShiftWithAssociatedInfoClassesAndInfoLessons.
     */
    public InfoShiftWithAssociatedInfoClassesAndInfoLessons(InfoShift infoShift, List infoLessons,
            List infoClasses) {
        super();
        setInfoClasses(infoClasses);
        setInfoLessons(infoLessons);
        setInfoShift(infoShift);
    }

    /**
     * Returns the infoClasses.
     * 
     * @return List
     */
    public List getInfoClasses() {
        return infoClasses;
    }

    /**
     * Returns the infoLessons.
     * 
     * @return List
     */
    public List getInfoLessons() {
        return infoLessons;
    }

    /**
     * Returns the infoShift.
     * 
     * @return InfoShift
     */
    public InfoShift getInfoShift() {
        return infoShift;
    }

    /**
     * Sets the infoClasses.
     * 
     * @param infoClasses
     *            The infoClasses to set
     */
    public void setInfoClasses(List infoClasses) {
        this.infoClasses = infoClasses;
    }

    /**
     * Sets the infoLessons.
     * 
     * @param infoLessons
     *            The infoLessons to set
     */
    public void setInfoLessons(List infoLessons) {
        this.infoLessons = infoLessons;
    }

    /**
     * Sets the infoShift.
     * 
     * @param infoShift
     *            The infoShift to set
     */
    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

}