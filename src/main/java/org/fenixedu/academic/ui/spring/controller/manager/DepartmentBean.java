package org.fenixedu.academic.ui.spring.controller.manager;



public class DepartmentBean {

    private boolean active;
    private String code;
    private String name;
    private String realName;
    private String realNameEn;
    private String externalId;

    public DepartmentBean() {

    }

    public DepartmentBean(boolean active, String code, String name, String realName, String realNameEn, String externalId) {
        this.active = active;
        this.code = code;
        this.name = name;
        this.realName = realName;
        this.realNameEn = realNameEn;
        this.externalId = externalId;
    }

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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
