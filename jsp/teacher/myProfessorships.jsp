<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:importAttribute />

<logic:notEmpty name="professorshipList" >	
	<bean:define id="titleKey">
		<tiles:getAsString name="title"/>
	</bean:define>
	<h2><bean:message key="<%= titleKey %>" /></h2>
		<table width="50%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.professorships.acronym" />
				</td>
				<td class="listClasses-header"><bean:message key="label.professorships.name" />
				</td>
			</tr>
			<bean:define id="link">
				<tiles:getAsString name="link"/>
			</bean:define>
			<logic:iterate name="professorshipList" id="professorship" >
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				
				<tr>
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>	 	

