<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<bean:define id="motivation" type="java.lang.String"  name="motivation"/>
<bean:define id="cases" type="java.util.List" name="cases"/>
<bean:define id="modalityName" type="java.lang.String" name="modalityName"/>
<logic:present name="motivation">
	<logic:present name="cases">
		<logic:present name="modalityName">
				<h2><bean:message key="label.candidacySubmitedTitle"/></h2>
				<table>
					<logic:present name="theme">
						<logic:notEmpty name="theme">
							<tr>
								<td width="0%">
									<b><bean:message key="label.seminaries.showCandidacy.Theme"/></b>
								</td>
								<td>
									<bean:write name="theme" property="name"/>
								</td>
							</tr>
						</logic:notEmpty>
					</logic:present>
					<tr>
						<td width="0%">
							<b><bean:message key="label.seminaries.showCandidacy.Modality"/></b>
						</td>
						<td>
							<bean:write name="modalityName"/>
						</td>
					</tr>
					<tr>
						<td>
							<b><bean:message key="label.seminaries.showCandidacy.Motivation"/></b>
						</td>
						<td>
							<bean:write name="motivation"/>
						</td>
					</tr>
					<logic:iterate indexId="index" name="cases" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy" id="caseStudy">
						<tr>
							<td>
								<b><bean:message key="label.seminaries.showCandidacy.Case"/> <%=index.intValue()+1 %></b>
							</td>
							<td>
								<bean:write name="caseStudy" property="code"/> - <bean:write name="caseStudy" property="name"/>
							</td>
						</tr>
					</logic:iterate>
				</table>
				<html:link page="/listAllSeminaries.do"><bean:message key="label.seminaries.showCandidacy.Back"/></html:link>
		</logic:present>
	</logic:present>
</logic:present>