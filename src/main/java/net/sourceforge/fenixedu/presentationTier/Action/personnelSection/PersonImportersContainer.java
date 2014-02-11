package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.YearMonthDay;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class PersonImportersContainer {

    private final static PersonFieldImporter FULL_NAME = new PersonFieldImporter("Name", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            String name = getCell(row).getStringCellValue();
            Preconditions.checkArgument(!StringUtils.isEmpty(name));
            personBean.setName(name);
        }
    };

    private final static PersonFieldImporter GIVEN_NAMES = new PersonFieldImporter("Name", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            String name = getCell(row).getStringCellValue();
            String givenNames = name.substring(0, name.lastIndexOf(" "));
            Preconditions.checkArgument(!StringUtils.isEmpty(givenNames));
            personBean.setGivenNames(givenNames);
        }
    };

    private final static PersonFieldImporter FAMILY_NAMES = new PersonFieldImporter("Name", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            String name = getCell(row).getStringCellValue();
            String familyNames = name.substring(name.lastIndexOf(" ") + 1);
            Preconditions.checkArgument(!StringUtils.isEmpty(familyNames));
            personBean.setFamilyNames(familyNames);
        }
    };

    private final static PersonFieldImporter DATE_OF_BIRTH = new PersonFieldImporter("DateOfBirth", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            YearMonthDay dateOfBirth = new YearMonthDay(getCellDate(row, columnName));
            Preconditions.checkNotNull(dateOfBirth);
            personBean.setDateOfBirth(dateOfBirth);
        }
    };

    private final static PersonFieldImporter DOCUMENT_TYPE = new PersonFieldImporter("DocumentType", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            IDDocumentType idDocumentType = IDDocumentType.parse(getCell(row).getStringCellValue());
            Preconditions.checkNotNull(idDocumentType);
            personBean.setIdDocumentType(idDocumentType);
        }
    };

    private final static PersonFieldImporter DOCUMENT_ID = new PersonFieldImporter("DocumentNumber", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            String documentIdNumber = getCell(row).getStringCellValue();
            Preconditions.checkArgument(!StringUtils.isEmpty(documentIdNumber));
            personBean.setDocumentIdNumber(documentIdNumber);
        }
    };

    private final static PersonFieldImporter DOCUMENT_EMISSION_LOCATION = new PersonFieldImporter("DocumentEmissionLocation",
            false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setDocumentIdEmissionLocation(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter DOCUMENT_EMISSION_DATE = new PersonFieldImporter("DocumentEmissionDate", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setDocumentIdEmissionDate(new YearMonthDay(getCellDate(row, columnName)));
        }
    };

    private final static PersonFieldImporter DOCUMENT_EXPIRATION_DATE = new PersonFieldImporter("DocumentExpirationDate", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setDocumentIdExpirationDate(new YearMonthDay(getCellDate(row, columnName)));
        }
    };

    private final static PersonFieldImporter FATHER_NAME = new PersonFieldImporter("FatherName", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setFatherName(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter MOTHER_NAME = new PersonFieldImporter("MotherName", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setMotherName(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter GENDER = new PersonFieldImporter("Gender", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            Gender gender = Gender.parse(getCell(row).getStringCellValue());
            Preconditions.checkNotNull(gender);
            personBean.setGender(gender);
        }
    };

    private final static PersonFieldImporter MARITIAL_STATUS = new PersonFieldImporter("MaritialStatus", true) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            MaritalStatus maritialStatus = MaritalStatus.parse(getCell(row).getStringCellValue());
            Preconditions.checkNotNull(maritialStatus);
            personBean.setMaritalStatus(maritialStatus);
        }
    };

    private final static PersonFieldImporter NATIONALITY = new PersonFieldImporter("Nationality", false) {
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

    private final static PersonFieldImporter PHONE = new PersonFieldImporter("Phone", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setPhone(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter MOBILE_PHONE = new PersonFieldImporter("MobilePhone", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setMobile(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter EMAIL = new PersonFieldImporter("Email", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setEmail(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter ADDRESS = new PersonFieldImporter("Address", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setAddress(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter ZIPCODE = new PersonFieldImporter("ZipCode", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setAreaCode(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter ZIPCODE_LOCATION = new PersonFieldImporter("ZipCodeLocation", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setAreaOfAreaCode(getCell(row).getStringCellValue());
        }
    };

    private final static PersonFieldImporter LOCATION = new PersonFieldImporter("Location", false) {
        @Override
        public void apply(Row row, PersonBean personBean) {
            personBean.setArea(getCell(row).getStringCellValue());
        }
    };

    public static List<PersonFieldImporter> getAllFieldImporters() {
        return Lists.newArrayList(FULL_NAME, GIVEN_NAMES, FAMILY_NAMES, DOCUMENT_ID, DATE_OF_BIRTH, DOCUMENT_TYPE, GENDER,
                MARITIAL_STATUS, DOCUMENT_EMISSION_LOCATION, DOCUMENT_EMISSION_DATE, DOCUMENT_EXPIRATION_DATE, FATHER_NAME,
                MOTHER_NAME, NATIONALITY, PHONE, MOBILE_PHONE, EMAIL, ADDRESS, ZIPCODE, ZIPCODE_LOCATION, LOCATION);
    }

    public static List<PersonFieldImporter> getMandatoryFields() {
        return Lists.newArrayList(Iterables.filter(getAllFieldImporters(), new Predicate<PersonFieldImporter>() {
            @Override
            public boolean apply(PersonFieldImporter input) {
                return input.isMandatory();
            }
        }));
    }

    public static List<String> getMandatoryColumns() {
        Set<String> mandatoryColumns = new HashSet<>();
        for (PersonFieldImporter importer : getMandatoryFields()) {
            mandatoryColumns.add(importer.getColumnName());
        }
        return new ArrayList<String>(mandatoryColumns);
    }
}
