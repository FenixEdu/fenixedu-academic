package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateNewInternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPersonMatchingAnyParameter;
import net.sourceforge.fenixedu.dataTransferObject.person.InternalPersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.CollectionPager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

@Mapping(path = "/personnelManagePeople", module = "personnelSection", scope = "request", parameter = "method")
@Forwards({
        @Forward(name = "searchPeople", path = "/personnelSection/people/searchPeople.jsp", tileProperties = @Tile(
                title = "private.staffarea.managepeople.searchpeople")),
        @Forward(name = "createPerson", path = "/personnelSection/people/createPerson.jsp", tileProperties = @Tile(
                title = "private.staffarea.managepeople.createperson")),
        @Forward(name = "createPersonFillInfo", path = "/personnelSection/people/createPersonFillInfo.jsp",
                tileProperties = @Tile(title = "private.staffarea.managepeople.createperson")),
        @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp", tileProperties = @Tile(
                title = "private.staffarea.managepeople")) })
public class ManagePeople extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("searchPeople");
    }

    public ActionForward prepareCreatePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final AnyPersonSearchBean anyPersonSearchBean = new AnyPersonSearchBean();
        request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
        return mapping.findForward("createPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("anyPersonSearchBeanId");
        AnyPersonSearchBean bean = (AnyPersonSearchBean) viewState.getMetaObject().getObject();

        CollectionPager<Person> result =
                SearchPersonMatchingAnyParameter.run(bean.getName(), null, null, bean.getDocumentIdNumber(),
                        bean.getIdDocumentType(), null, null, null, null, null, null, null);

        request.setAttribute("resultPersons", result.getCollection());
        request.setAttribute("anyPersonSearchBean", bean);
        return mapping.findForward("createPerson");
    }

    private void setRequestParametersToCreateInvitedPerson(final HttpServletRequest request, final InternalPersonBean personBean) {

        AnyPersonSearchBean anyPersonSearchBean = getRenderedObject("anyPersonSearchBeanId");
        if (anyPersonSearchBean != null) {
            personBean.setName(anyPersonSearchBean.getName());
            personBean.setIdDocumentType(anyPersonSearchBean.getIdDocumentType());
            personBean.setDocumentIdNumber(anyPersonSearchBean.getDocumentIdNumber());
        } else {
            final String name = request.getParameter("name");
            if (isSpecified(name)) {
                personBean.setName(name);
            }
            final String idDocumentType = request.getParameter("idDocumentType");
            if (isSpecified(idDocumentType)) {
                personBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
            }
            final String documentIdNumber = request.getParameter("documentIdNumber");
            if (isSpecified(documentIdNumber)) {
                personBean.setDocumentIdNumber(documentIdNumber);
            }
        }
        request.setAttribute("personBean", personBean);
    }

    private boolean isSpecified(final String string) {
        return !StringUtils.isEmpty(string);
    }

    public ActionForward prepareCreatePersonFillInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        setRequestParametersToCreateInvitedPerson(request, new InternalPersonBean());
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward createNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final InternalPersonBean bean = getRenderedObject();
        try {
            final Person person = CreateNewInternalPerson.run(bean);
            return viewPerson(person, mapping, request);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("invitedPersonBean", bean);
            return mapping.findForward("createPersonFillInfo");
        }
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InternalPersonBean bean = getRenderedObject();
        request.setAttribute("invitedPersonBean", bean);
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        return viewPerson(person, mapping, request);
    }

    public ActionForward viewPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request)
            throws Exception {
        request.setAttribute("person", person);
        return mapping.findForward("viewPerson");
    }

    public ActionForward attributeRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        final RoleType roleType = RoleType.valueOf(request.getParameter("roleType"));
        CreateNewInternalPerson.attributeRoles(person, Collections.singleton(roleType));
        return viewPerson(person, mapping, request);
    }

    public ActionForward prepareImportPersonsFromCSV(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("personsUploadBean", new PersonsUploadBean());
        return prepareCreatePerson(mapping, actionForm, request, response);
    }

    public ActionForward importPersonsFromCSV(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ArrayList<Person> importedPersons = new ArrayList<>();

        final PersonsUploadBean personsUploadBean = getRenderedObject();

        personsUploadBean.createPersons();

        importedPersons.addAll(personsUploadBean.getPersons());

        request.setAttribute("anyPersonSearchBean", new AnyPersonSearchBean());
        request.setAttribute("resultPersons", importedPersons);

        return mapping.findForward("createPerson");
    }

    public static class PersonsUploadBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient InputStream inputStream;
        private Workbook workbook;
        private String filename;
        private Set<Person> persons;

        public PersonsUploadBean() {
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(final InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(final String filename) {
            this.filename = filename;
        }

        public Sheet getSheet() {
            return getWorkbook().getSheetAt(0);
        }

        public Workbook getWorkbook() {
            if (workbook == null) {
                try {
                    workbook = WorkbookFactory.create(getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return workbook;
        }

        public static Integer getColumnNumber(Sheet sheet, String columnName) {
            Row firstRow = sheet.getRow(0);
            Iterator<Cell> iterator = firstRow.cellIterator();
            while (iterator.hasNext()) {
                Cell cell = iterator.next();
                if (StringUtils.equals(cell.getStringCellValue(), columnName)) {
                    return cell.getColumnIndex();
                }
            }
            return null;
        }

        public static Cell getCell(Row row, String columnName) {
            return row.getCell(getColumnNumber(row.getSheet(), columnName));
        }

        public static Date getCellDate(Row row, String columnName) {
            Cell cell = getCell(row, "data_nascimento");
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return cell.getDateCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                try {
                    return new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).parse(cell.getStringCellValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private static IDDocumentType getDocumentType(String type) {
            for (IDDocumentType documentType : IDDocumentType.values()) {
                if (StringUtils.equals(type, documentType.getName()) || StringUtils.equals(type, documentType.getLocalizedName())) {
                    return documentType;
                }
            }
            return null;
        }

        public PersonBean createPersonBean(Row row) {

            String name = getCell(row, "nome").getStringCellValue();
            Preconditions.checkNotNull(name, "nome is required and is empty for row " + row.getRowNum());

            int startFamilyName = name.lastIndexOf(" ");
            Preconditions.checkArgument(startFamilyName > 0, "full name is required");

            String givenNames = name.substring(0, startFamilyName);
            String familyNames = name.substring(startFamilyName);

            String identificationNumber = getCell(row, "docum_num").getStringCellValue();
            Preconditions.checkNotNull(identificationNumber, "docum_num is required and is empty for row " + row.getRowNum());

            Date dateOfBirth = getCellDate(row, "data_nascimento");
            Preconditions.checkNotNull(dateOfBirth, "data_nascimento is required and is empty for row " + row.getRowNum());

            IDDocumentType idDocumentType = getDocumentType(getCell(row, "docum").getStringCellValue());
            Preconditions.checkNotNull(idDocumentType, "docum is required and is empty for row " + row.getRowNum());

            PersonBean personBean =
                    new PersonBean(name, identificationNumber, idDocumentType, new YearMonthDay(dateOfBirth.getTime()));
            personBean.setGivenNames(givenNames);
            personBean.setFamilyNames(familyNames);

            //TODO - all the remaining fiels

            return personBean;
        }

        public Set<PersonBean> createPersonBeans(Sheet sheet) {
            Set<PersonBean> personsBeans = Sets.newHashSet();
            Iterator<Row> iterator = sheet.rowIterator();
            iterator.next();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (!isRowEmpty(row)) {
                    personsBeans.add(createPersonBean(row));
                }
            }
            return personsBeans;
        }

        public void createPersons() {
            Set<Person> persons = Sets.newHashSet();
            for (PersonBean personBean : createPersonBeans(getSheet())) {
                persons.add(new Person(personBean));
            }
            setPersons(persons);
        }

        private boolean isRowEmpty(Row row) {
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

}
