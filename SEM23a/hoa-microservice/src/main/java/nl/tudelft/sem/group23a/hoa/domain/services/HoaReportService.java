package nl.tudelft.sem.group23a.hoa.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HomeOwnersAssociation;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Member;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Report;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import nl.tudelft.sem.group23a.hoa.domain.repositories.MemberRepository;
import nl.tudelft.sem.group23a.hoa.domain.repositories.ReportRepository;
import nl.tudelft.sem.group23a.hoa.models.ReportFilingModel;
import nl.tudelft.sem.group23a.hoa.models.ReportModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HoaReportService {

    private final transient HoaRepository hoaRepository;
    private final transient MemberRepository memberRepository;
    private final transient ReportRepository reportRepository;

    public enum ReportOutcome {
        HOA_DOES_NOT_EXIST(HttpStatus.NOT_FOUND),
        REPORTED_MEMBER_NOT_PART_OF_HOA(HttpStatus.NOT_FOUND),
        SUCCESS(HttpStatus.ACCEPTED),
        YOU_ARE_NOT_PART_OF_HOA(HttpStatus.FORBIDDEN);

        public final HttpStatus status;

        ReportOutcome(HttpStatus status) {
            this.status = status;
        }
    }

    /**
     * Retrieves a HOA's filed reports.
     *
     * @param hoaId the HOA for which to retrieve the reports.
     * @return a list of these reports.
     */
    public List<ReportModel> getReports(Long hoaId) {
        return this.reportRepository
                .findAllByReportInHoaId(hoaId)
                .stream()
                .map(r -> new ReportModel(r.getFilingMember().memberId,
                        r.getReportedMember().memberId,
                        r.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * Reports a member.
     *
     * @param hoaId the HOA to which the member belongs.
     * @param reportRequest the report itself, i.e., member and description.
     * @param userId the user filing the report.
     * @return a ReportOutcome, either acceptance or a reason for rejection
     */
    public ReportOutcome report(Long hoaId, ReportFilingModel reportRequest, String userId) {
        Optional<HomeOwnersAssociation> optionalHoa = this.hoaRepository.findById(hoaId);

        if (optionalHoa.isEmpty()) {
            return ReportOutcome.HOA_DOES_NOT_EXIST;
        }

        HomeOwnersAssociation hoa = optionalHoa.get();

        if (hoa.doesNotContainMember(new MemberId(userId))) {
            return ReportOutcome.YOU_ARE_NOT_PART_OF_HOA;
        }
        if (hoa.doesNotContainMember(reportRequest.getMember())) {
            return ReportOutcome.REPORTED_MEMBER_NOT_PART_OF_HOA;
        }

        Optional<Member> optionalMe = this.memberRepository
                .findByMemberIdAndMemberOfHoaId(new MemberId(userId), hoaId);

        if (optionalMe.isEmpty()) {
            return ReportOutcome.YOU_ARE_NOT_PART_OF_HOA;
        }

        Optional<Member> optionalMember = this.memberRepository
                .findByMemberIdAndMemberOfHoaId(reportRequest.getMember(), hoaId);

        if (optionalMember.isEmpty()) {
            return ReportOutcome.REPORTED_MEMBER_NOT_PART_OF_HOA;
        }

        Member member = optionalMember.get();
        Member me = optionalMe.get();

        Report r = Report.builder()
                .reportInHoa(hoa)
                .reportedMember(member)
                .filingMember(me)
                .description(reportRequest.getDescription())
                .build();

        hoa.addReport(r);

        this.hoaRepository.save(hoa);

        return ReportOutcome.SUCCESS;
    }

}
