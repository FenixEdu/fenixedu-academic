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


<h2>
	<bean:message key="label.item" bundle="SITE_RESOURCES" /> 
	<logic:present name="item">
		<fr:view name="item" property="name" />
	</logic:present>
	<logic:notPresent name="item">
		<fr:view name="section" property="name" />
	</logic:notPresent>
</h2>


<bean:define id="url" value="<%=  actionName + "?method=section&sectionID=" + sectionID +"&" + context %>"/>
<logic:present name="item">
	<bean:define id="itemID" name="item" property="idInternal" />
	<bean:define id="url" value="<%= url +  "#item-" + itemID %>"/>
</logic:present>

<div class="dinline forminline">
	<fr:form
		action="<%=  url %>">
		<table class="tstyle5 thleft thlight thmiddle">
			<tr>
				<th><bean:message key="label.file" bundle="SITE_RESOURCES"/>:</th>
				<td><fr:view name="fileItem" property="filename"/></td>	
			</tr>
			<tr>
				<th><bean:message key="label.displayName"
					bundle="SITE_RESOURCES" />:</th>
				<td><fr:edit name="fileItem" slot="displayName" >
							<fr:layout>
		                		<fr:property name="size" value="40"/>
		            		</fr:layout>
						</fr:edit>
				</td>
			</tr>
			<tr>
			<tr>
				<th><bean:message key="label.fileVisible.question" bundle="SITE_RESOURCES"/></th>
				<td><fr:edit name="node" slot="visible"/></td>	
			</tr>
		</table>
		
		
		<html:submit>
			<bean:message key="button.submit" bundle="SITE_RESOURCES" />
		</html:submit>
	</fr:form>
	<fr:form action="<%= url %>">
		<html:submit>
			<bean:message key="button.cancel" bundle="SITE_RESOURCES" />
		</html:submit>
	</fr:form>
</div>

        