<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="siteActionName" value="/departmentSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="departmentID" toScope="request"/>
<bean:define id="siteContextParamValue" name="UserView" property="person.employee.currentDepartmentWorkingPlace.idInternal" toScope="request"/>

