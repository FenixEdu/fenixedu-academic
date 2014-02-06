package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class PersonsBatchImporter {

    private Workbook workbook;
    private Set<Person> persons;
    final static Locale PT = new Locale("pt");

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
            persons.add(new Person(personBean));
        }
        setPersons(persons);
    }

    private final static PersonFieldImporter FULL_NAME = new PersonFieldImporter("nome", true) {

        @Override
        public void apply(Row row, PersonBean personBean) {
            String name = getCell(row).getStringCellValue();
            Preconditions.checkArgument(!StringUtils.isEmpty(name));
            personBean.setName(name);
        }
    };

    private final static PersonFieldImporter GIVEN_NAMES = new PersonFieldImporter("nome", true) {

        @Override
        public void apply(Row row, PersonBean personBean) {
            String name = getCell(row).getStringCellValue();
            String givenNames = name.substring(0, name.lastIndexOf(" "));
            Preconditions.checkArgument(!StringUtils.isEmpty(givenNames));
            personBean.setGivenNames(givenNames);
        }
    };

    private final static PersonFieldImporter FAMILY_NAMES = new PersonFieldImporter("nome", true) {

        @Override
        public void apply(Row row, PersonBean personBean) {
            String name = getCell(row).getStringCellValue();
            String familyNames = name.substring(name.lastIndexOf(" ") + 1);
            Preconditions.checkArgument(!StringUtils.isEmpty(familyNames));
            personBean.setFamilyNames(familyNames);
        }
    };

    private final static PersonFieldImporter DOCUMENT_ID = new PersonFieldImporter("docum_num", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            String documentIdNumber = getCell(row).getStringCellValue();
            Preconditions.checkArgument(!StringUtils.isEmpty(documentIdNumber));
            personBean.setDocumentIdNumber(documentIdNumber);
        }
    };

    private final static PersonFieldImporter DATE_OF_BIRTH = new PersonFieldImporter("data_nascimento", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            YearMonthDay dateOfBirth = new YearMonthDay(getCellDate(row, columnName));
            Preconditions.checkNotNull(dateOfBirth);
            personBean.setDateOfBirth(dateOfBirth);
        }
    };

    private final static PersonFieldImporter DOCUMENT_TYPE = new PersonFieldImporter("docum", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            IDDocumentType idDocumentType = IDDocumentType.parse(getCell(row).getStringCellValue(), PT);
            Preconditions.checkNotNull(idDocumentType);
            personBean.setIdDocumentType(idDocumentType);
        }
    };

    private final static PersonFieldImporter GENDER = new PersonFieldImporter("sexo", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            Gender gender = parseGender(getCell(row).getStringCellValue());
            Preconditions.checkNotNull(gender);
            personBean.setGender(gender);
        }
    };

    private final static PersonFieldImporter MARITIAL_STATUS = new PersonFieldImporter("estado_civil", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            MaritalStatus maritialStatus = MaritalStatus.parse(getCell(row).getStringCellValue(), PT);
            Preconditions.checkNotNull(maritialStatus);
            personBean.setMaritalStatus(maritialStatus);
        }
    };

    private final static PersonFieldImporter DOCUMENT_EMISSION_LOCATION = new PersonFieldImporter("docum_local_emissao", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setDocumentIdEmissionLocation(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter DOCUMENT_EMISSION_DATE = new PersonFieldImporter("docum_data_emissao", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setDocumentIdEmissionDate(new YearMonthDay(getCellDate(row, columnName)));
        }
    };

    private final static PersonFieldImporter DOCUMENT_EXPIRATION_DATE = new PersonFieldImporter("docum_data_valido", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setDocumentIdExpirationDate(new YearMonthDay(getCellDate(row, columnName)));
        }
    };

    private final static PersonFieldImporter FATHER_NAME = new PersonFieldImporter("docum_nome_pai", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setFatherName(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter MOTHER_NAME = new PersonFieldImporter("docum_nome_mae", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setMotherName(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter NATIONALITY = new PersonFieldImporter("nacionalidade", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            String cellValue = getCell(row).getStringCellValue();
            Country country = null;
            if (StringUtils.length(cellValue) == 3) {
                country = Country.readByThreeLetterCode(getCell(row).getStringCellValue());
            } else if (StringUtils.length(cellValue) == 2) {
                country = Country.readByThreeLetterCode(getCell(row).getStringCellValue());
            }
            personBean.setNationality(country);
        }
    };

    private final static PersonFieldImporter PHONE = new PersonFieldImporter("telefone_1", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setPhone(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter MOBILE_PHONE = new PersonFieldImporter("telemovel1", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setMobile(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter EMAIL = new PersonFieldImporter("e_mail1", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setEmail(getCell(row).getStringCellValue());
        }
    };

    public PersonBean createPersonBean(Row row) throws Exception {
        final PersonBean personBean = new PersonBean();
        for (PersonFieldImporter importer : getAllFieldImporters()) {
            importer.secureApply(row, personBean);
        }

        return personBean;
    }

    private PersonFieldImporter[] getAllFieldImporters() {
        return new PersonFieldImporter[] { FULL_NAME, GIVEN_NAMES, FAMILY_NAMES, DOCUMENT_ID, DATE_OF_BIRTH, DOCUMENT_TYPE,
                GENDER, MARITIAL_STATUS, DOCUMENT_EMISSION_LOCATION, DOCUMENT_EMISSION_DATE, DOCUMENT_EXPIRATION_DATE,
                FATHER_NAME, MOTHER_NAME, NATIONALITY, PHONE, MOBILE_PHONE, EMAIL };
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
