import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionService {

    private final DBConnection dbConnection = new DBConnection();

    public long countAllVotes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM vote";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getLong(1);
        }
    }

    public List<VoteTypeCount> countVotesByType() throws SQLException {
        String sql = "SELECT vote_type, COUNT(*) FROM vote GROUP BY vote_type";

        List<VoteTypeCount> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VoteType type = VoteType.valueOf(rs.getString(1));
                long count = rs.getLong(2);
                results.add(new VoteTypeCount(type, count));
            }
        }

        return results;
    }

    public List<CandidateVoteCount> countValidVotesByCandidate() throws SQLException {

        String sql = """
                SELECT c.name,
                       COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END)
                FROM candidate c
                LEFT JOIN vote v ON c.id = v.candidate_id
                GROUP BY c.name
                """;

        List<CandidateVoteCount> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new CandidateVoteCount(
                        rs.getString(1),
                        rs.getLong(2)
                ));
            }
        }

        return results;
    }

    public VoteSummary computeVoteSummary() throws SQLException {

        String sql = """
                SELECT
                COUNT(CASE WHEN vote_type = 'VALID' THEN 1 END),
                COUNT(CASE WHEN vote_type = 'BLANK' THEN 1 END),
                COUNT(CASE WHEN vote_type = 'NULL' THEN 1 END)
                FROM vote
                """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();

            return new VoteSummary(
                    rs.getLong(1),
                    rs.getLong(2),
                    rs.getLong(3)
            );
        }
    }

    public double computeTurnoutRate() throws SQLException {

        String sql = """
                SELECT COUNT(DISTINCT voter_id) * 100.0 /
                (SELECT COUNT(*) FROM voter)
                FROM vote
                """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getDouble(1);
        }
    }

    public ElectionResult findWinner() throws SQLException {

        String sql = """
                SELECT c.name, COUNT(*) AS valid_vote_count
                FROM vote v
                JOIN candidate c ON v.candidate_id = c.id
                WHERE v.vote_type = 'VALID'
                GROUP BY c.name
                ORDER BY valid_vote_count DESC
                LIMIT 1
                """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return new ElectionResult(
                    rs.getString(1),
                    rs.getLong(2)
            );
        }
    }
}