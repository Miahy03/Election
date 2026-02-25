import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // Q1 : nombre total de votes
    public long countAllVotes() {

        String sql = "SELECT COUNT(*) AS total FROM vote";

        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getLong("total");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur countAllVotes", e);
        }
    }

    // Q2 : nombre de votes par type
    public List<VoteTypeCount> countVotesByType() {

        String sql = """
            SELECT vote_type, COUNT(*) AS total
            FROM vote
            GROUP BY vote_type
            ORDER BY vote_type
        """;

        List<VoteTypeCount> list = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                VoteTypeCount v = new VoteTypeCount(
                        VoteType.valueOf(rs.getString("vote_type")),
                        rs.getLong("total")
                );
                list.add(v);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur countVotesByType", e);
        }

        return list;
    }

    // Q3 : votes valides par candidat
    public List<CandidateVoteCount> countValidVotesByCandidate() {

        String sql = """
            SELECT c.name AS name,
                   COUNT(CASE WHEN v.vote_type='VALID' THEN 1 END) AS total
            FROM candidate c
            LEFT JOIN vote v ON v.candidate_id = c.id
            GROUP BY c.name
            ORDER BY c.name
        """;

        List<CandidateVoteCount> list = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                CandidateVoteCount c = new CandidateVoteCount(
                        rs.getString("name"),
                        rs.getLong("total")
                );
                list.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur countValidVotesByCandidate", e);
        }

        return list;
    }

    // Q4 : synthèse globale
    public VoteSummary computeVoteSummary() {

        String sql = """
            SELECT
                COUNT(CASE WHEN vote_type='VALID' THEN 1 END) AS valid,
                COUNT(CASE WHEN vote_type='BLANK' THEN 1 END) AS blank,
                COUNT(CASE WHEN vote_type='NULL' THEN 1 END) AS null
            FROM vote
        """;

        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();

            VoteSummary s = new VoteSummary(
                    rs.getLong("valid"),
                    rs.getLong("blank"),
                    rs.getLong("null")
            );

            return s;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur computeVoteSummary", e);
        }
    }

    // Q5 : taux de participation
    public double computeTurnoutRate() {

        String sql = """
            SELECT COUNT(DISTINCT voter_id) * 100.0 /
                   (SELECT COUNT(*) FROM voter) AS rate
            FROM vote
        """;

        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getDouble("rate");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur computeTurnoutRate", e);
        }
    }

    // Q6 : gagnant
    public ElectionResult findWinner() {

        String sql = """
            SELECT c.name AS name,
                   COUNT(*) AS total
            FROM vote v
            JOIN candidate c ON v.candidate_id = c.id
            WHERE v.vote_type='VALID'
            GROUP BY c.name
            ORDER BY total DESC
            LIMIT 1
        """;

        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();

            ElectionResult r = new ElectionResult(
                    rs.getString("name"),
                    rs.getLong("total")
            );

            return r;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findWinner", e);
        }
    }
}
