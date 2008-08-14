/**
 * 
 */
package net.sourceforge.fenixedu.domain.curricularPeriod;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum CurricularPeriodType {

    FIVE_YEAR(5), THREE_YEAR(3), TWO_YEAR(2), YEAR(1), SEMESTER(0.5f), TRIMESTER(0.25f);

    private float weight;

    private CurricularPeriodType(float weight) {
	this.weight = weight;
    }

    public float getWeight() {
	return weight;
    }

    public String getAbbreviatedName() {
	return name() + ".ABBREVIATION";
    }

}
