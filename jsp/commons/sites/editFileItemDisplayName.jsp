<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>


<bean:define id="actionName" name="siteActionName" />
<bean:define id="contextParam" name="siteContextParam" />
<bean:define id="contextParamValue" name="siteContextParamValue" />
<bean:define id="context"
	value="<%= contextParam + "=" + contextParamValue %>" />
<bean:define id="sectionID" name="section" property="idInternal" />
<bean:define id="itemID" name="item" property="idInternal" />

<h2>
	<bean:message key="label.item" bundle="SITE_RESOURCES" /> 
	<fr:view name="item" property="name" />
</h2>

<fr:form
	action="<%= actionName + "?method=section&sectionID=" + sectionID +"&" + context + "#item-" + itemID%>">
	<table class="tstyle5">
		<tr>
			<td><bean:message key="label.displayName"
				bundle="SITE_RESOURCES" />:</td>
			<td><fr:edit name="fileItem" slot="displayName" >
						<fr:layout>
	                		<fr:property name="size" value="40"/>
	            		</fr:layout>
					</fr:edit>
			</td>
		</tr>
		<tr>
			<td><html:submit>
				<bean:message key="button.site.functions.order.save"
					bundle="SITE_RESOURCES" />
			</html:submit></td>
		</tr>
	</table>
</fr:form>
