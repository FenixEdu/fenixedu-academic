<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="siteView"> 
<bean:define id="infoSiteTeachingCareers" name="siteView" property="component"/>
<br/>
<h3><bean:message key="message.teachingCareer" /></h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teachingCareer.management" /></p>
<bean:message key="message.teachingCareer.managementInsertExplanation" />
<bean:message key="message.teachingCareer.managementCleanExplanation" />
<bean:message key="message.teachingCareer.managementInsertCareerExplanation" />
<bean:message key="message.teachingCareer.managementSaveExplanation" />
<bean:message key="message.teachingCareer.managementSeeExplanation" />
<table border="1">
<logic:iterate id="infoTeachingCareer" name="infoSiteTeachingCareers" property="infoCareers">
<tr>
	<td>
		<bean:write name="infoTeachingCareer" property="begin-Year" />-
		<bean:write name="infoTeachingCareer" property="end-Year" />
	</td>
	<td>
		<bean:write name="infoTeachingCareer" property="category.longName" />
	</td>
	<td>
		<bean:write name="infoTeachingCareer" property="courseOrPosition" />
	</td>
		<td>
		<div class="gen-button">
			<html:link page="/teachingCareer.do?method=prepareEdit&amp;page=0" 
					   paramId="careerId" 
					   paramName="infoTeachingCareer" 
					   paramProperty="idInternal">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td>
		<div class="gen-button">
			<html:link page="/teachingCareer.do?method=delete&amp;page=0" 
					   paramId="careerId" 
					   paramName="infoTeachingCareer" 
					   paramProperty="idInternal">
				<bean:message key="label.delete" />
			</html:link>
		</div>
	</td>
</tr>
</logic:iterate>
</table>
<div class="gen-button">
	<html:link page="/teachingCareer.do?method=prepareEdit&amp;page=0">
		<bean:message key="message.teachingCareer.insert" />
	</html:link>
</div>
<br />
<h3>
<table>
<tr align="center">	
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.continue"/>
	</html:submit>
	</td>
	<td>
		<html:reset styleClass="inputbutton">
		<bean:message key="button.seeInformation"/>
	</html:reset>
	</td>
</tr>
</table>
</h3>
</logic:present>
