package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.IntegerNumberConverter;

/**
 * This renderer provides a simple way of doing the input of a integer number.
 * The number is read form a text input field and parsed with
 * {@link Integer#parseInt(java.lang.String, int)} were the 
 * second argument is the value given in the {@linkplain #setBase(int) base}
 * property.
 *  
 * <p>
 * Example:
 *  <input type="text" value="12345"/>
 * 
 * @author cfgi
 */
public class IntegerInputRenderer extends NumberInputRenderer {

    private int base = 10;
    
    /**
     * The base in which the number should be interpreted. For instance,
     * if <tt>base</tt> is 16 then an input like <tt>CAFE</tt> will be 
     * interpreted as 51966.
     * 
     * @property
     */
    public void setBase(int base) {
        this.base = base;
    }
    
    public int getBase() {
        return this.base;
    }
    
    @Override
    protected Converter getConverter() {
        return new IntegerNumberConverter(getBase());
    }
    
}
