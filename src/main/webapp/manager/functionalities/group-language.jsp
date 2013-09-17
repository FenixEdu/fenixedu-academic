<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="title.groupLanguage" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<bean:message key="functionalities.groupLanguage.list.help" bundle="FUNCTIONALITY_RESOURCES"/>

<fr:view name="builders" layout="tabular" schema="functionalities.groups.info">
    <fr:layout>
    </fr:layout>
</fr:view>