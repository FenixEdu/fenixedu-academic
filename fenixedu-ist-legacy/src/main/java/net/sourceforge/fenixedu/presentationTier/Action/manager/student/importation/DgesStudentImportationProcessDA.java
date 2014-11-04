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
package org.fenixedu.academic.ui.struts.action.manager.student.importation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.student.importation.DgesBaseProcessLauncher;
import org.fenixedu.academic.domain.student.importation.DgesStudentImportationFile;
import org.fenixedu.academic.domain.student.importation.DgesStudentImportationProcess;
import org.fenixedu.academic.domain.student.importation.ExportDegreeCandidaciesByDegreeForPasswordGeneration;
import org.fenixedu.academic.domain.student.importation.ExportExistingStudentsFromImportationProcess;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "dges-student-importation",
        titleKey = "title.dges.student.importation")
@Mapping(path = "/dgesStudentImportationProcess", module = "manager")
@Forwards({
        @Forward(name = "list", path = "/manager/student/importation/list.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/manager/student/importation/prepareCreateNewProcess.jsp"),
        @Forward(name = "prepare-create-new-exportation-candidacies-for-password-generation-job",
                path = "/manager/student/importation/prepareCreateNewExportationForPasswordGeneration.jsp") })
public class DgesStudentImportationProcessDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        DgesBaseProcessBean bean = getRenderedBean();
        if (bean == null) {
            bean = new DgesBaseProcessBean(ExecutionYear.readCurrentExecutionYear());
        }

        RenderUtils.invalidateViewState("importation.bean");
        request.setAttribute("importationBean", bean);

        request.setAttribute("importationJobsDone", DgesStudentImportationProcess.readDoneJobs(bean.getExecutionYear()));
        request.setAttribute("importationJobsPending", DgesStudentImportationProcess.readUndoneJobs(bean.getExecutionYear()));
        request.setAttribute("exportationPasswordsDone",
                ExportDegreeCandidaciesByDegreeForPasswordGeneration.readDoneJobs(bean.getExecutionYear()));
        request.setAttribute("exportationPasswordsPending",
                ExportDegreeCandidaciesByDegreeForPasswordGeneration.readUndoneJobs(bean.getExecutionYear()));
        request.setAttribute("exportationAlreadyStudentsDone",
                ExportExistingStudentsFromImportationProcess.readDoneJobs(bean.getExecutionYear()));
        request.setAttribute("exportionAlreadyStudentsPending",
                ExportExistingStudentsFromImportationProcess.readUndoneJobs(bean.getExecutionYear()));

        request.setAttribute("canRequestJobImportationProcess", DgesStudentImportationProcess.canRequestJob());
        request.setAttribute("canRequestJobExportationPasswords",
                ExportDegreeCandidaciesByDegreeForPasswordGeneration.canRequestJob());
        request.setAttribute("canRequestJobExportationAlreadyStudents",
                ExportExistingStudentsFromImportationProcess.canRequestJob());

        return mapping.findForward("list");
    }

    private DgesBaseProcessBean getRenderedBean() {
        return getRenderedObject("importation.bean");
    }

    public ActionForward prepareCreateNewImportationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DgesBaseProcessBean bean = getRenderedBean();
        if (bean == null) {
            bean = new DgesBaseProcessBean(ExecutionYear.readCurrentExecutionYear());
        }

        RenderUtils.invalidateViewState("importation.bean");
        RenderUtils.invalidateViewState("importation.bean.edit");

        request.setAttribute("importationBean", bean);

        return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward createNewImportationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DgesBaseProcessBean bean = getRenderedBean();
        RenderUtils.invalidateViewState("importation.bean");
        RenderUtils.invalidateViewState("importation.bean.edit");

        byte[] contents = bean.consumeStream();

        DgesStudentImportationFile file =
                DgesStudentImportationFile.create(contents, bean.getFilename(), bean.getExecutionYear(), bean.getCampus(),
                        bean.getPhase());
        DgesBaseProcessLauncher.launchImportation(bean.getExecutionYear(), bean.getCampus(), bean.getPhase(), file);

        return list(mapping, form, request, response);
    }

    public ActionForward createNewImportationProcessInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        addActionMessage(request, "error", "error.dges.importation.file");
        return prepareCreateNewImportationProcess(mapping, form, request, response);
    }

    public ActionForward cancelJob(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        QueueJob job = getDomainObject(request, "queueJobId");
        job.cancel();

        return list(mapping, form, request, response);
    }

    public ActionForward prepareCreateNewExportationCandidaciesForPasswordGenerationJob(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        DgesBaseProcessBean bean = getRenderedBean();
        if (bean == null) {
            bean = new DgesBaseProcessBean(ExecutionYear.readCurrentExecutionYear());
        }

        RenderUtils.invalidateViewState("importation.bean");
        RenderUtils.invalidateViewState("importation.bean.edit");

        request.setAttribute("importationBean", bean);

        return mapping.findForward("prepare-create-new-exportation-candidacies-for-password-generation-job");
    }

    public ActionForward createNewExportationCandidaciesForPasswordGenerationProcess(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        DgesBaseProcessBean bean = getRenderedBean();
        RenderUtils.invalidateViewState("importation.bean");
        RenderUtils.invalidateViewState("importation.bean.edit");

        DgesBaseProcessLauncher.launchExportationCandidaciesForPasswordGeneration(bean.getExecutionYear(), bean.getPhase());

        return list(mapping, form, request, response);
    }

    public ActionForward createNewExportationCandidaciesForPasswordGenerationProcessInvalid(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return prepareCreateNewExportationCandidaciesForPasswordGenerationJob(mapping, form, request, response);
    }

    public static class DgesBaseProcessBean implements java.io.Serializable {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private InputStream stream;
        private String filename;
        private Long filesize;

        private ExecutionYear executionYear;
        private Space campus;
        private EntryPhase phase;

        public DgesBaseProcessBean(final ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        public InputStream getStream() {
            return stream;
        }

        public void setStream(InputStream stream) {
            this.stream = stream;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public Long getFilesize() {
            return filesize;
        }

        public void setFilesize(Long filesize) {
            this.filesize = filesize;
        }

        public ExecutionYear getExecutionYear() {
            return executionYear;
        }

        public void setExecutionYear(ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        public Space getCampus() {
            return campus;
        }

        public void setCampus(Space campus) {
            this.campus = campus;
        }

        public EntryPhase getPhase() {
            return phase;
        }

        public void setPhase(final EntryPhase phase) {
            this.phase = phase;
        }

        public byte[] consumeStream() throws IOException {
            byte[] data = new byte[getFilesize().intValue()];

            getStream().read(data);

            return data;
        }
    }

    public static class EntryPhaseProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            return Arrays.asList(EntryPhase.values());
        }

        @Override
        public Converter getConverter() {
            return null;
        }
    }
}
