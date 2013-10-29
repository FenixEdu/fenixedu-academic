<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<html:xhtml/>

<h2><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></h2>

<bean:message name="notAvailableMessage" bundle="INQUIRIES_RESOURCES" arg0="<%= Unit.getInstitutionAcronym() %>"/>

<bean:message key="message.inquiries.publicResults.moreInfoLink" bundle="INQUIRIES_RESOURCES"/>

