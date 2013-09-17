<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<h2><bean:message key="title.exams.calendar"/></h2>

<logic:iterate id="infoRoomExamsMap" name="<%= PresentationConstants.INFO_EXAMS_MAP %>" scope="request">
	<bean:define id="infoRoomExamsMap" name="infoRoomExamsMap" toScope="request"/>
<h3 class="break-before"> Sala <bean:write name="infoRoomExamsMap" property="infoRoom.nome"/> </h3>

<app:generateNewExamsMap name="infoRoomExamsMap" user="sop" mapType="null"/> 

</logic:iterate>