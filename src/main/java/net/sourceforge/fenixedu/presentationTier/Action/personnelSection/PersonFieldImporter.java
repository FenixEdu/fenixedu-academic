package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public abstract class PersonFieldImporter {
    private final String columnName;
    private final PersonBean personBean;
    private final boolean isMandatory;

    public PersonFieldImporter(String columnName, PersonBean personBean, boolean isMandatory) {
        this.columnName = columnName;
        this.personBean = personBean;
        this.isMandatory = isMandatory;
    }

    public abstract void apply(Row row);

    public boolean canApply(Row row) {
        return readCell(row) != null;
    }

    public Cell readCell(Row row) {
        return getCell(row, columnName);
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public PersonBean getPersonBean() {
        return personBean;
    }

}