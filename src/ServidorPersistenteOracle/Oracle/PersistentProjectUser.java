/*
 * Created on Jan 12, 2005
 *
 */
package ServidorPersistenteOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentProjectUser;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProjectUser implements IPersistentProjectUser {

    public Integer getUserCoordId(Integer userCode) throws ExcepcaoPersistencia {
        String query = " select ID_COORD from web_users where login = '" + userCode + "'";
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
