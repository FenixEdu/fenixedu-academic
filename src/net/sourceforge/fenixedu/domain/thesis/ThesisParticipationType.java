package net.sourceforge.fenixedu.domain.thesis;

public enum ThesisParticipationType {
    STATE_CREATOR(true),
    STATE_SUBMITTER(true),
    STATE_PROPOSAL_APPROVER(true),
    STATE_CONFIRMER(true),
    STATE_EVALUATION_APPROVER(true),
    
    ORIENTATOR(true),
    COORIENTATOR(true),
    PRESIDENT(true),
    VOWEL(false);
    
    private boolean single;
    
    public String getName() {
    	return name();
    }
    
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
