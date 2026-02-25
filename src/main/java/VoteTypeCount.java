public class VoteTypeCount {
    private VoteType voteType;
    private long count;

    public VoteTypeCount(VoteType voteType, long count) {
        this.voteType = voteType;
        this.count = count;
    }

    @Override
    public String toString() {
        return voteType + "=" + count;
    }
}
