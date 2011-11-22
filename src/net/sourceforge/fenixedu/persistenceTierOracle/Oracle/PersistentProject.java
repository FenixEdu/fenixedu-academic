/*
 * Created on Jan 10, 2005
 *
 */

package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProject {

    public List<InfoProject> readByUserLogin(String userLogin, final BackendInstance instance) throws ExcepcaoPersistencia {
	List<InfoProject> projects = new ArrayList<InfoProject>();

	StringBuilder query = new StringBuilder();
	query.append("select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO, p.gestor from  V_PROJECTOS p , web_user_projs up where up.login ='");
	query.append(userLogin);
	query.append("' and p.projectCode = up.id_proj order by p.projectCode");
	// String query = " select p.projectCode, p.title, p.origem, p.tipo,
	// p.custo, p.coordenacao, p.UNID_EXPLORACAO "
	// + " from V_PROJECTOS p , web_user_projs up " + " where up.login = ?
	// and p.projectCode = up.id_proj order by p.projectCode";

	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
	    p.startTransaction();

	    PreparedStatement stmt = p.prepareStatement(query.toString());

	    // stmt.setString(1, userLogin);

	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
		InfoProject project = new InfoProject();
		project.setProjectCode(rs.getString("projectCode"));
		project.setTitle(rs.getString("title"));
		project.setOrigin(rs.getString("origem"));
		project.setType(new LabelValueBean(rs.getString("tipo"), ""));
		project.setCost(rs.getString("custo"));
		project.setCoordination(rs.getString("coordenacao"));
		project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
		project.setProjectManager(getPerson(rs.getString("gestor")));
		projects.add(project);
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return projects;
    }

    private Person getPerson(String username) {
	if (!StringUtils.isEmpty(username)) {
	    User user = User.readUserByUserUId(username.trim());
	    return user != null ? user.getPerson() : null;
	}
	return null;
    }

    public List<InfoProject> readByProjectsCodes(List<String> projectCodes, final BackendInstance instance) throws ExcepcaoPersistencia {
	List<InfoProject> projects = new ArrayList<InfoProject>();
	if (projectCodes != null && projectCodes.size() != 0) {
	    StringBuilder stringBuffer = new StringBuilder();
	    stringBuffer
		    .append("select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO, p.gestor from  V_PROJECTOS p where p.projectCode IN (");
	    for (int i = 0; i < projectCodes.size(); i++) {
		if (i != 0)
		    stringBuffer.append(", ");
		stringBuffer.append("'");
		stringBuffer.append(projectCodes.get(i));
		stringBuffer.append("'");
	    }
	    stringBuffer.append(") order by p.projectCode");
	    String query = stringBuffer.toString();

	    try {
		PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
		p.startTransaction();

		PreparedStatement stmt = p.prepareStatement(query);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
		    InfoProject project = new InfoProject();
		    project.setProjectCode(rs.getString("projectCode"));
		    project.setTitle(rs.getString("title"));
		    project.setOrigin(rs.getString("origem"));
		    project.setType(new LabelValueBean(rs.getString("tipo"), ""));
		    project.setCost(rs.getString("custo"));
		    project.setCoordination(rs.getString("coordenacao"));
		    project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
		    project.setProjectManager(getPerson(rs.getString("gestor")));
		    projects.add(project);
		}
		rs.close();
		p.commitTransaction();
	    } catch (SQLException e) {
		throw new ExcepcaoPersistencia();
	    }
	}
	return projects;
    }

    public List<InfoProject> readByCoordinatorAndNotProjectsCodes(Integer coordinatorId, List projectCodes, final BackendInstance instance)
	    throws ExcepcaoPersistencia {
	List<InfoProject> projects = new ArrayList<InfoProject>();
	StringBuilder stringBuffer = new StringBuilder();
	stringBuffer
		.append("select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO, p.gestor from  V_PROJECTOS p , web_user_projs up where up.login = '");
	stringBuffer.append(coordinatorId);
	stringBuffer.append("' and p.projectCode = up.id_proj");
	if (projectCodes != null && projectCodes.size() != 0) {
	    stringBuffer.append(" and p.projectCode NOT IN (");
	    for (int i = 0; i < projectCodes.size(); i++) {
		if (i != 0)
		    stringBuffer.append(", ");
		stringBuffer.append("'");
		stringBuffer.append(projectCodes.get(i));
		stringBuffer.append("'");
	    }
	    stringBuffer.append(")");
	}
	stringBuffer.append(" order by p.projectCode");
	String query = stringBuffer.toString();

	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
	    p.startTransaction();

	    PreparedStatement stmt = p.prepareStatement(query);

	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
		InfoProject project = new InfoProject();
		project.setProjectCode(rs.getString("projectCode"));
		project.setTitle(rs.getString("title"));
		project.setOrigin(rs.getString("origem"));
		project.setType(new LabelValueBean(rs.getString("tipo"), ""));
		project.setCost(rs.getString("custo"));
		project.setCoordination(rs.getString("coordenacao"));
		project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
		project.setProjectManager(getPerson(rs.getString("gestor")));
		projects.add(project);
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return projects;
    }

    public InfoProject readProject(String projectCode, final BackendInstance instance) throws ExcepcaoPersistencia {
	String query = "select title, c.nome, tp.descricao, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO, p.gestor from V_Projectos p, V_COORD c , V_TIPOS_PROJECTOS tp  where p.idCoord = c.idCoord and tp.cod = p.tipo and p.projectCode ='"
		+ projectCode + "'";
	InfoProject project = new InfoProject();
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
	    p.startTransaction();
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery();

	    if (rs.next()) {
		project.setProjectCode(projectCode.toString());
		project.setTitle(rs.getString(1));
		project.setCoordinatorName(rs.getString(2));
		project.setOrigin(rs.getString("origem"));
		project.setType(new LabelValueBean(rs.getString("tipo"), rs.getString("descricao")));
		project.setCost(rs.getString("custo"));
		project.setCoordination(rs.getString("coordenacao"));
		project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
		project.setProjectManager(getPerson(rs.getString("gestor")));
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return project;
    }

    public boolean isUserProject(Integer userCode, String projectCode, final BackendInstance instance) throws ExcepcaoPersistencia {
	boolean result = false;
	String query = " select count(*) from web_user_projs up where up.login='" + userCode + "' and up.id_proj='" + projectCode + "'";

	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
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

    public int countUserProject(Integer userCode, final BackendInstance instance) throws ExcepcaoPersistencia {
	int result = 0;
	StringBuilder stringBuffer = new StringBuilder();
	stringBuffer.append("select count(*) from web_user_projs up where up.login='");
	stringBuffer.append(userCode);
	stringBuffer.append("'");
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
	    p.startTransaction();

	    PreparedStatement stmt = p.prepareStatement(stringBuffer.toString());
	    ResultSet rs = stmt.executeQuery();
	    if (rs.next())
		result = rs.getInt(1);
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return result;
    }

}
