package net.sourceforge.fenixedu.applicationTier.Servico;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.ICandidateView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class UserView1 implements IUserView {

    final private String utilizador;

	final private Collection roles;

    private ICandidateView candidateView;

    private String fullName;

    protected UserView1(final String utilizador, final Collection roles) {
		super();
		this.utilizador = utilizador;
		if (roles != null) {
			final Set rolesSet = new HashSet(roles);
			this.roles = Collections.unmodifiableSet(rolesSet);
		} else {
			this.roles = null;
		}
    }

    public boolean hasRoleType(final RoleType roleType) {
		if (roles == null) {
			return false;
		}
		return roles.contains(roleType);
    }

    public String getUtilizador() {
        return utilizador;
    }

    public Collection getRoles() {
        return roles;
    }

    public ICandidateView getCandidateView() {
        return candidateView;
    }

    public void setCandidateView(final ICandidateView view) {
        candidateView = view;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String string) {
        fullName = string;
    }

}
