<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:notPresent name="siteView" property="component">
	<h2><bean:message key="message.bibliography.not.available"/></h2>
</logic:notPresent>

<logic:present name="siteView" property="component" >
	<bean:define id="component" name="siteView" property="component" />
<logic:empty name="component" property="bibliographicReferences">
	<h2><bean:message key="message.bibliography.not.available"/></h2>
</logic:empty>	
<logic:notEmpty name="component" property="bibliographicReferences" >
<h2><bean:message key="message.recommendedBibliography"/></h2>
<table>
	<tbody>
		<logic:iterate id="bibliographicReference" name="component" property="bibliographicReferences">
		    <logic:notEqual name="bibliographicReference" property="optional" value="true">
				<tr>
					<td><h4><bean:message key="label.bibliography.title" />:</h4></td>
				    <td><bean:write name="bibliographicReference" property="title"/></td>
				</tr>
				<logic:notEmpty name="bibliographicReference" property="authors">
					<tr>
						<td><h4><bean:message key="label.bibliography.authors" />:</h4></td>
					    <td><bean:write name="bibliographicReference" property="authors"/></td>
					</tr>
				</logic:notEmpty>
				<logic:notEmpty name="bibliographicReference" property="reference">				
					<tr>
						<td><h4><bean:message key="label.bibliography.reference" />:</h4></td>
					    <td><h4><bean:write name="bibliographicReference" property="reference"/></td>
					</tr>
				</logic:notEmpty>
				<logic:notEmpty name="bibliographicReference" property="year">
					<tr>
						<td style="vertical-align:top"><h4><bean:message key="label.bibliography.year" />:</h4></td>
					    <td style="vertical-align:top"><bean:write name="bibliographicReference" property="year"/> <br /><br /></td>
					</tr>
				</logic:notEmpty>
		    </logic:notEqual>
		</logic:iterate>
	</tbody>
</table>
<h2><bean:message key="message.optionalBibliography"/></h2>
<table>
	<tbody>
         <logic:iterate id="bibliographicReference" name="component" property="bibliographicReferences">
            <logic:notEqual name="bibliographicReference" property="optional" value="false">
				<tr>
					<td><h4><bean:message key="label.bibliography.title" />:</h4></td>
				    <td><bean:write name="bibliographicReference" property="title"/></td>
				</tr>
				<logic:notEmpty name="bibliographicReference" property="authors">
					<tr>
						<td><h4><bean:message key="label.bibliography.authors" />:</h4></td>
					    <td><bean:write name="bibliographicReference" property="authors"/></td>
					</tr>
				</logic:notEmpty>
				<logic:notEmpty name="bibliographicReference" property="reference">				
					<tr>
						<td><h4><bean:message key="label.bibliography.reference" />:</h4></td>
					    <td><bean:write name="bibliographicReference" property="reference"/></td>
					</tr>
				</logic:notEmpty>
				<logic:notEmpty name="bibliographicReference" property="year">
					<tr>
						<td style="vertical-align:top"><h4><bean:message key="label.bibliography.year" />:</h4></td>
					    <td style="vertical-align:top"><bean:write name="bibliographicReference" property="year"/> <br /><br /></td>
					</tr>
				</logic:notEmpty>
            </logic:notEqual>
        </logic:iterate>
	</tbody>
</table>
</logic:notEmpty>
</logic:present>