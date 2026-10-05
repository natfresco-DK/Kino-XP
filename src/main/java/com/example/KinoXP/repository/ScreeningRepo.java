package com.example.KinoXP.repository;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDateTime;

@Repository
public interface ScreeningRepo extends JpaRepository<Screening,Long> {
    //JPQL query to check if a screening overlaps with existing screenings for the same screen
    @Query("""
        SELECT COUNT(s) > 0
        FROM Screening s
        WHERE s.screen = :screen
        AND s.startTime < :endTime
        AND s.endTime > :startTime
    """)
    boolean isOverlapping(
            @Param("screen") Screen screen,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<Screening> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime now);

    List<Screening> findByStartTimeGreaterThanEqualAndStartTimeLessThanOrderByStartTimeAsc(
            LocalDateTime start, LocalDateTime end);

    List<Screening> findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(
            Long movieId, LocalDateTime now);
}