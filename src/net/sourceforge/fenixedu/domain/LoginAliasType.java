package net.sourceforge.fenixedu.domain;

public enum LoginAliasType {

    INSTITUTION_ALIAS,
    ROLE_TYPE_ALIAS,
    CUSTOM_ALIAS;
    
    public String getName() {
        return name();
    }
}
