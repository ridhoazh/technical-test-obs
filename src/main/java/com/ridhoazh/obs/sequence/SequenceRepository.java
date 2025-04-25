package com.ridhoazh.obs.sequence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, String> {

    @Modifying
    @Query(value = "UPDATE Sequence s SET s.ordinal = ordinal + 1, s.lastUpdate = CURRENT_DATE WHERE name = :name")
    void setNext(@Param("name") String name);

    @Query("SELECT s.name from Sequence s where s.name = :name")
    List<String> findNamePrefix(@Param("name") String name);

    @Query(value = "SELECT s FROM Sequence s WHERE s.name = :name")
    Sequence getNext(@Param("name") String name);

}
