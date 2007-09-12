<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<bean:define id="forwardTo" value="showCandidates" />
<bean:define id="degreeOID" name="electionPeriodBean" property="degree.idInternal" />
<bean:define id="electionOID" name="electionPeriodBean" property="election.idInternal" />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.showCandidates" bundle="PEDAGOGICAL_COUNCIL" /></h2>
	
<p class="mtop1 mbottom1"><b><bean:message key="label.currentExecutionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
	<bean:write name="currentExecutionYear" property="year" /></p>
<%--
<p class="mtop1 mbottom1"><b><bean:message key="label.degree" bundle="PEDAGOGICAL_COUNCIL" />:</b>
	<bean:write name="electionPeriodBean" property="degree.name" /></p>		
--%>

<ul>
	<li>
		<p class="mbottom1"><html:link page="<%= "/electionsPeriodsManagement.do?method=selectDegreeType&degreeOID=" + degreeOID + "&forwardTo=showCandidacyPeriods" %>">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.back" /></html:link></p>
	</li>
</ul>

<logic:present name="electionPeriodBean" >
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.candidacyPeriod.resume" bundle="PEDAGOGICAL_COUNCIL"/></b></p>
		
	<fr:view name="electionPeriodBean" layout="tabular-nonNullValues" schema="electionPeriod.showResults.resume" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<bean:define id="electionID" name="electionPeriodBean" property="election.idInternal" />

<logic:present name="candidatesWithoutPhotos" >
	<logic:empty name="candidatesWithoutPhotos" >
		<p>
			<em><bean:message key="elections.showCandidates.noCandidates" bundle="PEDAGOGICAL_COUNCIL"/></em>
		</p>
	</logic:empty>
	
	<logic:notEmpty name="candidatesWithoutPhotos" >
		<p class="mtop1 mbottom05">
			<b><bean:message key="label.elections.candidacyPeriod.results" bundle="PEDAGOGICAL_COUNCIL"/></b></p>
			
		<html:link page="<%= "/electionsPeriodsManagement.do?method=showCandidates&selectedCandidacyPeriod=" +  electionID + "&showPhotos=true" %>">
			<bean:message bundle="PEDAGOGICAL_COUNCIL" key="link.elections.candidacyPeriod.results.showPhotos" />
		</html:link>
			
		<fr:view name="candidatesWithoutPhotos" layout="tabular-sortable" schema="yearDelegateElection.candidates" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight tdcenter mtop05"/>
				<fr:property name="columnClasses" value="width80px,width200px aleft"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="number,person.name"/>
	           	<fr:property name="sortUrl" value="<%= String.format("/electionsPeriodsManagement.do?method=showCandidates&amp;selectedCandidacyPeriod=" +electionOID ) %>"/>
	           	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "number" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

<logic:present name="candidatesWithPhotos" >	
	<logic:empty name="candidatesWithPhotos" >
		<p class="mtop2 mbottom2">
		<span class="error0"><b><bean:message key="elections.showCandidates.noCandidates" bundle="PEDAGOGICAL_COUNCIL"/></b></span></p>
	</logic:empty>
	
	<logic:notEmpty name="candidatesWithPhotos" >
		<p class="mtop1 mbottom05">
			<b><bean:message key="label.elections.candidacyPeriod.results" bundle="PEDAGOGICAL_COUNCIL"/></b></p>

		<html:link page="<%= "/electionsPeriodsManagement.do?method=showCandidates&selectedCandidacyPeriod=" +  electionID %>">
			<bean:message bundle="PEDAGOGICAL_COUNCIL" key="link.elections.candidacyPeriod.results.hidePhotos" />
		</html:link>

		<table class="tstyle2 mtop15 tdleft">
			<logic:iterate id="candidate" name="candidatesWithPhotos">	
			<tr>			
				<td class="personInfo_photo">
		          	<logic:notEqual name="candidate" property="person.photoPubliclyAvailable" value="true">
		          		<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>
						<div><img src="<%= request.getContextPath() %>/images/photo_placer01_<%= language == null ? "pt" : String.valueOf(language) %>.gif"/></div>
		          	</logic:notEqual>
		
		          	<logic:equal name="candidate" property="person.photoPubliclyAvailable" value="true">
		      			<bean:define id="homepageID" name="candidate" property="person.homepage.idInternal"/>
		      			<div><img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>"/></div>
		   			</logic:equal>
				</td>			
							
				<td> <bean:write name="candidate" property="person.name" /> (<bean:write name="candidate" property="number" />)</td>
			</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>
</logic:present>

