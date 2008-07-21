<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.reports.by.degree.type" bundle="GEP_RESOURCES" /></h2>

<logic:present name="reportBean">
	<fr:edit name="reportBean" id="reportBean" type="net.sourceforge.fenixedu.presentationTier.Action.gep.ReportsByDegreeTypeDA$ReportBean" 
			schema="select.degree.type">
		<fr:layout>
			<fr:property name="classes" value="thlight mbottom1"/>
		</fr:layout>
	</fr:edit>

	<br/>
	<br/>
	<logic:present name="reportBean" property="degreeType">
		<bean:define id="args" type="java.lang.String">degreeType=<bean:write name="reportBean" property="degreeType"/>&amp;executionYearID=<bean:write name="reportBean" property="executionYear.idInternal"/></bean:define>
		<bean:message key="label.available.reports" bundle="GEP_RESOURCES" />
		<bean:define id="urlEurAce" type="java.lang.String">/reportsByDegreeType.do?method=downloadEurAce&amp;<bean:write name="args" filter="false"/></bean:define>
		<bean:define id="urlEctsLabel" type="java.lang.String">/reportsByDegreeType.do?method=downloadEctsLabel&amp;<bean:write name="args" filter="false"/></bean:define>
		<bean:define id="urlStatusAndAproval" type="java.lang.String">/reportsByDegreeType.do?method=downloadStatusAndAproval&amp;<bean:write name="args" filter="false"/></bean:define>
		<table class="tstyle1 thleft thlight mtop025">
			<tr>
				<th>
					<bean:message key="label.report.eur.ace" bundle="GEP_RESOURCES" />
				</th>
				<td>
					<bean:define id="urlEurAceCsv" type="java.lang.String"><bean:write name="urlEurAce" filter="false"/>&amp;format=csv</bean:define>
					<html:link page="<%= urlEurAceCsv %>">
						<bean:message key="label.download.csv" bundle="GEP_RESOURCES" />
					</html:link>
				</td>
				<td>
					<bean:define id="urlEurAceXls" type="java.lang.String"><bean:write name="urlEurAce" filter="false"/>&amp;format=xls</bean:define>
					<html:link page="<%= urlEurAceXls %>">
						<bean:message key="label.download.xls" bundle="GEP_RESOURCES" />
					</html:link>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.report.ects.label" bundle="GEP_RESOURCES" />
				</th>
				<td>
					<bean:define id="urlEctsLabelCsv" type="java.lang.String"><bean:write name="urlEctsLabel" filter="false"/>&amp;format=csv</bean:define>
					<html:link page="<%= urlEctsLabelCsv %>">
						<bean:message key="label.download.csv" bundle="GEP_RESOURCES" />
					</html:link>
				</td>
				<td>
					<bean:define id="urlEctsLabelXls" type="java.lang.String"><bean:write name="urlEctsLabel" filter="false"/>&amp;format=xls</bean:define>
					<html:link page="<%= urlEctsLabelXls %>">
						<bean:message key="label.download.xls" bundle="GEP_RESOURCES" />
					</html:link>
				</td>
			</tr>
			<tr>
				<th>
					<bean:define id="year1" type="java.lang.String" name="reportBean" property="executionYearFourYearsBack.year"/>
					<bean:define id="year2" type="java.lang.String" name="reportBean" property="executionYear.year"/>
					<bean:message key="label.report.status.and.aprovals" bundle="GEP_RESOURCES" arg0="<%= year1 %>" arg1="<%= year2 %>"/>
				</th>
				<td>
					<bean:define id="urlStatusAndAprovalCsv" type="java.lang.String"><bean:write name="urlStatusAndAproval" filter="false"/>&amp;format=csv</bean:define>
					<html:link page="<%= urlStatusAndAprovalCsv %>">
						<bean:message key="label.download.csv" bundle="GEP_RESOURCES" />
					</html:link>
				</td>
				<td>
					<bean:define id="urlStatusAndAprovalXls" type="java.lang.String"><bean:write name="urlStatusAndAproval" filter="false"/>&amp;format=xls</bean:define>
					<html:link page="<%= urlStatusAndAprovalXls %>">
						<bean:message key="label.download.xls" bundle="GEP_RESOURCES" />
					</html:link>
				</td>
			</tr>
		</table>
	</logic:present>
</logic:present>