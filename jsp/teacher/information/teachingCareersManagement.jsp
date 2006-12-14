<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="siteView"> 
<bean:define id="infoSiteCareers" name="siteView" property="component"/>
<br/>
<h3><bean:message key="message.teachingCareer" /></h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.teachingCareer.management" /></p>
<bean:message key="message.teachingCareer.managementInsertExplanation" />
<bean:message key="message.teachingCareer.managementCleanExplanation" />
<bean:message key="message.teachingCareer.managementInsertCareerExplanation" />
<bean:message key="message.teachingCareer.managementSaveExplanation" />
<table border="0" style="margin-top:10px" cellspacing="1" cellpadding="5" width="100%">	
<logic:iterate id="infoTeachingCareer" name="infoSiteCareers" property="infoCareers">
<tr>
	<td class="listClasses">
		<bean:write name="infoTeachingCareer" property="beginYear" />-
		<bean:write name="infoTeachingCareer" property="endYear" />
	</td>
	<td  class="listClasses">
		&nbsp;
		<logic:notEmpty name="infoTeachingCareer" property="infoCategory" >
			<bean:write name="infoTeachingCareer" property="infoCategory.shortName" />
		</logic:notEmpty>
	</td>
	<td  class="listClasses">
		<bean:write name="infoTeachingCareer" property="courseOrPosition" />
	</td>
	<td  class="listClasses">
		<div class="gen-button">
			<html:link page="/teachingCareer.do?method=prepareEdit&amp;page=0" 
					   paramId="idInternal" 
					   paramName="infoTeachingCareer" 
					   paramProperty="idInternal">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td  class="listClasses">
		<div class="gen-button">
			<html:link page="/teachingCareer.do?method=delete&amp;page=0" 
					   paramId="idInternal" 
					   paramName="infoTeachingCareer" 
					   paramProperty="idInternal">
				<bean:message key="label.delete" />
			</html:link>
		</div>
	</td>
</tr>
</logic:iterate>
</table>
<br />
<div class="gen-button">
	<html:link page="<%= "/teachingCareer.do?method=prepareEdit&amp;page=0&amp;careerType=" + net.sourceforge.fenixedu.domain.CareerType.TEACHING.toString() %>" 
			   paramId="infoTeacher#idInternal" 
			   paramName="infoSiteCareers" 
			   paramProperty="infoTeacher.idInternal" >
		<bean:message key="message.teachingCareer.insert" />
	</html:link>
</div>
<br />

<table>
<tr align="center">	
	<td>
	<html:form action="/voidAction">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
			<bean:message key="button.continue"/>
		</html:submit>
	</html:form>
	</td>
</tr>
</table>

</logic:present>
