package net.sourceforge.fenixedu.domain.thesis;

public enum ThesisParticipationType {
    ORIENTATOR(true),
    COORIENTATOR(true),
    PRESIDENT(true),
    VOWEL(false);
    
    private boolean single;
    
    private ThesisParticipationType(boolean single) {
        this.single = single;
    }

    /**
     * @return <code>true</code> if only one person can have this
     *         participation type in a thesis
     */
    public boolean isSingle() {
        return this.single;
    }
}
