package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@Mapping(path = "/manageAssociatedObjects", module = "manager")
@Forwards({ @Forward(name = "show", path = "/manager/listAssociatedObjects.jsp"),
        @Forward(name = "list", path = "/manager/listAssociatedObjects.jsp"),
        @Forward(name = "createDepartment", path = "/manager/createDepartment.jsp"),
        @Forward(name = "createAcademicOffice", path = "/manager/createAcademicOffice.jsp") })
public class ManageAssociatedObjects extends FenixDispatchAction {
    public static class AssociatedObjectsBean implements Serializable {
        private boolean active;
        private String code;
        private String name;
        private String realName;
        private String realNameEn;
        private AdministrativeOfficeType type;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getRealNameEn() {
            return realNameEn;
        }

        public void setRealNameEn(String realNameEn) {
            this.realNameEn = realNameEn;
        }

        public AdministrativeOfficeType getType() {
            return type;
        }

        public void setType(AdministrativeOfficeType type) {
            this.type = type;
        }
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Set<Department> departments = Bennu.getInstance().getDepartmentsSet();
        Set<AdministrativeOffice> offices = Bennu.getInstance().getAdministrativeOfficesSet();

        request.setAttribute("departments", departments);
        request.setAttribute("offices", offices);

        return mapping.findForward("list");
    }

    public ActionForward prepareCreateDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new AssociatedObjectsBean());
        return mapping.findForward("createDepartment");
    }

    public ActionForward createDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("department");
        createDepartment(bean);
        return list(mapping, form, request, response);
    }

    @Atomic
    private void createDepartment(AssociatedObjectsBean bean) {
        Department department = new Department();
        department.setActive(bean.isActive());
        department.setCode(bean.getCode());
        department.setName(bean.getName());
        department.setRealName(bean.getRealName());
        department.setName(bean.getName());
        department.setRealNameEn(bean.getRealNameEn());
        department.setRootDomainObject(Bennu.getInstance());
    }

    public ActionForward prepareAcademicOffice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new AssociatedObjectsBean());
        return mapping.findForward("createAcademicOffice");
    }

    public ActionForward createAcademicOffice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("office");
        createAcademicOffice(bean);
        return list(mapping, form, request, response);
    }

    @Atomic
    private void createAcademicOffice(AssociatedObjectsBean bean) {
        AdministrativeOffice office = new AdministrativeOffice();
        office.setAdministrativeOfficeType(bean.getType());
        new AdministrativeOfficeServiceAgreementTemplate(office);
        office.setRootDomainObject(Bennu.getInstance());
    }
}
