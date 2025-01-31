package com.codev.domain.repository;

import com.codev.domain.enums.OrderBy;
import com.codev.domain.exceptions.challenges.JoinNotAcceptedException;
import com.codev.domain.exceptions.challenges.UnjoinNotAcceptedException;
import com.codev.domain.model.Challenge;
import com.codev.domain.model.Technology;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChallengeRepository {

    Set<Challenge> findAllChallengesWithPaging(
        Integer page, Integer size, UUID categoryId, OrderBy orderBy
    );

    boolean joinChallenge(UUID challengeId, UUID participantId) throws JoinNotAcceptedException;

    boolean unjoinChallenge(UUID challengeId, UUID participantId) throws UnjoinNotAcceptedException;


    List<Technology> findAllTechnologiesByChallengeId(UUID challengeId);

    List<Challenge> findAllChallengesByCategoryId(UUID categoryId, Integer page, Integer size);

    void addCategoryInChallenge(UUID challengeId, UUID categoryId) throws SQLException;

    void removeCategoryInChallenge(UUID challengeId) throws SQLException;

}