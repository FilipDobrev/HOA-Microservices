package nl.tudelft.sem.group23a.activity.models;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ProposalVotingRequestModel {
    private String choice;
    private String username;
    private Integer boardMembersCount;
}