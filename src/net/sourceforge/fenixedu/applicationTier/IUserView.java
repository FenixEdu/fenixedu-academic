package net.sourceforge.fenixedu.applicationTier;

import java.util.Collection;

import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author jorge
 */
public interface IUserView {
    /* Acrecentado por Fernanda Quitério & Tania Pousão */
    String getUtilizador();

    public String getFullName();

    Collection getRoles();

    ICandidateView getCandidateView();

    boolean hasRoleType(RoleType roleType);
}