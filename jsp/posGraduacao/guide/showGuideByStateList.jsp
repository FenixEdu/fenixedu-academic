<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>

<span class="error"><html:errors/></span>
<br />


<h2><bean:message key="label.masterDegree.administrativeOffice.year" /> : <bean:write name="year"/></h2>


<logic:present name="listOfGuides">

	<logic:iterate id="guideInfo" name="listOfGuides">
		<table>
			<tr>
				<td>
					<h2><bean:message key="label.masterDegree.administrativeOffice.situation" /></h2>
				</td>
				<td>
					<h3><bean:message name="guideInfo" property="situation" bundle="ENUMERATION_RESOURCES" /></h3>
				</td>
			</tr>
		</table>

		<br />
		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.masterDegree.administrativeOffice.guideNumber" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.masterDegree.administrativeOffice.name" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.total" />
				</th>
			</tr>

			<logic:iterate id="guide" name="guideInfo" property="guides">
				<tr>
					<td class="listClasses">
						<bean:define id="number" name="guide" property="number" />
						<html:link page="<%= "/guideListingByYear.do?method=chooseGuide&amp;page=0&amp;year="
								+ pageContext.findAttribute("year")
								+ "&amp;number="
								+ pageContext.findAttribute("number") %>">
							<bean:write name="guide" property="number" />
						</html:link>
					</td>
					<td class="listClasses">
						<bean:define id="personID" name="guide" property="infoPerson.idInternal" />
						<html:link page="<%= "/guideListingByYear.do?method=chooseGuideByPerson&page=0&personID="
								+ pageContext.findAttribute("personID") %>">
							<bean:write name="guide" property="infoPerson.nome" />
						</html:link>
					</td>
					<td class="listClasses">
						<bean:write name="guide" property="total" />
					</td>
				</tr>
			</logic:iterate>
			<tr>
				<td>
					
				</td>
				<td class="listClasses">
					<strong><bean:message key="label.total" /></strong>
				</td>
				<td class="listClasses">
					<strong><bean:write name="guideInfo" property="total" /></strong>
				</td>
			</tr>
	
	
		</table>
	</logic:iterate>
</logic:present>

<logic:notPresent name="listOfGuides">
	<bean:message key="label.masterDegree.administrativeOffice.noGuidesFound" />
</logic:notPresent>
