package net.sourceforge.fenixedu.util;

import org.apache.commons.codec.digest.DigestUtils;

public class FenixDigestUtils {

    public static String createDigest(String data){
        
            /*
            
            try {
            
            byte[] hash = DigestUtils.sha(data);
            
            // System.out.println(hash.length);
            
            BigDecimal bigDecimal = new BigDecimal("17");
            BigDecimal bigDecimal2 = new BigDecimal("101");

            for(int i = hash.length; i > 0; i--) {
                short a = (short) (0xFF & hash[i - 1]);
                // System.out.println(a);
                bigDecimal = bigDecimal.multiply(bigDecimal2).add(new BigDecimal(Long.valueOf((long)a * (long)i).toString()));
                
            }
            
            BigDecimal result = bigDecimal.remainder(new BigDecimal("99997"));
            
            System.out.println(res);
            
            double res2 = res % ((double) 99997);
            
            return Integer.toString((int) res2);
            
            return result.abs().toPlainString();
            
    
            } catch(Exception exception) {
                throw new RuntimeException(exception);
            } */
            
            return DigestUtils.shaHex(data);
        }
    
    public static void main(String[] args) {
           String res = FenixDigestUtils.createDigest("1qq asdsad1a zxfgsdf df 2s2ds2ah hdfg 12gdyh 12t rwdfg2sfg ty fdgh 13ert11yertyhsg  asdsadsa sdr sw1rt t s");
            System.out.println(res);
        
    }
    
}
