<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<bean:define id="markSheets" name="totalMarkSheetsCount"/>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print.web.markSheets"/></h2>


<html:form action="/printMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printMarkSheets"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.markSheet" property="markSheet" value="all"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<html:select property="ecID" onchange="this.form.method.value='choosePrinterMarkSheetsWebPostBack';this.form.submit();">	
		<html:options collection="periods" property="value" labelProperty="label"/>
	</html:select>
	<br/>
	<html:select property="dcpID" onchange="this.form.method.value='choosePrinterMarkSheetsWebPostBack';this.form.submit();">	
		<html:option value=""><bean:message key="label.dropDown.all" bundle="ENUMERATION_RESOURCES" /><!-- w3c complient --></html:option>
		<html:options collection="degreeCurricularPlans" property="value" labelProperty="label"/>
	</html:select>
	
	<p class="mvert2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.leftToPrint" arg0="<%= markSheets.toString() %>"/>.</p>
	
	<p class="mtop2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.choosePrinter"/>:</p>
	<logic:messagesPresent message="true">
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<p><span class="error0"><bean:write name="messages" /></span></p>
		</html:messages>
	</logic:messagesPresent>
	
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
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='searchMarkSheet';this.form.submit();" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back"/></html:cancel>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print"/></html:submit>
</html:form>

<logic:notEmpty name="curricularCourseMap">

	<br/><hr/>
	<h3><bean:write name="executionPeriod" property="executionYear.year"/> - <bean:write name="executionPeriod" property="semester"/>o Sem</h3>
	<br/>
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.printMarkSheetsWeb.markSheets.lefToPrint"/></strong>:
	<br/>
	<table class="tstyle4">
		<tr>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.printMarkSheetsWeb.degree"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.printMarkSheetsWeb.degreeCurricularPlan"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.printMarkSheetsWeb.curricularCourse"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.printMarkSheetsWeb.numberOfMarkSheets"/></th>
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
