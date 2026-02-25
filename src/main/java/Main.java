public class Main {

    public static void main(String[] args) throws Exception {

        ElectionService service = new ElectionService();

        System.out.println("Total votes = " + service.countAllVotes());

        System.out.println(service.countVotesByType());

        System.out.println(service.countValidVotesByCandidate());

        System.out.println(service.computeVoteSummary());

        System.out.println("Turnout = " + service.computeTurnoutRate());

        System.out.println(service.findWinner());
    }
}