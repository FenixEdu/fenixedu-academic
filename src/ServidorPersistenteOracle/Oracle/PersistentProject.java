/*
 * Created on Jan 10, 2005
 *
 */

package ServidorPersistenteOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import Dominio.projectsManagement.IProject;
import Dominio.projectsManagement.Project;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentProject;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProject implements IPersistentProject {

    public List getAllProjectsByUserLogin(String userLogin) throws ExcepcaoPersistencia {
        List projects = new ArrayList();

        String query = " select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO "
                + " from  V_PROJECTOS p , web_user_projs up " + " where up.login = ? and p.projectCode = up.id_proj order by p.projectCode";

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);

            stmt.setString(1, userLogin);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int i = 1;
                IProject project = new Project();
                project.setProjectCode(rs.getString("projectCode"));
                project.setTitle(rs.getString("title"));
                project.setOrigin(rs.getString("origem"));
                project.setType(new LabelValueBean(rs.getString("tipo"), ""));
                project.setCost(rs.getString("custo"));
                project.setCoordination(rs.getString("coordenacao"));
                project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
                projects.add(project);
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return projects;
    }

    public IProject readProject(Integer projectCode) throws ExcepcaoPersistencia {
        String query = "select title, c.nome, tp.descricao, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO  from V_Projectos p, V_COORD c , V_TIPOS_PROJECTOS tp  where p.idCoord = c.idCoord and tp.cod = p.tipo and p.projectCode ="
                + projectCode;
        IProject project = new Project();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int i = 1;
                project.setProjectCode(projectCode.toString());
                project.setTitle(rs.getString(1));
                project.setCoordinatorName(rs.getString(2));
                project.setOrigin(rs.getString("origem"));
                project.setType(new LabelValueBean(rs.getString("tipo"), rs.getString("descricao")));
                project.setCost(rs.getString("custo"));
                project.setCoordination(rs.getString("coordenacao"));
                project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return project;
    }

    public boolean isUserProject(Integer userCode, Integer projectCode) throws ExcepcaoPersistencia {
        boolean result = false;
        String query = " select count(*) from web_user_projs up where up.login='" + userCode + "' and up.id_proj=" + projectCode;

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                if (rs.getInt(1) > 0)
                    result = true;

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

}
