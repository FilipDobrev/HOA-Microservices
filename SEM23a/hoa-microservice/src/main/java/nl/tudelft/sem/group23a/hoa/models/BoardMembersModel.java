package nl.tudelft.sem.group23a.hoa.models;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DDD Model that lists usernames of board members.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardMembersModel {
    private List<String> boardMembers;
}
