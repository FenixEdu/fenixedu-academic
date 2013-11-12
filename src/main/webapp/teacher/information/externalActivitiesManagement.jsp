<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>

<logic:present name="siteView"> 
<bean:define id="infoSiteExternalActivities" name="siteView" property="component"/>

<h3><bean:message key="message.externalActivities" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></h3>

<p class="infoop">
	<span class="emphasis-box">1</span>
	<bean:message key="message.externalActivities.management" />
</p>

	<bean:message key="message.externalActivities.managementInsertExplanation" />
	<bean:message key="message.externalActivities.managementCleanExplanation" />
	<bean:message key="message.externalActivities.managementInsertActExplanation" />
	<bean:message key="message.externalActivities.managementSaveExplanation" />
	
<table class="tstyle4" width="100%">
<logic:iterate id="infoExternalActivity" name="infoSiteExternalActivities" property="infoExternalActivities">
<tr>
	<td class="listClasses" style="text-align:left;" >
		<span><bean:write name="infoExternalActivity" property="activity" /></span>
	</td>
	<td class="listClasses"> 
		<div class="gen-button">
		<%--					   paramId="externalActivityId" --%>
			<html:link page="/externalActivity.do?method=prepareEdit&amp;page=0" 
						paramId="externalId"
					   paramName="infoExternalActivity" 
					   paramProperty="externalId">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td class="listClasses">
		<div class="gen-button">
			<html:link page="/externalActivity.do?method=delete&amp;page=0" 
					   paramId="externalId" 
					   paramName="infoExternalActivity" 
					   paramProperty="externalId">
				<bean:message key="label.delete" />
			</html:link>
		</div>
	</td>
</tr>
</logic:iterate>
</table>


<div class="gen-button">
	<html:link page="/externalActivity.do?method=prepareEdit&amp;page=0" 
			   paramId="infoTeacher#externalId" 
			   paramName="infoSiteExternalActivities" 
			   paramProperty="infoTeacher.externalId" >
		<bean:message key="message.externalActivities.insert" />
	</html:link>
</div>



<p class="mtop15">
	<html:form action="/voidAction">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
			<bean:message key="button.continue"/>
		</html:submit>
	</html:form>
</p>

</logic:present>
