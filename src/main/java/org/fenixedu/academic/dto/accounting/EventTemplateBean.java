package org.fenixedu.academic.dto.accounting;

import org.fenixedu.commons.i18n.LocalizedString;

public class EventTemplateBean {
    private String code;
    private LocalizedString title;
    private LocalizedString description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalizedString getTitle() {
        return title;
    }

    public void setTitle(LocalizedString title) {
        this.title = title;
    }

    public LocalizedString getDescription() {
        return description;
    }

    public void setDescription(LocalizedString description) {
        this.description = description;
    }

}
