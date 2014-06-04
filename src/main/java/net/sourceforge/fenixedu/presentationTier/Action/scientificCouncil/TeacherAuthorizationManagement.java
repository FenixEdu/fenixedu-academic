/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificTeachersApp;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.FileUtils;

@StrutsFunctionality(app = ScientificTeachersApp.class, path = "authorizations", titleKey = "label.authorizations")
@Mapping(path = "/teacherAuthorization", module = "scientificCouncil")
@Forwards({ @Forward(name = "createTeacherAuthorization", path = "/scientificCouncil/createTeacherAuthorization.jsp"),
        @Forward(name = "listTeacherAuthorization", path = "/scientificCouncil/listTeacherAuthorization.jsp"),
        @Forward(name = "teacherAuthorizationsUpload", path = "/scientificCouncil/teacherAuthorizationsUpload.jsp") })
public class TeacherAuthorizationManagement extends FenixDispatchAction {

    public static class TeacherAuthorizationManagementBean implements Serializable {
        private String istUsername;
        private ProfessionalCategory professionalCategory;
        private ExecutionSemester executionSemester;
        private Double lessonHours;
        private Boolean canPark;
        private Boolean canHaveCard;
        private Department department;

        public TeacherAuthorizationManagementBean() {
        }

        public void setIstUsername(String istUsername) {
            this.istUsername = istUsername;
        }

        public String getIstUsername() {
            return istUsername;
        }

        public void setProfessionalCategory(ProfessionalCategory professionalCategory) {
            this.professionalCategory = professionalCategory;
        }

        public ProfessionalCategory getProfessionalCategory() {
            return professionalCategory;
        }

        public void setExecutionSemester(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        @Atomic
        ExternalTeacherAuthorization create() throws FenixActionException {

            User user = User.findByUsername(getIstUsername());
            if (user == null) {
                throw new FenixActionException("label.invalid.istUsername");
            }
            final Person person = user.getPerson();

            if (person.hasTeacher()) {
                for (final TeacherAuthorization teacherAuthorization : person.getTeacher().getAuthorizationSet()) {
                    if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
                        final ExternalTeacherAuthorization auth = (ExternalTeacherAuthorization) teacherAuthorization;
                        if (auth.getActive().booleanValue() && auth.getExecutionSemester() == getExecutionSemester()) {
                            throw new FenixActionException("already.created.teacher.authorization");
                        }
                    }
                }
            }

            ExternalTeacherAuthorization eta = new ExternalTeacherAuthorization();
            eta.setAuthorizer(AccessControl.getPerson());
            eta.setExecutionSemester(getExecutionSemester());
            eta.setProfessionalCategory(getProfessionalCategory());
            eta.setRootDomainObject(Bennu.getInstance());
            eta.setActive(true);
            eta.setCanPark(getCanPark());
            eta.setCanHaveCard(false);
            eta.setLessonHours(Double.valueOf(getLessonHours()));
            eta.setDepartment(getDepartment());
            if (person.getTeacher() != null) {
                eta.setTeacher(person.getTeacher());
            } else {
                Teacher teacher = new Teacher(person);
                eta.setTeacher(teacher);
            }
            return eta;
        }

        public void setLessonHours(Double lessonHours) {
            this.lessonHours = lessonHours;
        }

        public Double getLessonHours() {
            return lessonHours;
        }

        public void setCanPark(Boolean canPark) {
            this.canPark = canPark;
        }

        public Boolean getCanPark() {
            return canPark;
        }

        public void setCanHaveCard(Boolean canHaveCard) {
            this.canHaveCard = canHaveCard;
        }

        public Boolean getCanHaveCard() {
            return canHaveCard;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department departments) {
            this.department = departments;
        }
    }

    public static class TeacherAuthorizationsUploadBean implements Serializable {

        private ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();

        private transient InputStream inputStream;

        private transient String fileContent;

        private String filename;

        public TeacherAuthorizationsUploadBean() {
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        public void setExecutionSemester(final ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
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

        public String getFileContent() {
            if (fileContent == null) {
                try {
                    fileContent = FileUtils.readFile(new InputStreamReader(inputStream, Charset.defaultCharset().name()));
                } catch (final UnsupportedEncodingException e) {
                    throw new Error(e);
                } catch (final IOException e) {
                    throw new Error(e);
                }
            }
            return fileContent;
        }

        private Set<TeacherAuthorizationManagementBean> getTeacherAuthorizationManagementBeans(final List<String> messages) {
            final Set<TeacherAuthorizationManagementBean> result =
                    new HashSet<TeacherAuthorizationManagement.TeacherAuthorizationManagementBean>();
            int lineCount = 0;
            for (final String line : getFileContent().split("\n")) {
                lineCount++;
                final String tline = line.trim();
                if (!tline.isEmpty()) {
                    final String splitChar = tline.indexOf(';') > 0 ? ";" : "\t";
                    final String[] parts = tline.split(splitChar);

                    if (parts.length != 6) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC,
                                "label.message.invalid.line.format", Integer.toString(lineCount)));
                        continue;
                    }

                    final String istUsername = parts[0].trim();
                    final ProfessionalCategory professionalCategory =
                            ProfessionalCategory.find(parts[1].trim(), CategoryType.TEACHER);
                    final String i = parts[2].trim();
                    final Double lessonHours =
                            StringUtils.isNumeric(i.replace(".", " ").replace(',', ' ').replace(" ", "")) ? Double.valueOf(i
                                    .replace(',', '.')) : null;
                    final Boolean canPark = Boolean.valueOf("S".equalsIgnoreCase(parts[3].trim()));
                    final Boolean canHaveCard = Boolean.valueOf("S".equalsIgnoreCase(parts[4].trim()));
                    final Department department = Department.find(parts[5].trim());

                    if (istUsername == null || istUsername.isEmpty() || User.findByUsername(istUsername) == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC,
                                "label.message.istUsername.invalid", Integer.toString(lineCount), parts[0].trim()));
                        continue;
                    }

                    if (professionalCategory == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC,
                                "label.message.professionalCategory.invalid", Integer.toString(lineCount), parts[1].trim()));
                        continue;
                    }

                    if (department == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC,
                                "label.message.department.invalid", Integer.toString(lineCount), parts[5].trim()));
                        continue;
                    }

                    if (lessonHours == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC,
                                "label.message.lessonHours.invalid", Integer.toString(lineCount), parts[2].trim()));
                        continue;
                    }

                    final TeacherAuthorizationManagementBean bean = new TeacherAuthorizationManagementBean();
                    bean.setIstUsername(istUsername);
                    bean.setProfessionalCategory(professionalCategory);
                    bean.setLessonHours(lessonHours);
                    bean.setCanPark(canPark);
                    bean.setCanHaveCard(canHaveCard);
                    bean.setDepartment(department);
                    bean.setExecutionSemester(executionSemester);
                    result.add(bean);
                }
            }
            return result;
        }

        @Atomic
        public List<String> create() {
            final List<String> messages = new ArrayList<String>();
            for (final TeacherAuthorizationManagementBean bean : getTeacherAuthorizationManagementBeans(messages)) {
                try {
                    bean.create();
                } catch (final FenixActionException ex) {
                    messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, ex.getMessage(),
                            bean.getIstUsername()));
                }
            }
            return messages;
        }

    }

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ArrayList<ExternalTeacherAuthorization> teacher = new ArrayList<ExternalTeacherAuthorization>();

        for (TeacherAuthorization teacherAuthorization : Bennu.getInstance().getTeacherAuthorizationSet()) {
            if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
                teacher.add((ExternalTeacherAuthorization) teacherAuthorization);
            }
        }

        request.setAttribute("auths", teacher);
        return mapping.findForward("listTeacherAuthorization");
    }

    public ActionForward pre(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new TeacherAuthorizationManagementBean());
        return mapping.findForward("createTeacherAuthorization");
    }

    public ActionForward prepareUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("teacherAuthorizationsUploadBean", new TeacherAuthorizationsUploadBean());
        return mapping.findForward("teacherAuthorizationsUpload");
    }

    public ActionForward upload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final TeacherAuthorizationsUploadBean teacherAuthorizationsUploadBea = getRenderedObject();
        final List<String> uploadMessages = teacherAuthorizationsUploadBea.create();
        request.setAttribute("uploadMessages", uploadMessages);
        return list(mapping, actionForm, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherAuthorizationManagementBean tamb = ((TeacherAuthorizationManagementBean) getRenderedObject("bean"));

        try {
            tamb.create();
        } catch (FenixActionException e) {
            RenderUtils.invalidateViewState();
            request.setAttribute("bean", tamb);
            addActionMessage(request, e.getMessage(), tamb.getIstUsername());
            return mapping.findForward("createTeacherAuthorization");
        }

        return list(mapping, actionForm, request, response);
    }

    public ActionForward revoke(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExternalTeacherAuthorization auth = FenixFramework.getDomainObject(request.getParameter("oid"));
        auth.revoke();
        return list(mapping, actionForm, request, response);
    }
}
