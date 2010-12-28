<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<html:xhtml/>

<h3>
	<bean:message key="link.inquiries.define.response.period" bundle="INQUIRIES_RESOURCES"/>
</h3>

<br/>

<logic:present name="executionPeriodLVBs">
	<html:form action="/defineResponsePeriods">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
		<table>
			<tr>
				<td>
				</td>
				<td>
					<e:labelValues id="responsePeriodTypes"	enumeration="net.sourceforge.fenixedu.domain.oldInquiries.teacher.InquiryResponsePeriodType" bundle="ENUMERATION_RESOURCES" /> 
					<html:select property="periodType" onchange="this.form.submit()">
						<html:options collection="responsePeriodTypes" property="value" labelProperty="label" />
					</html:select>
					<br/>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" size="1" onchange="this.form.submit()">
				    	<html:options collection="executionPeriodLVBs" labelProperty="label" property="value"/>
				    </html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:present>

<br/>
<br/>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
		<span class="error"><!-- Error messages go here -->
			<bean:write name="message"/>
		</span>
	</html:messages>
	<br/>
	<br/>
</logic:messagesPresent>

<logic:present name="selectedExecutionPeriod">
	<bean:message key="link.inquiries.define.response.period.information" bundle="INQUIRIES_RESOURCES"/>
	<br/><br/>
	<logic:present name="inquiryResponsePeriod" >
		<fr:edit name="inquiryResponsePeriod" action="/defineResponsePeriods.do?method=prepare"
				schema="net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
  				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:destination name="search" path="/announcements/boards.do?method=search"/>
		   	</fr:layout>	    	
		</fr:edit>	
	</logic:present>
	<logic:notPresent name="inquiryResponsePeriod">
		<fr:create action="/defineResponsePeriods.do?method=prepare"
				type="net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod"
				schema="net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod.create">
			<fr:hidden slot="executionPeriod" name="selectedExecutionPeriod"/>
			<fr:hidden slot="type" name="periodType" />
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
  				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:destination name="search" path="/announcements/boards.do?method=search"/>
		   	</fr:layout>
		</fr:create>
	</logic:notPresent>	
</logic:present>
