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
				<bean:define id="subunitId" name="subunit" property="idInternal"/>
				<bean:define id="url" name="<%= "viewSite" + subunitId %>"/>
				<html:link page="<%= url.toString() %>">
					<fr:view name="subunit" property="nameI18n"/>
				</html:link>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
