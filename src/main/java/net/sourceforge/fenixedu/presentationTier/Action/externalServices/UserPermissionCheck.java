package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.ConnectionManager;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * This action is used by DSpace to check if a given user has access to the specified {@link File}.
 * 
 * @deprecated DSpace is in read-only mode as of Fenix 2.0.0, and its support will be permanently dropped in version 3.0.0.
 * 
 * @author João Carvalho (joao.pedro.carvalho@tecnico.ulisboa.pt)
 * 
 */
@Deprecated
@Mapping(module = "external", path = "/userPermissionCheck", scope = "request", parameter = "method")
public class UserPermissionCheck extends ExternalInterfaceDispatchAction {

    private static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";

    private static final String SUCCESS_CODE = "SUCCESS";

    public ActionForward canUserAccessFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!FenixConfigurationManager.getHostAccessControl().isAllowed(this, request)) {
            writeResponse(response, NOT_AUTHORIZED_CODE, "");
        } else {
            try {
                Person person = Person.readPersonByUsername(request.getParameter("username"));
                File file = readFileByExternalStorageIdentification(request.getParameter("dspaceBitstreamIdentification"));
                boolean result = file != null && file.isPersonAllowedToAccess(person);
                writeResponse(response, SUCCESS_CODE, Boolean.toString(result));
            } catch (Exception e) {
                writeResponse(response, UNEXPECTED_ERROR_CODE, "");
            }
        }
        return null;
    }

    /*
     * Unfortunately we need to read the file this way, as the Fenix Framework lacks indexing capabilities.
     */
    private static File readFileByExternalStorageIdentification(String externalStorageIdentification) throws SQLException {
        final Connection connection = ConnectionManager.getCurrentSQLConnection();
        try (PreparedStatement stmt =
                connection.prepareStatement("SELECT OID FROM GENERIC_FILE WHERE EXTERNAL_STORAGE_IDENTIFICATION = ?")) {
            stmt.setString(1, externalStorageIdentification);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return FenixFramework.getDomainObject(rs.getString(1));
            } else {
                return null;
            }
        }
    }
}