/*
 * Created on Nov 13, 2003
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerType extends FenixValuedEnum {

    public static final int PROFESSIONAL_TYPE = 1;

    public static final int TEACHING_TYPE = 2;

    public static CareerType PROFESSIONAL = new CareerType("Professional", PROFESSIONAL_TYPE);

    public static CareerType TEACHING = new CareerType("Teaching", TEACHING_TYPE);

    public CareerType(String name, int value) {
        super(name, value);
    }

    public static CareerType getEnum(String careerType) {
        return (CareerType) getEnum(CareerType.class, careerType);
    }

    public static CareerType getEnum(int careerType) {
        return (CareerType) getEnum(CareerType.class, careerType);
    }

    public static Map getEnumMap() {
        return getEnumMap(CareerType.class);
    }

    public static List getEnumList() {
        return getEnumList(CareerType.class);
    }

    public static Iterator iterator() {
        return iterator(CareerType.class);
    }

    public String toString() {
        String result = "Career Type :\n";
        result += "\n  - Career Type : " + this.getName();

        return result;
    }
}