package ServidorAplicacao;

import java.util.Collection;

import Util.RoleType;

/**
 * @author jorge
 */
public interface IUserView { 
	Collection getRoles();
	ICandidateView getCandidateView();
	boolean hasRoleType(RoleType roleType);
}