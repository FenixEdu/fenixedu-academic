package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/degreesList", scope = "request", parameter = "method")
public class DegreesListDA extends ExternalInterfaceDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final JSONArray jsonDegreesArray = new JSONArray();

        for (Degree degree : Degree.readBolonhaDegrees()) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", degree.getDegreeTypeName());
            jsonObject.put("name", degree.getNameI18N().toString());
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String url =
                    scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort)
                            + request.getContextPath();
            jsonObject.put("url", url + "/" + degree.getSigla());
            jsonDegreesArray.add(jsonObject);
        }

        writeResponse(response, SUCCESS_CODE, jsonDegreesArray.toJSONString());

        return null;
    }
}
