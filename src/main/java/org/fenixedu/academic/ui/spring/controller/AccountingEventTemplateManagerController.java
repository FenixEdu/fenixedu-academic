package org.fenixedu.academic.ui.spring.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.EventTemplate;
import org.fenixedu.academic.domain.accounting.EventTemplateConfig;
import org.fenixedu.academic.dto.accounting.EventTemplateBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "Event Templates")
@RequestMapping(AccountingEventTemplateManagerController.REQUEST_MAPPING)
public class AccountingEventTemplateManagerController {

        private static final Locale PT = new Locale("pt", "PT");
        private static final Locale EN = new Locale("en", "GB");

        private static final Logger logger = LoggerFactory.getLogger(AccountingEventTemplateManagerController.class);
        public static final String REQUEST_MAPPING = "/event-template-management";

        @RequestMapping(value = "", method = RequestMethod.GET)
        public String list(boolean showAll, Model model) {
                List<EventTemplate> templates = new ArrayList<EventTemplate>(
                                showAll ? Bennu.getInstance().getEventTemplateSet()
                                                : EventTemplate.getTopLevelTemplates());
                templates.sort(Comparator.comparing((t) -> t.getCode()));
                model.addAttribute("templates", templates);
                return view("event-template-list");
        }

        @RequestMapping(value = "create", method = RequestMethod.GET)
        public String prepareCreate(Model model) {
                return view("event-template-create");
        }

        @RequestMapping(value = "create", method = RequestMethod.POST)
        public String create(EventTemplateBean eventTemplateBean, Model model) {
                EventTemplate template = createEventTemplate(eventTemplateBean.getCode(), eventTemplateBean.getTitle(),
                                eventTemplateBean.getDescription());
                return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config";
        }

        @RequestMapping(value = "{template}", method = RequestMethod.GET)
        public String detail(@PathVariable EventTemplate template,
                        @RequestParam(required = false) EventTemplateConfig config, Model model) {
                model.addAttribute("template", template);

                List<EventTemplate> children = new ArrayList<EventTemplate>(template.getAlternativeEventTemplateSet());
                children.sort(Comparator.comparing((t) -> t.getCode()));
                model.addAttribute("children", children);

                List<EventTemplate> parents = new ArrayList<EventTemplate>(
                                template.getEventTemplateFromAlternativeSet());
                parents.sort(Comparator.comparing((t) -> t.getCode()));
                model.addAttribute("parents", parents);

                List<EventTemplateConfig> configs = new ArrayList<EventTemplateConfig>(
                                template.getEventTemplateConfigSet());
                configs.sort(Comparator.comparing((EventTemplateConfig c) -> c.getApplyFrom()).reversed());
                model.addAttribute("configs", configs);

                EventTemplateConfig selectedConfig = null;
                if (config != null && config.getEventTemplate().equals(template)) {
                        selectedConfig = config;
                } else if (!configs.isEmpty()) {
                        selectedConfig = configs.get(0);
                }

                if (selectedConfig != null) {
                        model.addAttribute("selectedConfig", selectedConfig);
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        try {
                                JsonObject selectedConfigData = gson.fromJson(selectedConfig.getData(),
                                                JsonObject.class);
                                model.addAttribute("selectedConfigData", selectedConfigData);
                                model.addAttribute("selectedConfigDataRaw", gson.toJson(selectedConfigData));

                                final JsonObject tuitionData = selectedConfigData.getAsJsonObject("TUITION");
                                final JsonElement tuitionAccountData = tuitionData == null ? null
                                                : tuitionData.get("accountId");
                                final Account tuitionAccount = tuitionAccountData == null ? null
                                                : FenixFramework.getDomainObject(tuitionAccountData.getAsString());
                                model.addAttribute("tuitionAccount", tuitionAccount);
                                final JsonObject insuranceData = selectedConfigData.getAsJsonObject("INSURANCE");
                                final JsonElement insuranceAccountData = insuranceData == null ? null
                                                : insuranceData.get("accountId");
                                final Account insuranceAccount = insuranceAccountData == null ? null
                                                : FenixFramework.getDomainObject(insuranceAccountData.getAsString());
                                model.addAttribute("insuranceAccount", insuranceAccount);
                                final JsonObject adminFeesData = selectedConfigData.getAsJsonObject("ADMIN_FEES");
                                final JsonElement adminFeesAccountData = adminFeesData == null ? null
                                                : adminFeesData.get("accountId");
                                final Account adminFeesAccount = adminFeesAccountData == null ? null
                                                : FenixFramework.getDomainObject(adminFeesAccountData.getAsString());
                                model.addAttribute("adminFeesAccount", adminFeesAccount);
                                final JsonElement semesterData = selectedConfigData.get("semester");
                                final ExecutionSemester semester = semesterData == null ? null
                                                : FenixFramework.getDomainObject(semesterData.getAsString());
                                model.addAttribute("semester", semester);
                        } catch (JsonSyntaxException jse) {
                                // TODO maybe log this
                        }
                }

                return view("event-template-details");
        }

        @RequestMapping(value = "{template}/create-config", method = RequestMethod.GET)
        public String prepareCreateConfig(@PathVariable EventTemplate template, Model model) {
                model.addAttribute("template", template);

                List<ExecutionSemester> semesters = new ArrayList<ExecutionSemester>(
                                Bennu.getInstance().getExecutionPeriodsSet());
                semesters.sort(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR.reversed());
                model.addAttribute("semesters", semesters);

                List<EventTemplate> children = new ArrayList<EventTemplate>(template.getAlternativeEventTemplateSet());
                children.sort(Comparator.comparing((t) -> t.getCode()));
                model.addAttribute("children", children);

                return view("event-template-config-create");
        }

        @RequestMapping(value = "{template}/create-config", method = RequestMethod.POST)
        public String createConfig(@PathVariable EventTemplate template,
                        @RequestParam(name = "applyFrom") String applyFromStr,
                        @RequestParam(name = "applyUntil") String applyUntilStr,
                        @RequestParam(name = "configData") String configDataStr,
                        @RequestParam(name = "alternativesData") String alternativesDataStr,
                        Model model) {

                // TODO validate input

                DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
                DateTime applyFrom = DateTime.parse(applyFromStr, dateTimeFormat);
                DateTime applyUntil = DateTime.parse(applyUntilStr, dateTimeFormat);
                Gson gson = new Gson();
                JsonObject configData = gson.fromJson(configDataStr, JsonObject.class);

                EventTemplateConfig config = createEventTemplateConfig(template, applyFrom,
                                applyUntil, configData);

                // take alternativesData to create configs for alternative event templates
                JsonObject alternativesData = gson.fromJson(alternativesDataStr, JsonObject.class);
                for (Entry<String, JsonElement> entry : alternativesData.entrySet()) {
                        String id = entry.getKey();
                        EventTemplate alternative = FenixFramework.getDomainObject(id);
                        // TODO modify configData according to entry value
                        createEventTemplateConfig(alternative, applyFrom, applyUntil, configData);
                }

                return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "?config=" +
                                config.getExternalId();
        }

        @RequestMapping(value = "{template}/create-alternative", method = RequestMethod.GET)
        public String prepareCreateAlternative(@PathVariable EventTemplate template, Model model) {
                model.addAttribute("parent", template);
                return prepareCreate(model);
        }

        @RequestMapping(value = "{template}/create-alternative", method = RequestMethod.POST)
        public String createAlternative(@PathVariable("template") EventTemplate parent,
                        EventTemplateBean eventTemplateBean,
                        Model model) {
                EventTemplate template = createEventTemplate(parent, eventTemplateBean.getCode(),
                                eventTemplateBean.getTitle(),
                                eventTemplateBean.getDescription());
                return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config";
        }

        private String view(String view) {
                return "fenixedu-academic/accounting/" + view;
        }

        @Atomic
        private EventTemplate createEventTemplate(final String code, final LocalizedString title,
                        final LocalizedString description) {
                return new EventTemplate(code, title, description);
        }

        @Atomic
        private EventTemplate createEventTemplate(final EventTemplate parent,
                        final String code, final LocalizedString title, final LocalizedString description) {
                EventTemplate eventTemplate = new EventTemplate(code, title, description);
                parent.addAlternativeEventTemplate(eventTemplate);
                return eventTemplate;
        }

        @Atomic
        private EventTemplateConfig createEventTemplateConfig(EventTemplate eventTemplate,
                        final DateTime applyFrom, final DateTime applyUntil,
                        final JsonObject configData) {
                return eventTemplate.createConfig(applyFrom, applyUntil, configData);
        }

        /*
         * Example method to create config for alternatives from parameters:
         * 
         * private void createEventTemplate(final EventTemplate parent, final String
         * code,
         * final LocalizedString title, final LocalizedString description,
         * final DateTime applyFrom, final DateTime applyUntil,
         * final Consumer<JsonObject> tuitionMapConsumer,
         * final Consumer<JsonObject> insuranceMapConsumer,
         * final Consumer<JsonObject> adminFeesMapConsumer,
         * final Consumer<JsonObject> adminFeesPenaltyMapConsumer,
         * final int percentage, final String date1, final String date2,
         * final String tuitionProductCode, final String tuitionProductDescription) {
         * 
         * final BigDecimal tuition = new
         * BigDecimal(JsonUtils.toJson(tuitionMapConsumer).entrySet().stream()
         * .map(Map.Entry::getValue)
         * .mapToDouble(e -> Double.parseDouble(e.getAsString()))
         * .sum());
         * final double tuitionP = tuition.multiply(new BigDecimal("0." + (percentage /
         * 2)))
         * .setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
         * final double tuitionF = tuition.multiply(new BigDecimal("0." + percentage))
         * .setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
         * final double maxCredits = 60 * percentage / 100;
         * final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
         * 
         * final EventTemplate eventTemplate = new EventTemplate(code + percentage,
         * title.append(" " + percentage + "%"),
         * description.append(" " + percentage + "%").append(ls(
         * ".\nEste plano limita as inscrições no ano letivo a " + maxCredits +
         * " ECTS.",
         * ".\nThis plan limits enrolments for the entire year to " + maxCredits +
         * " ECTS.")));
         * eventTemplate.createConfig(applyFrom, applyUntil, config(tuitionMap -> {
         * tuitionMap.addProperty(date1, Double.toString(tuitionP));
         * tuitionMap.addProperty(date2, Double.toString(tuitionP));
         * }, insuranceMapConsumer, adminFeesMapConsumer, adminFeesPenaltyMapConsumer,
         * null, null, maxCredits, null,
         * tuitionProductCode, tuitionProductDescription));
         * parent.addAlternativeEventTemplate(eventTemplate);
         * 
         * final EventTemplate eventTemplateF1 = new EventTemplate(code + percentage +
         * "S1",
         * title.append(ls(" " + percentage + "% Semestre 1", " " + percentage +
         * "% Semester 1")),
         * description.append(ls(" " + percentage + "% Semestre 1", " " + percentage +
         * "% Semester 1")).append(ls(
         * ".\nEste plano apenas permite inscrições no 1º semestre e limita as inscrições a um máximo de "
         * + maxCredits + " ECTS.",
         * ".\nThis plan only allows enrolments during the 1st semester and limits enrolments to a maximum of "
         * + maxCredits + " ECTS.")));
         * eventTemplateF1.createConfig(applyFrom, applyUntil, config(tuitionMap -> {
         * tuitionMap.addProperty(date1, Double.toString(tuitionF));
         * }, insuranceMapConsumer, adminFeesMapConsumer, adminFeesPenaltyMapConsumer,
         * null, null, maxCredits,
         * executionYear.getFirstExecutionPeriod(),
         * tuitionProductCode, tuitionProductDescription));
         * parent.addAlternativeEventTemplate(eventTemplateF1);
         * 
         * final EventTemplate eventTemplateF2 = new EventTemplate(code + percentage +
         * "S2",
         * title.append(ls(" " + percentage + "% Semestre 2", " " + percentage +
         * "% Semester 2")),
         * description.append(ls(" " + percentage + "% Semestre 2", " " + percentage +
         * "% Semester 2")).append(ls(
         * ".\nEste plano apenas permite inscrições no 2º semestre e limita as inscrições a um máximo de "
         * + maxCredits + " ECTS.",
         * ".\nThis plan only allows enrolments during the 2nd semester and limits enrolments to a maximum of "
         * + maxCredits + " ECTS.")));
         * eventTemplateF2.createConfig(applyFrom, applyUntil, config(tuitionMap -> {
         * tuitionMap.addProperty(date2, Double.toString(tuitionF));
         * }, insuranceMapConsumer, adminFeesMapConsumer, adminFeesPenaltyMapConsumer,
         * null, null, maxCredits,
         * executionYear.getLastExecutionPeriod(),
         * tuitionProductCode, tuitionProductDescription));
         * parent.addAlternativeEventTemplate(eventTemplateF2);
         * }
         */

        private LocalizedString ls(final String pt, final String en) {
                return new LocalizedString(PT, pt).with(EN, en);
        }
}
