<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

	<%-- MANDATORY FIELDS --%>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.subType" />
		</td>
		<td>
			<html:select property="subtype">
				<html:options collection="subTypeList" property="subtype" />
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.title" />
		</td>
		<td>
			<html:text size="20" property="title"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.numberPages" />
		</td>
		<td>
			<html:text size="20" property="numberPages"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.language" />
		</td>
		<td>
			<html:text size="20" property="language"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.format" />
		</td>
		<td>
			<html:select property="format">
				<html:options collection="formatList" property="format" />
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:define id="valueMonth" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueMonth" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.monthInith" />
			</logic:equal>
			<logic:notEqual name="valueMonth" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.month" />
			</logic:notEqual>
		</td>
		<td>
			<html:select property="month">
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
			<bean:define id="valueYear" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.yearInith" />
			</logic:equal>
			<logic:notEqual name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.year" />
			</logic:notEqual>
		</td>
		<td>
			<html:text size="9" property="year"/>
		</td>
	</tr>
				
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.month_end" />
		</td>
		<td>
			<html:select property="month_end">
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
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.year_end" />
		</td>
		<td>
			<html:text size="9" property="year_end"/>
		</td>
	</tr>

	
	<%-- NON-MANDATORY FIELDS --%>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.university" />
		</td>
		<td>
			<html:text size="40" property="university"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.local" />
		</td>
		<td>
			<html:text size="40" property="local"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.url" />
		</td>
		<td>
			<html:text size="70" property="url"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.observations" />
		</td>
		<td>
			<html:text size="20" property="observation"/>
		</td>
	</tr>
