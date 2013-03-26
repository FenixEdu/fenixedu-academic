<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="infoSiteQualifications">
<br/>
<h3><bean:message key="message.teacherInformation.qualifications" /></h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.qualification.management" /></p>
<bean:message key="message.qualification.managementInsertExplanation" />
<bean:message key="message.qualification.managementCleanExplanation" />
<bean:message key="message.qualification.managementInsertQualExplanation" />
<bean:message key="message.qualification.managementSaveExplanation" />
<table border="0" style="margin-top:10px" cellspacing="1" cellpadding="5" width="100%">	
<logic:iterate id="infoQualification" name="infoSiteQualifications" property="infoQualifications">
<tr>
	<td class="listClasses">
		<dt:format pattern="yyyy">
			<bean:write name="infoQualification" property="date.time" />
		</dt:format>
	</td>
	<td class="listClasses">
		<bean:write name="infoQualification" property="school" />
	</td>
	<td class="listClasses">
		<bean:write name="infoQualification" property="title" />
	</td>
	<td class="listClasses">
		<bean:write name="infoQualification" property="degree" />
	</td>
	<td class="listClasses">
		<bean:write name="infoQualification" property="mark" />
	</td>
	<td class="listClasses">
		<div class="gen-button">
			<html:link page="/qualificationForm.do?method=prepareEdit&amp;page=0" 
					   paramId="idInternal" 
					   paramName="infoQualification" 
					   paramProperty="idInternal">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td class="listClasses">
		<div class="gen-button">
			<html:link page="/qualificationForm.do?method=delete&amp;page=0" 
					   paramId="idInternal" 
					   paramName="infoQualification" 
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
	<html:link page="/qualificationForm.do?method=prepareEdit&amp;page=0" 
			   paramId="infoPerson#idInternal" 
			   paramName="infoSiteQualifications" 
			   paramProperty="infoPerson.idInternal" >
		<bean:message key="message.qualification.insert" />
	</html:link>
</div>
<br />
<h3>
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
</h3>
</logic:present>