<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="markSheets" name="totalMarkSheetsCount"/>
<h2><bean:message key="label.markSheet.leftToPrint" arg0="<%= markSheets.toString() %>"/></h2>
<br/>
<h3><bean:message key="label.choosePrinter"/></h3>
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<p><span class="error0"><bean:write name="messages" /></span></p>
	</html:messages>
</logic:messagesPresent>
<br/>
<html:form action="/printMarkSheetWeb.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printMarkSheets"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.markSheet" property="markSheet" value="all"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
	<table>
		<tr>
			<td>
				<ul>
				<logic:iterate id="name"  name="printerNames">
					<li><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.printerName" property="printerName" value='<%= name.toString() %>'>
						<bean:write name="name"/>
					</html:radio></li>
				</logic:iterate>
				</ul>
			</td>
			<logic:messagesPresent message="false">
				<td>
					<html:errors/>
				</td>									
			</logic:messagesPresent>
		</tr>
	</table>
	<br/>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='searchMarkSheet';this.form.submit();" styleClass="inputbutton"><bean:message key="label.back"/></html:cancel>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.print"/></html:submit>
</html:form>

<logic:notEmpty name="curricularCourseMap">

	<br/><br/><hr/>
	<h3><bean:write name="executionPeriod" property="executionYear.year"/> - <bean:write name="executionPeriod" property="semester"/>� Sem</h3>
	<br/>
	<strong><bean:message key="label.markSheet.printMarkSheetsWeb.markSheets.lefToPrint"/></strong>:
	<br/>
	<table class="tstyle4">
		<tr>
			<th><bean:message key="label.markSheet.printMarkSheetsWeb.degree"/></th>
			<th><bean:message key="label.markSheet.printMarkSheetsWeb.degreeCurricularPlan"/></th>
			<th><bean:message key="label.markSheet.printMarkSheetsWeb.curricularCourse"/></th>
			<th><bean:message key="label.markSheet.printMarkSheetsWeb.numberOfMarkSheets"/></th>
		</tr>
	
	<logic:iterate id="entry" name="curricularCourseMap">
	
		<bean:define id="curricularCourse" name="entry" property="key"/>
		<bean:define id="numberOfMarkSheets" name="entry" property="value"/>
		
		<tr>
			<td><bean:write name="curricularCourse" property="degreeCurricularPlan.degree.name"/></td>
			<td><bean:write name="curricularCourse" property="degreeCurricularPlan.name"/></td>
			<td><bean:write name="curricularCourse" property="name"/> - <bean:write name="curricularCourse" property="code"/></td>
			<td align="center"><bean:write name="numberOfMarkSheets"/></td>
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>
