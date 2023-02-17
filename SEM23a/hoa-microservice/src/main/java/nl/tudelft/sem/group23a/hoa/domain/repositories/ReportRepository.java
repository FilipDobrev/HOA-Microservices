package nl.tudelft.sem.group23a.hoa.domain.repositories;

import java.util.List;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>  {

    @Override
    <R extends Report> R save(R report);

    <R extends Report> List<R> findAllByReportInHoaId(long id);
}
