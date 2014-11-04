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
package org.fenixedu.academic.ui.struts.action.scientificCouncil;

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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificTeachersApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
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
        private static final long serialVersionUID = 1812211290868535463L;

        private String username;
        private TeacherCategory teacherCategory;
        private ExecutionSemester executionSemester;
        private Double lessonHours;
        private Department department;

        public TeacherAuthorizationManagementBean() {
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setTeacherCategory(TeacherCategory teacherCategory) {
            this.teacherCategory = teacherCategory;
        }

        public TeacherCategory getTeacherCategory() {
            return teacherCategory;
        }

        public void setExecutionSemester(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        @Atomic
        TeacherAuthorization create() throws FenixActionException {

            User user = User.findByUsername(getUsername());
            if (user == null) {
                throw new FenixActionException("label.invalid.username");
            }
            final Person person = user.getPerson();

            if (person.getTeacher() == null) {
                new Teacher(person);
            }
            return TeacherAuthorization.createOrUpdate(person.getTeacher(), getDepartment(), getExecutionSemester(),
                    getTeacherCategory(), false, getLessonHours());
        }

        public void setLessonHours(Double lessonHours) {
            this.lessonHours = lessonHours;
        }

        public Double getLessonHours() {
            return lessonHours;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department departments) {
            this.department = departments;
        }
    }

    public static class TeacherAuthorizationsUploadBean implements Serializable {
        private static final long serialVersionUID = -3469499844474498832L;

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
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, "label.message.invalid.line.format",
                                Integer.toString(lineCount)));
                        continue;
                    }

                    final String username = parts[0].trim();
                    final TeacherCategory teacherCategory = TeacherCategory.find(parts[1].trim());
                    final String i = parts[2].trim();
                    final Double lessonHours =
                            StringUtils.isNumeric(i.replace(".", " ").replace(',', ' ').replace(" ", "")) ? Double.valueOf(i
                                    .replace(',', '.')) : null;
                    final Department department = Department.find(parts[3].trim());

                    if (username == null || username.isEmpty() || User.findByUsername(username) == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, "label.message.username.invalid",
                                Integer.toString(lineCount), parts[0].trim()));
                        continue;
                    }

                    if (teacherCategory == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, "label.message.teacherCategory.invalid",
                                Integer.toString(lineCount), parts[1].trim()));
                        continue;
                    }

                    if (department == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, "label.message.department.invalid",
                                Integer.toString(lineCount), parts[3].trim()));
                        continue;
                    }

                    if (lessonHours == null) {
                        messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, "label.message.lessonHours.invalid",
                                Integer.toString(lineCount), parts[2].trim()));
                        continue;
                    }

                    final TeacherAuthorizationManagementBean bean = new TeacherAuthorizationManagementBean();
                    bean.setUsername(username);
                    bean.setTeacherCategory(teacherCategory);
                    bean.setLessonHours(lessonHours);
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
                    messages.add(BundleUtil.getString(Bundle.SCIENTIFIC, ex.getMessage(), bean.getUsername()));
                }
            }
            return messages;
        }
    }

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("auths", Bennu.getInstance().getTeacherAuthorizationSet());
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
            addActionMessage(request, e.getMessage(), tamb.getUsername());
            return mapping.findForward("createTeacherAuthorization");
        }

        return list(mapping, actionForm, request, response);
    }

    public ActionForward revoke(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherAuthorization auth = FenixFramework.getDomainObject(request.getParameter("oid"));
        auth.revoke();
        return list(mapping, actionForm, request, response);
    }
}
