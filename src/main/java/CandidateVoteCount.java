public class CandidateVoteCount {

    private String candidateName;
    private long validVoteCount;

    public CandidateVoteCount(String candidateName, long validVoteCount) {
        this.candidateName = candidateName;
        this.validVoteCount = validVoteCount;
    }

    @Override
    public String toString() {
        return candidateName + "=" + validVoteCount;
    }
}
