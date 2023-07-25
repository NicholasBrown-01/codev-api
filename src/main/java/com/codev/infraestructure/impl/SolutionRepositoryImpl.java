package com.codev.infraestructure.impl;

import com.codev.domain.dto.view.LikeDTOView;
import com.codev.domain.dto.view.SolutionDTOView;
import com.codev.domain.exceptions.solutions.LikeNotAcceptedException;
import com.codev.domain.exceptions.solutions.SolutionNotDeletedException;
import com.codev.domain.model.Solution;
import com.codev.domain.model.User;
import com.codev.domain.repository.SolutionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class SolutionRepositoryImpl implements SolutionRepository {

    private final EntityManager entityManager;

    public SolutionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Inject
    DataSource dataSource;

    @Override
    public List<SolutionDTOView> findAllSolutionsByChallengeId(
            Long challengeId, Long userId, Integer page, Integer size
    ) {

        if (page < 0) {
            throw new IllegalArgumentException("Page must be a positive integer.");
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SolutionDTOView> criteriaQuery = criteriaBuilder.createQuery(SolutionDTOView.class);

        Root<Solution> solutionRoot = criteriaQuery.from(Solution.class);

        Join<Solution, User> authorJoin = solutionRoot.join("author", JoinType.LEFT);
        Join<Solution, User> likeJoin = solutionRoot.join("participants", JoinType.LEFT);

        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<User> likeUserRoot = subquery.from(User.class);
        subquery.select(likeUserRoot.get("id"));
        subquery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(likeUserRoot.get("id"), userId),
                        criteriaBuilder.notEqual(solutionRoot.get("author").get("id"), likeUserRoot.get("id"))
                )
        );

        criteriaQuery.multiselect(
                solutionRoot.get("challenge").get("id"),
                authorJoin,
                solutionRoot.get("repositoryUrl"),
                solutionRoot.get("deployUrl"),
                criteriaBuilder.countDistinct(likeJoin.get("id")),
                criteriaBuilder.exists(subquery)
        );

        criteriaQuery.where(criteriaBuilder.equal(solutionRoot.get("challenge").get("id"), challengeId));

        criteriaQuery.groupBy(
                solutionRoot.get("id"),
                authorJoin
        );

        int firstResult = page * size;

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();

    }

    @Override
    public LikeDTOView addLike(Long solutionId, Long userId) throws LikeNotAcceptedException {
        boolean isLikedInSolution = isLikedInSolution(solutionId, userId);

        if (!isLikedInSolution)
            return addLikeInSolution(solutionId, userId);
        else
            throw new LikeNotAcceptedException();
    }

    @Override
    public LikeDTOView removeLike(Long solutionId, Long userId) throws LikeNotAcceptedException {
        boolean isLikedInSolution = isLikedInSolution(solutionId, userId);

        if (isLikedInSolution)
            return removeLikeBySolutionIdAndparticipantId(solutionId, userId);
        else
            throw new LikeNotAcceptedException();
    }

    private LikeDTOView addLikeInSolution(Long solutionId, Long userId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO tb_like (participant_id, solution_id) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, userId);
                statement.setLong(2, solutionId);
                statement.executeUpdate();

                return new LikeDTOView(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private LikeDTOView removeLikeBySolutionIdAndparticipantId(Long solutionId, Long participantId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM tb_like WHERE participant_id = ? AND solution_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, participantId);
                statement.setLong(2, solutionId);
                statement.executeUpdate();

                return new LikeDTOView(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isLikedInSolution(Long solutionId, Long participantId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) AS count" +
                    " FROM tb_like" +
                    " WHERE participant_id = ? AND solution_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, participantId);
                statement.setLong(2, solutionId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        return count > 0;
                    }
                    throw new RuntimeException("result.next() == false. There are no more rows");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean deleteSolution(Long solutionId, Long authorId) throws SolutionNotDeletedException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM tb_solution WHERE author_id = ? AND id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, authorId);
                statement.setLong(2, solutionId);
                statement.executeUpdate();

                return true;
            }

        } catch (SQLException e) {
            throw new SolutionNotDeletedException(e.getMessage());
        }
    }

    public void removeLikeBySolutionId(Long solutionId) throws SolutionNotDeletedException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM tb_like WHERE solution_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, solutionId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("LANÇOU EM: removeLikeBySolutionId");
            throw new SolutionNotDeletedException(e.getMessage());
        }
    }

}
