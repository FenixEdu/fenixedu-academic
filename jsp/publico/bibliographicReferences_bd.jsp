<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
		<logic:iterate id="bibliographicReference" name="component" property="bibliographicReferences">
			<logic:notEqual name="bibliographicReference" property="optional" value="true">
				<div class="info-lst">
					<table cellpadding="2" cellspacing="2">
						<tr>
							<td width="70"><strong><bean:message key="label.bibliography.title" /></strong></td>
							<td><bean:write name="bibliographicReference" property="title" filter="false"/></td>
						</tr>
						<logic:notEmpty name="bibliographicReference" property="authors">
						<tr>
							<td width="70"><strong><bean:message key="label.bibliography.authors" /></strong></td>
							<td><bean:write name="bibliographicReference" property="authors" filter="false"/></td>
						</tr>
						</logic:notEmpty>
						<logic:notEmpty name="bibliographicReference" property="reference">				
							<tr>
								<td width="70"><strong><bean:message key="label.bibliography.reference" /></strong></td>
								<td><bean:write name="bibliographicReference" property="reference" filter="false"/></td>
							</tr>
						</logic:notEmpty>
						<logic:notEmpty name="bibliographicReference" property="year">
							<tr>
								<td width="70"><strong><bean:message key="label.bibliography.year" /></strong></td>
								<td><bean:write name="bibliographicReference" property="year" filter="false"/></td>
							</tr>
						</logic:notEmpty>
					</table>
				</div>
		    </logic:notEqual>
		</logic:iterate>

<br />
<br />

		<h2><bean:message key="message.optionalBibliography"/></h2>
         <logic:iterate id="bibliographicReference" name="component" property="bibliographicReferences">
            <logic:notEqual name="bibliographicReference" property="optional" value="false">
				<div class="info-lst">
					<table cellpadding="1" cellspacing="2">
						<tr>
							<td width="70"><strong><bean:message key="label.bibliography.title" /></strong></td>
							<td><bean:write name="bibliographicReference" property="title" filter="false"/></td>
						</tr>
						<logic:notEmpty name="bibliographicReference" property="authors">
						<tr>
							<td width="70"><strong><bean:message key="label.bibliography.authors" /></strong></td>
							<td><bean:write name="bibliographicReference" property="authors" filter="false"/></td>
						</tr>
						</logic:notEmpty>
						<logic:notEmpty name="bibliographicReference" property="reference">				
						<tr>
							<td width="70"><strong><bean:message key="label.bibliography.reference" /></strong></td>
							<td><bean:write name="bibliographicReference" property="reference" filter="false"/></td>
						</tr>
						</logic:notEmpty>
						<logic:notEmpty name="bibliographicReference" property="year">
						<tr>
							<td width="70"><strong><bean:message key="label.bibliography.year" /></strong></td>
							<td><bean:write name="bibliographicReference" property="year" filter="false"/></td>
						</tr>
						</logic:notEmpty>
					</table>
				</div>
            </logic:notEqual>
        </logic:iterate>
	</logic:notEmpty>
</logic:present>