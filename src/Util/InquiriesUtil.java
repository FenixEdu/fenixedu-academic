/*
 * Created on Nov 30, 2004
 * 
 */
package Util;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesUtil extends FenixUtil {
    
    public static String formatAnswer(Double answer) {
        String result = "-";
        double ans = answer.doubleValue();
        
        if((answer != null) && (ans > 0)) {
            result = Math.floor(ans) == ans ?
                    String.valueOf(answer.intValue()) : String.valueOf(ans);
        }
        return result;
    }
    
    public static String formatAnswer(Integer answer) {
        String result = "-";
        int ans = answer.intValue();
        
        if((answer != null) && (ans > 0))
            result = "" + ans;
        
        return result;
    }

    
    public static String formatAnswer(String str) {
        return ((str == null || str.length() == 0) ? "-" : str);
    }
    
    public static String formatAnswer(Double answer, Double repQuota, Double minRepQuota) {
        if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
            return formatAnswer(answer);

        } else {
            return "-";
        }
    }
    
    public static String formatAnswer(String answer, Double repQuota, Double minRepQuota) {
        if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
            return formatAnswer(answer);

        } else {
            return "-";
        }
    }
    
    
    public static String getTdClass(Double val, final String[] classes, final String defaultClass, final double[] values) {
        String classVal = defaultClass;

        if(val == null)
            return classVal;
        
        if(classes.length == (values.length-1)) {
            double v = val.doubleValue();
            for(int i = 0; i < classes.length; i++) {
                if((v >= values[i]) && (v < values[i+1])) {
                    classVal = classes[i];
                    break;
                }
            }
            
        }
        return classVal;
    }
    
    public static String getTdClass(String val, final String[] classes, final String defaultClass, final String[] values) {
        String classVal = defaultClass;

        if(val == null)
            return classVal;
        
        if(classes.length == values.length) {
            for(int i = 0; i < classes.length; i++) {
                if(val.equalsIgnoreCase(values[i])) {
                    classVal = classes[i];
                    break;
                }
            }
            
        }
        return classVal;
        
    }
    
    public static String getTdClass(Double val, final String[] classes, final String defaultClass,
            						final double[] values, Double repQuota, Double minRepQuota) {

        if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
            return getTdClass(val, classes, defaultClass, values);

        } else {
            return defaultClass;
        }
    }

    public static String getTdClass(String val, final String[] classes, final String defaultClass,
            						final String[] values, Double repQuota, Double minRepQuota) {

		if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
		    return getTdClass(val, classes, defaultClass, values);
		
		} else {
		    return defaultClass;
		}
}

}
