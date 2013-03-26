package net.sourceforge.fenixedu.domain.degreeStructure;

import java.io.Serializable;
import java.util.Arrays;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

public class EctsComparabilityTable implements Serializable {
    private static final long serialVersionUID = 4007115993594121554L;

    private final char[] table;

    EctsComparabilityTable(char[] table) {
        this.table = table;
    }

    EctsComparabilityTable(String[] table) {
        char[] converted = new char[table.length];
        for (int i = 0; i < table.length; i++) {
            if (table[i].trim().length() != 1 || table[i].trim().charAt(0) < 'A' || table[i].trim().charAt(0) > 'E') {
                throw new DomainException("error.ects.invalidTable", StringUtils.join(table, "<tab>"));
            }
            converted[i] = table[i].trim().charAt(0);
        }
        this.table = converted;
    }

    public String convert(int grade) {
        if (grade < 10 || grade > 20) {
            throw new DomainException("error.degreeStructure.converting.grade.not.in.approval.range");
        }
        return Character.toString(table[grade - 10]);
    }

    public String getPrintableFormat() {
        return toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EctsComparabilityTable) {
            EctsComparabilityTable table = (EctsComparabilityTable) object;
            return Arrays.equals(this.table, table.table);
        }
        return false;
    }

    @Override
    public String toString() {
        return new String(table);
    }

    public static EctsComparabilityTable fromString(String serialized) {
        return new EctsComparabilityTable(serialized.toCharArray());
    }

    public static EctsComparabilityTable fromStringArray(String[] table) {
        if (isEmpty(table)) {
            return null;
        }
        return new EctsComparabilityTable(table);
    }

    private static boolean isEmpty(String[] table) {
        for (String part : table) {
            if (!StringUtils.isEmpty(part)) {
                return false;
            }
        }
        return true;
    }
}
