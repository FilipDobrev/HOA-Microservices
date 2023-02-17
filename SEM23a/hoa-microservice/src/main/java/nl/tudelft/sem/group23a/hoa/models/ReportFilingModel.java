package nl.tudelft.sem.group23a.hoa.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;

/**
 * DDD Model for reporting a member for breaking the HOA's rules.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportFilingModel {
    private MemberId member;
    private String description;
}
