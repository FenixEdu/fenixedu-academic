/*
 * Created on Jan 12, 2005
 *
 */
package ServidorPersistenteOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.projectsManagement.IRubric;
import Dominio.projectsManagement.Rubric;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentRubric;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentRubric implements IPersistentRubric {

    public List getRubricList(String rubricTableName) throws ExcepcaoPersistencia {
        List rubricList = new ArrayList();

        String query = "select COD, DESCRICAO from " + rubricTableName + " order by COD";

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String code = rs.getString(1);
                String description = rs.getString(2);
                IRubric rubric = new Rubric(code, description);
                rubricList.add(rubric);
            }

            rs.close();
            p.commitTransaction();

        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return rubricList;
    }

}
