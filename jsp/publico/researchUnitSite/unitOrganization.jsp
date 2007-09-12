<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<p>
	<bean:message key="label.organization" bundle="SITE_RESOURCES"/> | <html:link href="<%= "viewResearchUnitSite.do?siteID=" + site.getIdInternal() + "&method=showResearchers"%>"><bean:message key="label.members" bundle="SITE_RESOURCES"/></html:link>
</p>

<h2>
    <bean:message key="label.organization" bundle="SITE_RESOURCES"/> 
</h2>

<div>
	<fr:view name="site" property="unit" layout="organigram-with-card">
		<fr:layout>
			<fr:property name="classes" value="ostructure1"/>
			<fr:property name="rootUnitClasses" value="osrootunit"/>
			<fr:property name="unitClasses" value="osunit"/>
			<fr:property name="levelClasses" value="oslevel"/>
			<fr:property name="employeesSectionClasses" value="osemployees"/>
			<fr:property name="employeesClasses" value="osperson osworker"/>
			<fr:property name="functionsClasses" value="osfunction"/>
			<fr:property name="implicitFunctionsClasses" value="osfunction osimplicit"/>
			<fr:property name="membersClasses" value="osperson osmember nobullet"/>
			
			
			<fr:property name="showDates" value="false"/>
			<fr:property name="unitShown" value="false"/>
			<fr:property name="cardProperty(subLayout)" value="values-as-list"/>
			<fr:property name="cardProperty(subSchema)" value="present.research.member"/>
		</fr:layout>	
	</fr:view>
</div>