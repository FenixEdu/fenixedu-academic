<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.groupLanguage" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<bean:message key="functionalities.groupLanguage.list.help" bundle="FUNCTIONALITY_RESOURCES"/>

<fr:view name="builders" layout="tabular" schema="functionalities.groups.info">
    <fr:layout>
    </fr:layout>
</fr:view>