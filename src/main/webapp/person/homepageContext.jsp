<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>



<bean:define id="siteActionName" value="/manageHomepage.do" toScope="request"/>
<bean:define id="siteContextParam" value="homepageID" toScope="request"/>
<bean:define id="siteContextParamValue" name="homepage" property="externalId" toScope="request"/>
