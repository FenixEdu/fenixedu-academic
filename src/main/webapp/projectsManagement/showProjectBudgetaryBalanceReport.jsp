<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/projects-report" prefix="report"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="backendInstance" name="backendInstance" type="net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance"/>
<bean:define id="backendInstanceUrl" type="java.lang.String">&amp;backendInstance=<%= backendInstance.name() %></bean:define>


<logic:present name="infoReport">
	<bean:define id="code" value="" />
	<logic:present name="infoCostCenter" scope="request">
		<table class="viewHeader">
			<tr>
				<td>
				<h3><bean:write name="infoCostCenter" property="description" /></h3>
				</td>
			</tr>
		</table>
		<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
		<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
	</logic:present>
	<logic:present name="it" scope="request">
		<logic:equal name="it" value="true">
			<bean:define id="code" value="<%=code+"&amp;backendInstance=IST"%>" />
		</logic:equal>
	</logic:present>
	<logic:notEmpty name="infoReport" property="infoProject">
		<bean:define id="infoProject" name="infoReport" property="infoProject" />
		<table class="viewHeader" width="100%" cellspacing="0">
			<tr>
				<td class="infoop">
				<h2><bean:message key="title.projectBudgetaryBalanceReport" /></h2>
				</td>
				<logic:notEmpty name="infoReport" property="lines">
					<bean:define id="projectCode" name="infoProject" property="projectCode" />
					<td class="infoop" width="20"><html:link page="<%="/projectReport.do?method=exportToExcel&amp;reportType=projectBudgetaryBalanceReport&amp;projectCode="+projectCode+code+backendInstanceUrl%>">
						<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" align="right" />
					</html:link></td>
				</logic:notEmpty>
			</tr>
		</table>
		<br />
		<table><tr>
			<td style="vertical-align: top;"><table>
				<tr class="printHeader">
					<td rowspan="7"><img height="110" alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="university.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" /></td>
				</tr>
				<tr class="printHeader">
					<td colspan="2">
					<h2><bean:message key="title.projectBudgetaryBalanceReport" /></h2>
					</td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.acronym" />:</strong></td>
					<td><bean:write name="infoProject" property="title" /></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.projectNumber" />:</strong></td>
					<td><bean:write name="infoProject" property="projectIdentification" /></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.type" />:</strong></td>
					<bean:define id="type" name="infoProject" property="type" />
					<td><bean:write name="type" property="label" />&nbsp;-&nbsp;<bean:write name="type" property="value" /></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.coordinator" />:</strong></td>
					<td><bean:write name="infoProject" property="coordinatorName" /></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.date" />:</strong></td>
					<td><report:computeDate /></td>
				</tr>
			</table></td>
		<logic:notEmpty name="infoProject" property="projectManager">
			<bean:define id="projectManager" name="infoProject" property="projectManager"/>
			<td style="vertical-align: top; padding-left: 10px;"><table>
				<tr>
					<td><strong><bean:message key="label.projectManager" />:</strong></td>
					<td><bean:write name="projectManager" property="name" /></td>
				</tr>
		  		<tr>
    		  		<td><strong><bean:message key="label.workPhone" />:</strong></td>
		  			<td>
                        <fr:view name="projectManager" property="phones">
                            <fr:layout name="contact-list">
                                <fr:property name="classes" value="nobullet list6" />
                            </fr:layout>
                        </fr:view>
					</td>
				</tr>
				<tr>
                    <td><strong><bean:message key="label.email" />:</strong></td>
                    <td>
                        <fr:view name="projectManager" property="emailAddresses">
                            <fr:layout name="contact-list">
                                <fr:property name="classes" value="nobullet list6" />
                            </fr:layout>
                        </fr:view>
                    </td>
				</tr>
			</table></td>
		</logic:notEmpty>
		</tr></table>
		
		<br/>
		<bean:message key="message.budgetaryBalanceReport" />
		<br />
		<br />
		<logic:notEmpty name="infoReport" property="lines">
			<bean:define id="budgetaryBalanceLines" name="infoReport" property="lines" />
			<table class="report-table">
				<tr>
					<td class="report-hdr"><bean:message key="label.rubric" /></td>
					<td class="report-hdr"><bean:message key="label.name" /></td>
					<td class="report-hdr"><bean:message key="label.budgeted" /></td>
					<td class="report-hdr"><bean:message key="label.executed" /></td>
					<td class="report-hdr"><bean:message key="label.balance" /></td>
				</tr>
				<logic:iterate id="line" name="budgetaryBalanceLines" indexId="lineIndex">
					<tr>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="center"><bean:write name="line" property="rubric" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="left"><bean:write name="line" property="rubricDescription" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="budget" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="executed" /></td>
						<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>" align="right"><report:formatDoubleValue name="line" property="balance" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<br />
		<br />
		<br />
	</logic:notEmpty>
</logic:present>
