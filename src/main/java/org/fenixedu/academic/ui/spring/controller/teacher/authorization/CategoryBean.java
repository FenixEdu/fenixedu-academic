package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import org.fenixedu.commons.i18n.LocalizedString;

public class CategoryBean {
    private String code;
    private LocalizedString name;
    private Integer weight;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalizedString getName() {
        return name;
    }

    public void setName(LocalizedString name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
