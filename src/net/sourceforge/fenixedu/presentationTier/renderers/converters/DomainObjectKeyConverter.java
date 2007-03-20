package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DomainObjectKeyConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        
        if (value == null || value.equals("")) {
            return null;
        }
        
        String key = (String) value;
        
        String[] parts = key.split(":");
        if (parts.length < 2) {
            throw new ConversionException("invalid key format: " + key);
        }
        
        try {
            Class keyType = Class.forName(parts[0]);
            Integer oid = Integer.parseInt(parts[1]);
            
            try {
                return RootDomainObject.getInstance().readDomainObjectByOID(keyType, oid);
            } catch (Exception e) {
                throw new ConversionException("could not get object with given key: " + key, e);
            }
        } catch (NumberFormatException e) {
            throw new ConversionException("invalid oid in key: " + key, e);
        } catch (ClassNotFoundException e) {
            throw new ConversionException("invalid type in key: " + key, e);
        }
    }

    public static String code(String type, String oid) {
        return type + ":" + oid;
    }

    public static String code(String type, Integer oid) {
        return code(type, oid.toString());
    }

    public static String code(Class type, Integer oid) {
        return code(type.getName(), oid.toString());
    }
    
    public static String code(DomainObject object) {
        return code(object.getClass(), object.getIdInternal());
    }

}
