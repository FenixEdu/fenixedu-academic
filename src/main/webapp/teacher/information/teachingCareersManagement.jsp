<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>

<logic:present name="siteView"> 
<bean:define id="infoSiteCareers" name="siteView" property="component"/>
<h3><bean:message key="message.teachingCareer" /></h3>

<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teachingCareer.management" /></p>
<bean:message key="message.teachingCareer.managementInsertExplanation" />
<bean:message key="message.teachingCareer.managementCleanExplanation" />
<bean:message key="message.teachingCareer.managementInsertCareerExplanation" />
<bean:message key="message.teachingCareer.managementSaveExplanation" />

<table class="tstyle4" width="100%">	
<logic:iterate id="infoTeachingCareer" name="infoSiteCareers" property="infoCareers">
<tr>
	<td class="listClasses">
		<bean:write name="infoTeachingCareer" property="beginYear" />-
		<bean:write name="infoTeachingCareer" property="endYear" />
	</td>
	<td  class="listClasses">
		&nbsp;
		<logic:notEmpty name="infoTeachingCareer" property="infoCategory" >
			<bean:write name="infoTeachingCareer" property="infoCategory.name" />
		</logic:notEmpty>
	</td>
	<td  class="listClasses">
		<bean:write name="infoTeachingCareer" property="courseOrPosition" />
	</td>
	<td  class="listClasses">
		<div class="gen-button">
			<html:link page="/teachingCareer.do?method=prepareEdit&amp;page=0" 
					   paramId="externalId" 
					   paramName="infoTeachingCareer" 
					   paramProperty="externalId">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td  class="listClasses">
		<div class="gen-button">
			<html:link page="/teachingCareer.do?method=delete&amp;page=0" 
					   paramId="externalId" 
					   paramName="infoTeachingCareer" 
					   paramProperty="externalId">
				<bean:message key="label.delete" />
			</html:link>
		</div>
	</td>
</tr>
</logic:iterate>
</table>

<div class="gen-button">
	<html:link page="<%= "/teachingCareer.do?method=prepareEdit&amp;page=0&amp;careerType=" + net.sourceforge.fenixedu.domain.CareerType.TEACHING.toString() %>" 
			   paramId="infoTeacher#externalId" 
			   paramName="infoSiteCareers" 
			   paramProperty="infoTeacher.externalId" >
		<bean:message key="message.teachingCareer.insert" />
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
