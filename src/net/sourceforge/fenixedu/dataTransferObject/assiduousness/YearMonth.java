package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import net.sourceforge.fenixedu.util.Month;

public class YearMonth implements Serializable {
    Integer year;

    Month month;

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
