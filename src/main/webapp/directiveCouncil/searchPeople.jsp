<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<html:xhtml/>

<logic:present role="role(DIRECTIVE_COUNCIL)">
	<li><html:link page="/searchPeople.do?method=downloadActiveStudentList"><bean:message key="link.list.students.with.active.registration" /></html:link></li>
</logic:present>
