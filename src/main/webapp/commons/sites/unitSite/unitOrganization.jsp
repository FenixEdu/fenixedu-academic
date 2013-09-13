<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.unitSite.unit.organization" bundle="SITE_RESOURCES"/> 
</h2>

<div>
	<fr:view name="site" property="unit" layout="organigram">
		<fr:layout>
			<fr:property name="showEmptyFunctions" value="false"/>
			<fr:property name="showOnlyPeopleWithFunctions" value="true"/>
			<fr:property name="showDates" value="false"/>
			<fr:property name="unitShown" value="false"/>
		</fr:layout>	
	</fr:view>
</div>