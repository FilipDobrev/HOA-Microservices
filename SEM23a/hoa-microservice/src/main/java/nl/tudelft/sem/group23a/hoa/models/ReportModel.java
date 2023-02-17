package nl.tudelft.sem.group23a.hoa.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;

/**
 * DDD Model for retrieving reports.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportModel {
    private MemberId filingMember;
    private MemberId reportedMember;
    private String description;
}
