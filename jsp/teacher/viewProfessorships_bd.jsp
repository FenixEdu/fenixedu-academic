<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><html:errors/></span>	

<tiles:insert definition="teacher-professor-ships" flush="true">
	<tiles:put name="title" value="label.professorships"/>
	<tiles:put name="link" value="/teacherAdministrationViewer.do?method=instructions"/>
</tiles:insert>