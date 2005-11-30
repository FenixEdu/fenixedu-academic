/**
 * 
 */
package net.sourceforge.fenixedu.util;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum ContentType {

    JPG, GIF, PNG;

    public String getMimeType() {

        ContentType type = valueOf(name());

        switch (type) {
        case JPG:
            return "image/jpeg";
        case GIF:
            return "image/gif";
        case PNG:
            return "image/png";            
        default:
            return "*/*";
        }

    }
    
    public static ContentType getContentType(String httpContentType){
        
        if(httpContentType.toLowerCase().equals("image/gif")){
            return GIF;
        }
        if(httpContentType.toLowerCase().equals("image/jpeg") || httpContentType.toLowerCase().equals("image/jpg")){
            return JPG;
        }
        if(httpContentType.toLowerCase().equals("image/png")){
            return PNG;
        }
        
        return null;
    }

}
