package net.sourceforge.fenixedu.renderers.model;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.renderers.utils.ClassHierarchyTable;

import org.apache.log4j.Logger;

// TODO: This needs revision. It's problably an horrible way of creating default values for types
public class DefaultValues {
    
    private static Logger logger = Logger.getLogger(DefaultValues.class);
    
    protected static DefaultValues instance;

    public static abstract class ValueCreator {
        public abstract Object createValue(Class type, String defaultValue);
    }
    
    private static ClassHierarchyTable<ValueCreator> defaultValues = new ClassHierarchyTable<ValueCreator>();
    
    protected DefaultValues() {
        Method[] methods = getClass().getMethods();
        
        for (int i = 0; i < methods.length; i++) {
            final Method m = methods[i];
            
            if (m.getName().startsWith("createValue")) {
                Class[] parameters = m.getParameterTypes();
                
                if (parameters.length == 3 && parameters[1].equals(Class.class) && parameters[2].equals(String.class)) {
                    Class type = parameters[0];
                    
                    try {
                        registerCreator(type, new ValueCreator() {
                            @Override
                            public Object createValue(Class type, String defaultValue) {
                                try {
                                    return m.invoke(DefaultValues.this, new Object[] { null, type, defaultValue });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                
                                return null;
                            }                          
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    //
    // public interface
    //
    
    public void registerCreator(Class type, ValueCreator creator) {
        defaultValues.put(type, creator);
        if (LogLevel.INFO) {
            logger.info("adding default value for type: " + type.getName());
        }
    }

    public Object createValue(Class type) {
        return defaultValues.get(type).createValue(type, null);
    }

    public Object createValue(Class type, String defaultValue) {
        return defaultValues.get(type).createValue(type, defaultValue);
    }
    
    public static DefaultValues getInstance() {
        if (DefaultValues.instance == null) {
            DefaultValues.instance = new DefaultValues();
        }
        
        return DefaultValues.instance;
    }
    
    //
    // Default creators
    //
    // Add a new default value: create a public method named createValue that takes 3 arguments. The first 
    // is an argument of the type that will be created. The second is the class of the actual type for which 
    // the value is required. The third argument is a string representing the default that should be used
    // when creating the new value.
    //
    //The first argument will always have the null value when the method is called. 
    
    private boolean isEmptyString(String value) {
        return value == null || value.length() == 0;
    }
    
    public Object createValue(Object o, Class type, String defaultValue) throws InstantiationException, IllegalAccessException {
        return null;//type.newInstance();
    }
    
    public String createValue(String s, Class type, String defaultValue) {
        return defaultValue != null ? defaultValue : "";
    }
    
    public Number createValue(Number n, Class type, String defaultValue) {
        if (isEmptyString(defaultValue)) {
            return null;
        }
        
        try {
            return new Integer(defaultValue != null ? defaultValue : "0");
        } catch (NumberFormatException e) {
            try {
                return new Float(defaultValue != null ? defaultValue : "0.0");
            } catch (NumberFormatException e1) {
                if (LogLevel.WARN) {
                    logger.warn("could not create number from default value '" + defaultValue + "': " + e1);
                }
                e1.printStackTrace();
            }
        }
        
        return new Integer(0);
    }
    
    public Integer createValue(Integer i, Class type, String defaultValue) {
        if (isEmptyString(defaultValue)) {
            return null;
        }

        try {
            return new Integer(defaultValue != null ? defaultValue : "0");
        } catch (NumberFormatException e) {
            if (LogLevel.WARN) {
                logger.warn("could not create integer from default value '" + defaultValue + "': " + e);
            }
        }
        
        return new Integer(0);
    }

    public Float createValue(Float n, Class type, String defaultValue) {
        if (isEmptyString(defaultValue)) {
            return null;
        }

        try {
            return new Float(defaultValue != null ? defaultValue : "0.0");
        } catch (NumberFormatException e) {
            if (LogLevel.WARN) {
                logger.warn("could not create float from default value '" + defaultValue + "': " + e);
            }
        }
        
        return new Float(0.0f);
    }

    public Boolean createValue(Boolean b, Class type, String defaultValue) {
        if (isEmptyString(defaultValue)) {
            return null;
        }

        return new Boolean(defaultValue != null ? defaultValue : "false");
    }
    
    public Date createValue(Date d, Class type, String defaultValue) {
        if (isEmptyString(defaultValue)) {
            return null;
        }

        if (defaultValue != null) {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(defaultValue);
            } catch (ParseException e) {
                if (LogLevel.WARN) {
                    logger.warn("could not create date from default value '" + defaultValue + "': " + e);
                }
                e.printStackTrace();
            }
        }
        
        return new Date();
    }
    
    public Enum createValue(Enum e, Class type, String defaultValue) {
        if (isEmptyString(defaultValue)) {
            return null;
        }

        Object[] constants = type.getEnumConstants();
        
        if (defaultValue != null) {
            for (int i = 0; i < constants.length; i++) {
                if (constants[i].toString().equals(defaultValue)) {
                    return (Enum) constants[i];
                }
            }
        }

        return null;
    }
}
