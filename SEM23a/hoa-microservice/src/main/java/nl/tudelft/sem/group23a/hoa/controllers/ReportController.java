package nl.tudelft.sem.group23a.hoa.controllers;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaReportService;
import nl.tudelft.sem.group23a.hoa.models.ReportFilingModel;
import nl.tudelft.sem.group23a.hoa.models.ReportModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hoa/report")
public class ReportController {

    private final transient HoaMembershipService hoaMembershipService;
    private final transient HoaReportService hoaReportService;
    private final transient AuthManager authManager;

    /**
     * Endpoint to report a member of the HOA.
     *
     * @param hoaId The HOA to which the member belongs.
     * @param reportDto The report the user wants to file.
     * @return A ResponseEntity indicating whether filing the report was successful via status codes.
     */
    @PostMapping("{hoaId}")
    public ResponseEntity<?> report(@Valid @PathVariable Long hoaId,
                                    @Valid @RequestBody ReportFilingModel reportDto) {
        HoaReportService.ReportOutcome outcome = this.hoaReportService.report(hoaId, reportDto, authManager.getId());
        HttpStatus returnStatus = outcome.status;
        return ResponseEntity.status(returnStatus).build();
    }

    /**
     * Endpoint to retrieve the HOA's reports.
     *
     * @param hoaId The HOA of which to retrieve the reports.
     * @return A ResponseEntity indicating whether filing the report was successful
     *         via status codes, with the reports as its body.
     */
    @GetMapping("{hoaId}")
    public ResponseEntity<List<ReportModel>> getReports(@Valid @PathVariable Long hoaId) {
        if (!this.hoaMembershipService.isBoardMember(hoaId, this.authManager.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<ReportModel> reports = this.hoaReportService.getReports(hoaId);
        return ResponseEntity.ok(reports);
    }
}
