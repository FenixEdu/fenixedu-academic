package org.fenixedu.academic.ui.spring.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang.math.NumberUtils;
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
import pt.ist.fenixframework.Atomic.TxMode;

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "accounting.management.event.templates.title")
@RequestMapping(AccountingEventTemplateManagerController.REQUEST_MAPPING)
public class AccountingEventTemplateManagerController {

    private static final Logger logger = LoggerFactory.getLogger(AccountingEventTemplateManagerController.class);
    public static final String REQUEST_MAPPING = "/event-template-management";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(boolean showAll, Model model) {
        List<EventTemplate> templates = new ArrayList<EventTemplate>(
                showAll ? Bennu.getInstance().getEventTemplateSet() : EventTemplate.getTopLevelTemplates());
        templates.sort(Comparator.comparing((t) -> t.getCode()));
        model.addAttribute("templates", templates);
        model.addAttribute("showAll", showAll);
        return view("event-template-list");
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String prepareCreate(Model model) {
        return view("event-template-create");
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(EventTemplateBean eventTemplateBean, Model model) {
        logger.info(
                "Received create template request: code={}; title={}; description={}",
                eventTemplateBean.getCode(), eventTemplateBean.getTitle(),
                eventTemplateBean.getDescription().toString());

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

                final JsonObject tuitionData = selectedConfigData
                        .getAsJsonObject(EventTemplate.Type.TUITION.toString());
                final JsonElement tuitionAccountData = tuitionData == null ? null
                        : tuitionData.get("accountId");
                final Account tuitionAccount = tuitionAccountData == null ? null
                        : FenixFramework.getDomainObject(tuitionAccountData.getAsString());
                model.addAttribute("tuitionAccount", tuitionAccount);
                final JsonObject insuranceData = selectedConfigData
                        .getAsJsonObject(EventTemplate.Type.INSURANCE.toString());
                final JsonElement insuranceAccountData = insuranceData == null ? null
                        : insuranceData.get("accountId");
                final Account insuranceAccount = insuranceAccountData == null ? null
                        : FenixFramework.getDomainObject(insuranceAccountData.getAsString());
                model.addAttribute("insuranceAccount", insuranceAccount);
                final JsonObject adminFeesData = selectedConfigData
                        .getAsJsonObject(EventTemplate.Type.ADMIN_FEES.toString());
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
                logger.error(
                        "Unable to deliver Event Template details page due to malformed config JSON data (as detailed in the following exception)",
                        jse);
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

        logger.info(
                "Received create config request: template={} ({}); applyFrom={}; applyUntil={}; configData={}; alternativesData={}",
                template.getCode(), template.getExternalId(), applyFromStr, applyUntilStr, configDataStr,
                alternativesDataStr);

        // validate dates
        if (!(validateDate(applyFromStr, "yyyy-MM-dd") && validateDate(applyUntilStr, "yyyy-MM-dd"))) {
            // invalid dates, return to previous page with error message
            logger.warn("Request rejected due to applyFrom/applyUntil badly formatted.");
            return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config?error=true";
        }
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime applyFrom = DateTime.parse(applyFromStr, dateTimeFormat);
        DateTime applyUntil = DateTime.parse(applyUntilStr, dateTimeFormat);

        // validate interval
        if (!applyFrom.isBefore(applyUntil)) {
            // invalid dates, return to previous page with error message
            logger.warn("Request rejected due to applyFrom-applyUntil not being a valid time interval.");
            return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config?error=true";
        }

        Gson gson = new Gson();
        JsonElement configDataEl = gson.fromJson(configDataStr, JsonElement.class);
        if (!validateConfigDataJson(configDataEl)) {
            // invalid config data for this template, return to previous page with error
            // message
            logger.warn("Request rejected due to config data not being valid.");
            return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config?error=true";
        }
        JsonElement alternativesDataEl = gson.fromJson(alternativesDataStr, JsonElement.class);
        if (!validateAlternativesDataJson(alternativesDataEl)) {
            // invalid patch data for template alternatives, return to previous page with
            // error message
            logger.warn("Request rejected due to alternative data not being valid.");
            return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config?error=true";
        }

        // create config and alternate data
        JsonObject configData = configDataEl.getAsJsonObject();
        JsonObject alternativesData = alternativesDataEl.getAsJsonObject();

        EventTemplateConfig config;
        try {
            config = createEventTemplateConfig(template, applyFrom,
                    applyUntil, configData, alternativesData);
        } catch (Exception e) {
            // given config and patches cause an exception, return to previous page with
            // error message
            logger.warn("Request rejected due to exception thrown when attempting to save config data.", e);
            return "redirect:" + REQUEST_MAPPING + "/" + template.getExternalId() + "/create-config?error=true";
        }

        // configs created successfully, return to template page
        logger.info("Request accepted, config created ({})", config.getExternalId());
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
        logger.info(
                "Received create alternative request: parent={} ({}); code={}; title={}; description={}",
                parent.getCode(), parent.getExternalId(), eventTemplateBean.getCode(), eventTemplateBean.getTitle(),
                eventTemplateBean.getDescription().toString());

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

    @Atomic(mode = TxMode.WRITE)
    private EventTemplate createEventTemplate(final EventTemplate parent,
            final String code, final LocalizedString title, final LocalizedString description) {
        EventTemplate eventTemplate = new EventTemplate(code, title, description);
        parent.addAlternativeEventTemplate(eventTemplate);
        return eventTemplate;
    }

    @Atomic(mode = TxMode.WRITE)
    private EventTemplateConfig createEventTemplateConfig(EventTemplate template,
            final DateTime applyFrom, final DateTime applyUntil,
            final JsonObject configData, final JsonObject alternativesData) {

        EventTemplateConfig config = template.createConfig(applyFrom, applyUntil, configData);

        // take alternativesData to create configs for alternative event templates
        for (Entry<String, JsonElement> alternativeEntry : alternativesData.entrySet()) {
            String id = alternativeEntry.getKey();
            JsonArray patches = alternativeEntry.getValue().getAsJsonArray();

            Optional<EventTemplate> maybeAlternative = template.getAlternativeEventTemplateSet().stream()
                    .filter((a) -> a.getExternalId().equals(id)).findFirst();

            maybeAlternative.ifPresent((alternative) -> {
                JsonObject configDataForAlternative = configData.deepCopy();
                // modify configDataForAlternative according to patches
                for (JsonElement patchEl : patches) {
                    JsonObject patch = patchEl.getAsJsonObject();
                    applyJsonPatch(configDataForAlternative, patch);
                }

                alternative.createConfig(applyFrom, applyUntil, configDataForAlternative);
            });
        }

        return config;
    }

    private void applyJsonPatch(JsonObject original, JsonObject patch) {
        String op = patch.get("op").getAsString();
        String path = patch.get("path").getAsString();
        String property;

        if ("add".equals(op)) {
            // add or replace
            JsonElement value = patch.get("value");
            switch (path) {
                case "/TUITION/dueDateAmountMap":
                    property = path.substring("/TUITION/".length()); // remove the "/TUITION/" to obtain the correct
                                                                     // name
                    original.getAsJsonObject("TUITION").add(property, value);
                    break;
                case "/maxCredits":
                    // intentional fall through!
                case "/semester":
                    property = path.substring(1); // remove the "/" to obtain the correct name
                    original.add(property, value);
                    break;
                default:
                    // for our purposes, we need only to support these paths
                    // any other value is ignored for now
                    return;
            }
        }
        if ("remove".equals(patch.get("op").getAsString())) {
            switch (path) {
                case "/TUITION/dueDateAmountMap":
                    property = path.substring("/TUITION/".length()); // remove the "/TUITION/" to obtain the correct
                                                                     // name
                    original.getAsJsonObject("TUITION").remove(property);
                    break;
                case "/maxCredits":
                    // intentional fall through!
                case "/semester":
                    property = path.substring(1); // remove the "/" to obtain the correct name
                    original.remove(property);
                    break;
                default:
                    // for our purposes, we need only to support these paths
                    // any other value is ignored for now
                    return;
            }
        }
        // there are other ops, but we don't need to implement them for now
    }

    /*
     * VALIDATION METHODS
     * note: for now, these validation methods only return whether the json data is
     * valid or not, without any additional information.
     * In the future, it might be useful to get more feedback on where it failed
     * validation.
     */

    private boolean validateConfigDataJson(JsonElement configData) {
        if (!configData.isJsonObject()) {
            return false;
        }

        JsonObject configDataObj = configData.getAsJsonObject();
        if (!(configDataObj.has(EventTemplate.Type.TUITION.toString())
                && configData.getAsJsonObject().has(EventTemplate.Type.INSURANCE.toString())
                && configData.getAsJsonObject().has(EventTemplate.Type.ADMIN_FEES.toString()))) {
            // this config object is missing required fields
            return false;
        }

        if (!(validateTuitionDataJson(configDataObj.get(EventTemplate.Type.TUITION.toString()))
                && validateInsuranceDataJson(configDataObj.get(EventTemplate.Type.INSURANCE.toString()))
                && validateAdminFeesDataJson(configDataObj.get(EventTemplate.Type.ADMIN_FEES.toString())))) {
            // malformed required fields
            return false;
        }

        if (configDataObj.has("maxCredits")) {
            JsonElement maxCreditsData = configDataObj.get("maxCredits");
            if (!(maxCreditsData.isJsonPrimitive() && maxCreditsData.getAsJsonPrimitive().isNumber())) {
                // maxCredits exists but is malformed
                return false;
            }
        }

        if (configDataObj.has("semester")) {
            JsonElement semesterData = configDataObj.get("semester");
            if (!(semesterData.isJsonPrimitive() && semesterData.getAsJsonPrimitive().isString())) {
                // semester exists but is malformed
                return false;
            }
            // check whether semester is a valid ExecutionSemester
            boolean semesterExists = Bennu.getInstance().getExecutionPeriodsSet().stream()
                    .anyMatch(s -> s.getExternalId().equals(semesterData.getAsString()));
            if (!semesterExists) {
                return false;
            }
        }

        // additional fiels are always valid

        return true;
    }

    private boolean validateTuitionDataJson(JsonElement tuitionData) {
        // TUITION is a standard payment object with an optional additional object:
        // byECTS

        if (!validateStandardPaymentObjectData(tuitionData)) {
            return false;
        }

        JsonObject tuitionDataObj = tuitionData.getAsJsonObject();

        if (tuitionDataObj.has("byECTS")) {
            JsonElement byEctsData = tuitionDataObj.get("byECTS");
            if (!byEctsData.isJsonObject()) {
                return false;
            }

            JsonObject byEctsDataObj = byEctsData.getAsJsonObject();
            if (!(byEctsDataObj.has("daysToPay")
                    && byEctsDataObj.get("daysToPay").isJsonPrimitive()
                    && byEctsDataObj.getAsJsonPrimitive("daysToPay").isNumber())) {
                // byECTS/daysToPay is invalid
                return false;
            }

            if (!(byEctsDataObj.has("value")
                    && byEctsDataObj.get("value").isJsonPrimitive()
                    && byEctsDataObj.getAsJsonPrimitive("value").isString()
                    && NumberUtils.isNumber(byEctsDataObj.getAsJsonPrimitive("value").getAsString()))) {
                // byECTS/value is invalid
                return false;
            }
        }

        return true;
    }

    private boolean validateInsuranceDataJson(JsonElement insuranceData) {
        // INSURANCE is a standard payment object
        return validateStandardPaymentObjectData(insuranceData);
    }

    private boolean validateAdminFeesDataJson(JsonElement adminFeesData) {
        // ADMIN_FEE is a standard payment object with an additional amount map:
        // penaltyAmountMap
        if (!validateStandardPaymentObjectData(adminFeesData)) {
            return false;
        }

        JsonObject adminFeesDataObj = adminFeesData.getAsJsonObject();
        if (!(adminFeesDataObj.has("penaltyAmountMap")
                && validateAmountMapData(adminFeesDataObj.get("penaltyAmountMap")))) {
            // malformed penaltyAmountMap
            return false;
        }

        return true;
    }

    private boolean validateStandardPaymentObjectData(JsonElement paymentData) {
        if (!paymentData.isJsonObject()) {
            return false;
        }

        // standard payment objects should have valid dueDateAmountMap, productCode,
        // productDescription and accountId

        JsonObject paymentDataObj = paymentData.getAsJsonObject();
        if (!(paymentDataObj.has("dueDateAmountMap")
                && validateAmountMapData(paymentDataObj.get("dueDateAmountMap")))) {
            // malformed dueDateAmountMap
            return false;
        }

        if (!(paymentDataObj.has("productCode")
                && paymentDataObj.get("productCode").isJsonPrimitive()
                && paymentDataObj.getAsJsonPrimitive("productCode").isString())) {
            // TODO? check if corresponds to valid product
            // productCode is invalid
            return false;
        }

        if (!(paymentDataObj.has("productDescription")
                && paymentDataObj.get("productDescription").isJsonPrimitive()
                && paymentDataObj.getAsJsonPrimitive("productDescription").isString())) {
            // TODO? check if corresponds to valid product
            // productDescription is invalid
            return false;
        }

        if (!(paymentDataObj.has("accountId")
                && paymentDataObj.get("accountId").isJsonPrimitive()
                && paymentDataObj.getAsJsonPrimitive("accountId").isString())) {
            // TODO check if corresponds to valid account
            // accountId is invalid
            return false;
        }

        return true;
    }

    private boolean validateAmountMapData(JsonElement amountMapData) {
        if (!amountMapData.isJsonObject()) {
            return false;
        }

        for (Entry<String, JsonElement> entry : amountMapData.getAsJsonObject().entrySet()) {
            if (!validateDate(entry.getKey(), "dd/MM/yyyy")) {
                // this amountMap has an invalid date as a key
                return false;
            }

            if (!(entry.getValue().isJsonPrimitive()
                    && entry.getValue().getAsJsonPrimitive().isString()
                    && NumberUtils.isNumber(entry.getValue().getAsString()))) {
                // the amountMap has an invalid amount as a value
                return false;
            }
        }

        return true;

    }

    private boolean validateAlternativesDataJson(JsonElement alternativeData) {
        if (!alternativeData.isJsonObject()) {
            return false;
        }

        for (Entry<String, JsonElement> patch : alternativeData.getAsJsonObject().entrySet()) {
            if (!patch.getValue().isJsonArray()) {
                return false;
            }

            for (JsonElement patchOpEl : patch.getValue().getAsJsonArray()) {
                if (!patchOpEl.isJsonObject()) {
                    return false;
                }
                JsonObject patchOpObj = patchOpEl.getAsJsonObject();

                if (!(patchOpObj.has("op") && patchOpObj.get("op").isJsonPrimitive()
                        && patchOpObj.getAsJsonPrimitive("op").isString())) {
                    return false;
                }
                String patchOp = patchOpObj.getAsJsonPrimitive("op").getAsString();
                if (!("remove".equals(patchOp) || "add".equals(patchOp))) {
                    // for our purposes, we need only to support these two ops
                    return false;
                }

                if (!(patchOpObj.has("path") && patchOpObj.get("path").isJsonPrimitive()
                        && patchOpObj.getAsJsonPrimitive("path").isString())) {
                    return false;
                }
                String patchPath = patchOpObj.getAsJsonPrimitive("path").getAsString();

                if ("remove".equals(patchOp)) {
                    // no more checks necessary
                    return true;
                } else if ("add".equals(patchOp) && !patchOpObj.has("value")) {
                    // we need a value for this op
                    return false;
                }

                // we know there's a value field, we need to validate it
                switch (patchPath) {
                    case "/TUITION/dueDateAmountMap":
                        if (!validateAmountMapData(patchOpObj.get("value"))) {
                            // new value for /TUITION/dueDateAmountMap is malformed
                            return false;
                        }
                        break;
                    case "/maxCredits":
                        if (!(patchOpObj.get("value").isJsonPrimitive()
                                && patchOpObj.get("value").getAsJsonPrimitive().isNumber())) {
                            // new value for /maxCredits is malformed
                            return false;
                        }
                        break;
                    case "/semester":
                        if (!(patchOpObj.get("value").isJsonPrimitive()
                                && patchOpObj.get("value").getAsJsonPrimitive().isString())) {
                            // new value for /semester is malformed
                            return false;
                        }
                        // check whether semester is a valid ExecutionSemester
                        boolean semesterExists = Bennu.getInstance().getExecutionPeriodsSet().stream()
                                .anyMatch(s -> s.getExternalId().equals(patchOpObj.get("value").getAsString()));
                        if (!semesterExists) {
                            return false;
                        }
                        break;
                    default:
                        // for our purposes, we need only to support these paths
                        // any other value is denied
                        return false;
                }
            }
        }

        return true;
    }

    private boolean validateDate(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
            formatter.parseDateTime(date);
        } catch (IllegalArgumentException iae) {
            // this is an invalid date
            return false;
        }
        return true;
    }
}
