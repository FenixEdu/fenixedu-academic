package Util.tests;

public class RenderFIB extends Render {

    public static final int STRING_CODE = 1;

    public static final int INTEGER_CODE = 2;

    public static final int DECIMAL_CODE = 3;

    // public static final int SCIENTIFIC_CODE = 4;

    // public static final int BOOLEAN_CODE = 5;

    public static final String STRING = "String";

    public static final String INTEGER = "Integer";

    public static final String DECIMAL = "Decimal";

    private Integer rows;

    private Integer columns;

    private Integer maxchars;

    private Integer fibtype;

    public RenderFIB() {
        super();
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getMaxchars() {
        return maxchars;
    }

    public void setMaxchars(Integer maxchars) {
        this.maxchars = maxchars;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getFibtype() {
        return fibtype;
    }

    public void setFibtype(String fibtype) {
        this.fibtype = getFibTypeCode(fibtype);
    }

    private Integer getFibTypeCode(String fibType) {
        if (fibType.equals(INTEGER))
            return new Integer(INTEGER_CODE);
        else if (fibType.equals(DECIMAL))
            return new Integer(DECIMAL_CODE);
        else
            return new Integer(STRING_CODE);
    }

    private String getFibTypeString() {
        if (fibtype.intValue() == INTEGER_CODE)
            return INTEGER;
        else if (fibtype.intValue() == DECIMAL_CODE)
            return DECIMAL;
        else
            return STRING;
    }

    public String toXML(String inside) {
        String result = new String("<render_fib");
        if (fibtype != null)
                result = result.concat(" fibtype=\"" + getFibTypeString()
                        + "\"");
        if (rows != null) result = result.concat(" rows=\"" + rows + "\"");
        if (columns != null)
                result = result.concat(" columns=\"" + columns + "\"");
        if (maxchars != null)
                result = result.concat(" maxchars=\"" + maxchars + "\"");
        return result.concat(">"+inside + "</render_fib>\n");
    }
}