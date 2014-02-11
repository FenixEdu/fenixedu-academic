package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.collect.Sets;

public class PersonsBatchImporter {

    private Workbook workbook;
    private Set<Person> persons;

    public PersonsBatchImporter(InputStream inputStream) {
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void createPersons() throws Exception {
        Set<Person> persons = Sets.newHashSet();
        for (PersonBean personBean : createPersonBeans(workbook.getSheetAt(0))) {
            Person person = new Person(personBean);
            for (PartyContact contact : person.getPartyContactsSet()) {
                contact.setValid();
            }
            persons.add(person);
        }
        setPersons(persons);
    }

    public PersonBean createPersonBean(Row row) throws Exception {
        final PersonBean personBean = new PersonBean();
        for (PersonFieldImporter importer : PersonImportersContainer.getAllFieldImporters()) {
            importer.secureApply(row, personBean);
        }

        return personBean;
    }

    public Set<PersonBean> createPersonBeans(Sheet sheet) throws Exception {
        Set<PersonBean> personsBeans = Sets.newHashSet();
        Iterator<Row> iterator = sheet.rowIterator();
        iterator.next();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (!isEmptyRow(row)) {
                personsBeans.add(createPersonBean(row));
            }
        }
        return personsBeans;
    }

    private boolean isEmptyRow(Row row) {
        for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

}
