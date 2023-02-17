package nl.tudelft.sem.group23a.activity.integrations.hoa.models;

import java.util.List;
import lombok.Data;

/**
 * Model that lists usernames of board members.
 */
@Data
public class BoardMembersModel {
    private final List<String> boardMembers;
}
