package nl.tudelft.sem.group23a.hoa.models;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DDD Model for voting.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VotingRequestModel {
    private String choice;
}
