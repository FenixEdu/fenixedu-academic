package net.sourceforge.fenixedu.persistenceTier;

public interface ISuportePersistente {

    public void iniciarTransaccao();

    public void confirmarTransaccao();

    public void cancelarTransaccao();
}
