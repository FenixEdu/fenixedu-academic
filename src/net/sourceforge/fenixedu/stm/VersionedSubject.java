package net.sourceforge.fenixedu.stm;

public interface VersionedSubject {
    public jvstm.VBoxBody addNewVersion(String attr, int txNumber);
}
