
package codereading.repository;

import codereading.domain.QuestionSeries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSeriesRepository extends JpaRepository<QuestionSeries, Long> {
    
}
