<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

	<%-- MANDATORY FIELDS --%>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.publicationType" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.publicationType" size="60" property="publicationType"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.title" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="20" property="title"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.numberPages" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.numberPages" size="20" property="numberPages"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" />
			<bean:define id="valueMonth" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueMonth" value="3">
				<bean:message key="message.publicationAttribute.monthInith" />
			</logic:equal>
			<logic:notEqual name="valueMonth" value="3">
				<bean:message key="message.publicationAttribute.month" />
			</logic:notEqual>
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" property="month">
				<logic:iterate id="month" name="monthList" >
					<html:option value='<%=month.toString()%>'>
						<bean:write name="month" />		
					</html:option>
				</logic:iterate>
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" />
			<bean:define id="valueYear" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.yearInith" />
			</logic:equal>
			<logic:notEqual name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.year" />
			</logic:notEqual>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" size="9" property="year"/>
		</td>
	</tr>
	
	
	<%-- NON-MANDATORY FIELDS --%>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.language" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.language" size="20" property="language"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.editor" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.editor" size="30" property="editor"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.editorCity" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.editorCity" size="20" property="editorCity"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.format" />
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" property="format">
				<html:options collection="formatList" property="format" />
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.country" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.country" size="20" property="country"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.issn" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.issn" size="20" property="issn"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.url" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.url" size="70" property="url"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.observations" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.observation" size="20" property="observation"/>
		</td>
	</tr>

