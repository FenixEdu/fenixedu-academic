/*
 * Created on Jan 12, 2005
 *
 */
package ServidorPersistenteOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Dominio.projectsManagement.IRubric;
import Dominio.projectsManagement.Rubric;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentProjectUser;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProjectUser implements IPersistentProjectUser {

    public IRubric readProjectCoordinator(Integer userCode) throws ExcepcaoPersistencia {
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("select NOME, ID_COORD from web_users where login = '");
        queryBuffer.append(userCode);
        queryBuffer.append("'");
        String query = queryBuffer.toString();
        IRubric result = new Rubric();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result.setCode(new Integer(rs.getInt("ID_COORD")).toString());
                result.setDescription(rs.getString("NOME"));
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

    public Integer getUserCoordId(Integer userCode) throws ExcepcaoPersistencia {
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("select ID_COORD from web_users where login = '");
        queryBuffer.append(userCode);
        queryBuffer.append("'");
        String query = queryBuffer.toString();
        Integer result = null;
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = new Integer(rs.getInt("ID_COORD"));
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

}
