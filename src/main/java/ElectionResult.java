
public class ElectionResult {

    private String candidateName;
    private long validVoteCount;

    public ElectionResult(String candidateName, long validVoteCount) {
        this.candidateName = candidateName;
        this.validVoteCount = validVoteCount;
    }

    @Override
    public String toString() {
        return "ElectionResult(candidateName=" + candidateName +
                ", validVoteCount=" + validVoteCount + ")";
    }
}
