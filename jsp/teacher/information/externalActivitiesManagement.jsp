<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>

<logic:present name="siteView"> 
<bean:define id="infoSiteExternalActivities" name="siteView" property="component"/>

<h3><bean:message key="message.externalActivities" /></h3>

<p class="infoop">
	<span class="emphasis-box">1</span>
	<bean:message key="message.externalActivities.management" />
</p>

	<bean:message key="message.externalActivities.managementInsertExplanation" />
	<bean:message key="message.externalActivities.managementCleanExplanation" />
	<bean:message key="message.externalActivities.managementInsertActExplanation" />
	<bean:message key="message.externalActivities.managementSaveExplanation" />
	
<table class="tstyle1">
<logic:iterate id="infoExternalActivity" name="infoSiteExternalActivities" property="infoExternalActivities">
<tr>
	<td class="listClasses" style="text-align:left;" >
		<span><bean:write name="infoExternalActivity" property="activity" /></span>
	</td>
	<td class="listClasses"> 
		<div class="gen-button">
		<%--					   paramId="externalActivityId" --%>
			<html:link page="/externalActivity.do?method=prepareEdit&amp;page=0" 
						paramId="idInternal"
					   paramName="infoExternalActivity" 
					   paramProperty="idInternal">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td class="listClasses">
		<div class="gen-button">
			<html:link page="/externalActivity.do?method=delete&amp;page=0" 
					   paramId="idInternal" 
					   paramName="infoExternalActivity" 
					   paramProperty="idInternal">
				<bean:message key="label.delete" />
			</html:link>
		</div>
	</td>
</tr>
</logic:iterate>
</table>


<div class="gen-button">
	<html:link page="/externalActivity.do?method=prepareEdit&amp;page=0" 
			   paramId="infoTeacher#idInternal" 
			   paramName="infoSiteExternalActivities" 
			   paramProperty="infoTeacher.idInternal" >
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
