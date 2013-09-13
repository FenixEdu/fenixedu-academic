<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.unitSite.unit.subunits" bundle="SITE_RESOURCES"/> 
</h2>

<logic:empty name="subunits">
	<em><bean:message key="message.unitSite.unit.subunits.empty" bundle="SITE_RESOURCES"/></em>
</logic:empty>

<logic:notEmpty name="subunits">
	<ul>
		<logic:iterate id="subunit" name="subunits">
			<li>
				<app:contentLink name="subunit" property="site">
					<fr:view name="subunit" property="nameI18n"/>
				</app:contentLink>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
