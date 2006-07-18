<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.apache.struts.Globals" %>
 <bean:define id="rooms" name="infoExam" property="associatedRooms"/>
 <html:form action="/distributeStudentsByRoom">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="distribute" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode"/>
	<logic:present name="<%= Globals.ERROR_KEY %>">
		<span class="error"><html:errors/></span><br/><br/>
	</logic:present>
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.distribute.information" /></td>
		</tr>
	</table>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.enroll" property="enroll" value="true"><bean:message key="label.distribute.enrolled"/> (<bean:write name="infoExam" property="enrolledStudents"/>)</html:radio><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.enroll" property="enroll" value="false"><bean:message key="label.distribute.attend"/> (<bean:write name="attendStudents"/>)</html:radio><br/><br/> 
	<bean:message key="label.distribution.information"/>
    <br />
    <br />    
	<table>
		<logic:iterate id="infoRoom" name="rooms" indexId="roomIndex" type="net.sourceforge.fenixedu.dataTransferObject.InfoRoom">
			<tr>
				<td>
					<b><%= roomIndex.intValue() + 1 %>.</b>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.rooms" property="rooms" value="<%= infoRoom.getIdInternal().toString() %>">
						<html:options collection="rooms" property="idInternal" labelProperty="nome" />
					</html:select>
				</td>
			</tr> 
		</logic:iterate>
	</table>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.ok"/>
	</html:submit>
</html:form>