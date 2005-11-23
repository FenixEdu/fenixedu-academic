<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h3>
	<bean:message key="link.inquiries.define.response.period" bundle="INQUIRIES_RESOURCES"/>
</h3>

<br/>

<logic:present name="executionPeriodLVBs">
	<html:form action="/defineResponsePeriods">
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="prepare"/>
		<table>
			<tr>
				<td>
				</td>
				<td>
					<html:select property="executionPeriodID" size="1" onchange="this.form.submit()">
				    	<html:options collection="executionPeriodLVBs" labelProperty="label" property="value"/>
				    </html:select>
				</td>
			</tr>
		</table>
	</html:form>
</logic:present>

<br/>
<br/>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
		<span class="error">
			<bean:write name="message"/>
		</span>
	</html:messages>
	<br/>
	<br/>
</logic:messagesPresent>

<logic:present name="selectedExecutionPeriod">
	<bean:message key="link.inquiries.define.response.period.information" bundle="INQUIRIES_RESOURCES"/>
	<br/><br/>
	<html:form action="/defineResponsePeriods">
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="define"/>
		<html:hidden property="executionPeriodID"/>
		<table>
			<tr>
				<td>
					<bean:message key="link.inquiries.define.response.period.begin" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td>
					<html:text property="inquiryResponseBegin"/>
				</td>
				<td>
					<bean:message key="link.inquiries.define.response.period.format" bundle="INQUIRIES_RESOURCES"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="link.inquiries.define.response.period.end" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td>
					<html:text property="inquiryResponseEnd"/>
				</td>
				<td>
					<bean:message key="link.inquiries.define.response.period.format" bundle="INQUIRIES_RESOURCES"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:submit styleClass="inputbutton">
						<bean:message key="label.define" bundle="INQUIRIES_RESOURCES"/>
					</html:submit>
				</td>
			</tr>
   </html:submit></p>
			
		</table>
	</html:form>
</logic:present>