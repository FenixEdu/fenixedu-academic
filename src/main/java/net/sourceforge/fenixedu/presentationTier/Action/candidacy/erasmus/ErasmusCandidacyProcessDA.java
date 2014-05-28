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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.institutionalRelations.academic.Program;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCandidaciesApp;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "mobility", titleKey = "label.application.mobility",
        accessGroup = "(academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES))")
@Mapping(path = "/caseHandlingMobilityApplicationProcess", module = "academicAdministration",
        formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards({
        @Forward(name = "intro", path = "/candidacy/erasmus/mainCandidacyProcess.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
        @Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
        @Forward(name = "view-child-process-with-missing.required-documents",
                path = "/candidacy/erasmus/viewChildProcessesWithMissingRequiredDocuments.jsp") })
public class ErasmusCandidacyProcessDA extends CandidacyProcessDA {

    static public class ErasmusCandidacyProcessForm extends CandidacyProcessForm {
        private String selectedProcessId;

        private String[] selectedProcesses;

        public String getSelectedProcessId() {
            return selectedProcessId;
        }

        public void setSelectedProcessId(String selectedProcessId) {
            this.selectedProcessId = selectedProcessId;
        }

        public String[] getSelectedProcesses() {
            return selectedProcesses;
        }

        public void setSelectedProcesses(String[] selectedProcesses) {
            this.selectedProcesses = selectedProcesses;
        }
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyProcessBean", new MobilityApplicationProcessBean(ExecutionYear.readCurrentExecutionYear()));
        return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final CandidacyProcess process = getProcess(request);
        final MobilityApplicationProcess map = (MobilityApplicationProcess) process;
        final MobilityApplicationProcessBean bean = new MobilityApplicationProcessBean(process);
        bean.setForSemester(((MobilityApplicationProcess) process).getForSemester());
        request.setAttribute("candidacyProcessBean", bean);
        if (map.hasAnyChildProcesses()) {
            request.setAttribute("preLoadLevel", "Error");
        } else if (map.hasAnyCoordinators() || map.getCandidacyPeriod().getMobilityQuotasSet().size() > 0
                || map.getCandidacyPeriod().getEmailTemplatesSet().size() > 0) {
            request.setAttribute("preLoadLevel", "Warn");
        } else {
            request.setAttribute("preLoadLevel", "Ok");
        }

        return mapping.findForward("prepare-edit-candidacy-period");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setChooseDegreeBean(request);
        setChooseMobilityProgramBean(request);
        request.setAttribute("chooseDegreeBeanSchemaName", "ErasmusChooseDegreeBean.selectDegree");
        request.setAttribute("chooseMobilityProgramBeanSchemaName", "MobilityChooseProgramBean.selectMobilityProgram");
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward preLoadLastConfigurations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String processEid = request.getParameter("processEid");
        MobilityApplicationProcess process = FenixFramework.getDomainObject(processEid);
        preLoadLastProcessConfigurations(process);
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    @Atomic
    private void preLoadLastProcessConfigurations(MobilityApplicationProcess process) {
        process.resetConfigurations();
        process.preLoadLastConfigurations();
    }

    protected void setChooseMobilityProgramBean(HttpServletRequest request) {
        ChooseMobilityProgramBean chooseMobilityProgramBean =
                (ChooseMobilityProgramBean) getObjectFromViewState("choose.mobility.program.bean");
        if (chooseMobilityProgramBean == null) {
            chooseMobilityProgramBean = new ChooseMobilityProgramBean(getProcess(request));
            String mobilityProgramEid = request.getParameter("mobilityProgramEid");
            if (mobilityProgramEid != null && !mobilityProgramEid.isEmpty()) {
                MobilityProgram mobilityProgram = FenixFramework.getDomainObject(mobilityProgramEid);
                chooseMobilityProgramBean.setMobilityProgram(mobilityProgram);
            }
        }
        request.setAttribute("chooseMobilityProgramBean", chooseMobilityProgramBean);
    }

    protected ChooseMobilityProgramBean getChooseMobilityProgramBean(HttpServletRequest request) {
        return (ChooseMobilityProgramBean) request.getAttribute("chooseMobilityProgramBean");
    }

    protected void setChooseDegreeBean(HttpServletRequest request) {
        ChooseDegreeBean chooseDegreeBean = (ChooseDegreeBean) getObjectFromViewState("choose.degree.bean");
        if (chooseDegreeBean == null) {
            chooseDegreeBean = new ChooseDegreeBean(getProcess(request));
            String degreeEid = request.getParameter("degreeEid");
            if (degreeEid != null && !degreeEid.isEmpty()) {
                Degree degree = FenixFramework.getDomainObject(degreeEid);
                chooseDegreeBean.setDegree(degree);
            }
        }
        request.setAttribute("chooseDegreeBean", chooseDegreeBean);
    }

    protected ChooseDegreeBean getChooseDegreeBean(HttpServletRequest request) {
        return (ChooseDegreeBean) request.getAttribute("chooseDegreeBean");
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
            IndividualCandidacyProcess individualCandidacyProcess) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return MobilityApplicationPeriod.class;
    }

    @Override
    protected Class getChildProcessType() {
        return MobilityIndividualApplicationProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals = getExecutionIntervalsWithCandidacyPeriod();

            if (executionIntervals.size() == 1) {
                final ExecutionInterval executionInterval = executionIntervals.iterator().next();
                final List<MobilityApplicationProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    setCandidacyProcessInformation(request, candidacyProcesses.iterator().next());
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    return;
                }
            }

            request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
            request.setAttribute("executionIntervals", executionIntervals);

        } else {
            final ExecutionInterval executionInterval = getExecutionInterval(request);
            final MobilityApplicationProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

            if (candidacyProcess != null) {
                setCandidacyProcessInformation(request, candidacyProcess);
                setCandidacyProcessInformation(actionForm, getProcess(request));
            } else {
                final List<MobilityApplicationProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    setCandidacyProcessInformation(request, candidacyProcesses.iterator().next());
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    return;
                }

                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", getExecutionIntervalsWithCandidacyPeriod());
            }
            request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
        }
    }

    protected List<MobilityApplicationProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
        final List<MobilityApplicationProcess> result = new ArrayList<MobilityApplicationProcess>();
        for (final MobilityApplicationPeriod period : executionInterval.getMobilityApplicationPeriods()) {
            result.add(period.getMobilityApplicationProcess());
        }
        return result;
    }

    protected List<ExecutionInterval> getExecutionIntervalsWithCandidacyPeriod() {
        return ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
    }

    @Override
    protected MobilityApplicationProcess getCandidacyProcess(final HttpServletRequest request,
            final ExecutionInterval executionInterval) {

        final String selectedProcessId = getStringFromRequest(request, "selectedProcessId");
        if (selectedProcessId != null) {
            for (final MobilityApplicationPeriod applicationPeriod : executionInterval.getMobilityApplicationPeriods()) {
                if (applicationPeriod.getMobilityApplicationProcess().getExternalId().equals(selectedProcessId)) {
                    return applicationPeriod.getMobilityApplicationProcess();
                }
            }
        }
        return null;
    }

    @Override
    protected Class getProcessType() {
        return MobilityApplicationProcess.class;
    }

    protected void setCandidacyProcessInformation(final ActionForm actionForm, final MobilityApplicationProcess process) {
        final ErasmusCandidacyProcessForm form = (ErasmusCandidacyProcessForm) actionForm;
        form.setSelectedProcessId(process.getExternalId());
        form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getExternalId());
    }

    @Override
    protected MobilityApplicationProcess getProcess(HttpServletRequest request) {
        return (MobilityApplicationProcess) super.getProcess(request);
    }

    @Override
    protected Predicate<IndividualCandidacyProcess> getChildProcessSelectionPredicate(final CandidacyProcess process,
            HttpServletRequest request) {
        final Degree selectedDegree = getChooseDegreeBean(request).getDegree();
        final MobilityProgram mobilityProgram = getChooseMobilityProgramBean(request).getMobilityProgram();
        if (selectedDegree == null) {
            if (mobilityProgram == null) {
                return Predicates.alwaysTrue();
            } else {
                return new Predicate<IndividualCandidacyProcess>() {
                    @Override
                    public boolean apply(IndividualCandidacyProcess process) {
                        return ((MobilityIndividualApplicationProcess) process).getMobilityProgram().equals(mobilityProgram);
                    }
                };
            }
        } else {
            return new Predicate<IndividualCandidacyProcess>() {
                @Override
                public boolean apply(IndividualCandidacyProcess process) {

                    MobilityIndividualApplicationProcess mobilityProcess = (MobilityIndividualApplicationProcess) process;

                    if (mobilityProgram != null && !mobilityProcess.getMobilityProgram().equals(mobilityProgram)) {
                        return false;
                    }

                    return ((MobilityIndividualApplicationProcess) process).getCandidacy().getSelectedDegree() == selectedDegree;
                }
            };
        }
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
        return introForward(mapping);
    }

    public ActionForward prepareExecuteViewChildProcessWithMissingRequiredDocumentFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));

        return mapping.findForward("view-child-process-with-missing.required-documents");
    }

    public static class ErasmusCandidacyDegreesProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            final List<Degree> degrees =
                    new ArrayList<Degree>(Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
                            DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_DEGREE));

            degrees.remove(Degree.readBySigla("MSCIT"));

            java.util.Collections.sort(degrees, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

            return degrees;
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

    }

    public static class MobilityApplicationsMobilityProgramsProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object arg0, Object arg1) {
            final Set<MobilityProgram> mobilityPrograms =
                    new TreeSet<MobilityProgram>(MobilityProgram.COMPARATOR_BY_REGISTRATION_AGREEMENT);
            for (Program program : Bennu.getInstance().getProgramsSet()) {
                if (program instanceof MobilityProgram) {
                    mobilityPrograms.add((MobilityProgram) program);
                }
            }
            return mobilityPrograms;
        }

    }

}
