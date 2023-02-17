package nl.tudelft.sem.group23a.hoa.domain.services;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.exceptions.UserIsNotEligibleForBoardException;
import nl.tudelft.sem.group23a.hoa.domain.exceptions.UserIsNotInBoardException;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HomeOwnersAssociation;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HoaMembershipService {

    private final transient HoaRepository hoaRepository;

    /**
     * Checks if the user is a board member in the given HOA.
     *
     * @param hoaId the HOA's id.
     * @param userId the user's id.
     * @return a boolean indicating whether the user is a board member in the HOA.
     */
    public boolean isBoardMember(Long hoaId, String userId) {
        Optional<HomeOwnersAssociation> optionalHoa = this.hoaRepository
                .findByIdAndBoardMembersMemberId(hoaId, new MemberId(userId));

        return optionalHoa.isPresent();
    }

    /**
     * Returns whether a user with specific username is a member of specific hoa.
     *
     * @param hoaId the id of the hoa
     * @param username the user's username
     * @return whether is member of it
     */
    public boolean isMember(Long hoaId, String username) {
        return this.hoaRepository.findByIdAndMembersMemberId(hoaId, new MemberId(username)).isPresent();
    }

    /**
     * Returns whether a user with specific username is a member of specific HOA for 3+ years.
     *
     * @param hoaId the id of the hoa
     * @param username the user's username
     * @return whether has been a member of it for that time
     */
    public boolean isMemberForThreePlusYears(Long hoaId, String username) {
        Optional<HomeOwnersAssociation> hoaResult = this.hoaRepository
                .findByIdAndMembersMemberId(hoaId, new MemberId(username));

        if (hoaResult.isEmpty()) {
            return false;
        }

        HomeOwnersAssociation hoa = hoaResult.get();
        if (!hoa.hasMembersForThreePlusYears()) {
            return true;
        }

        return hoa.isMemberForThreePlusYears(username);
    }

    /**
     * Confirms whether a user is a board member in a given HOA.
     *
     * @param hoaId the HOA's id.
     * @param userId the user's id.
     */
    public void confirmIsBoardMember(long hoaId, String userId) {
        if (!this.isBoardMember(hoaId, userId)) {
            throw new UserIsNotInBoardException();
        }
    }

    /**
     * Confirms whether a user is eligible to be a board member in a given HOA.
     *
     * @param hoaId the HOA's id.
     * @param username the user's username
     */
    public void confirmIsEligibleForBoard(long hoaId, String username) {
        if (!this.isMemberForThreePlusYears(hoaId, username)
              || this.isBoardMemberForTenPlusYears(hoaId, username)
              || this.isBoardMemberInOtherHoa(hoaId, username)) {
            throw new UserIsNotEligibleForBoardException();
        }
    }

    /**
     * Checks if the user is a board member in any hoa other than the one specified.
     *
     * @param hoaId The HOA where the member might be on the board regardless.
     * @param username The member for whom we are checking HOA board membership.
     * @return a boolean indicating whether the member is part of any other HOAs.
     */
    public boolean isBoardMemberInOtherHoa(long hoaId, String username) {
        List<HomeOwnersAssociation> hoaList = this.hoaRepository.findByBoardMembersMemberId(new MemberId(username));
        for (HomeOwnersAssociation hoa : hoaList) {
            if (hoa.getId() != hoaId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether a user with specific username was a board member of specific HOA for 10+ years.
     *
     * @param hoaId the id of the hoa
     * @param username the user's username
     * @return whether was no board member in that HOA for 10+ years
     */
    public boolean isBoardMemberForTenPlusYears(Long hoaId, String username) {
        Optional<HomeOwnersAssociation> hoaResult = hoaRepository.findByIdAndBoardMembersMemberId(hoaId,
                new MemberId(username));

        if (hoaResult.isEmpty()) {
            return false;
        }

        HomeOwnersAssociation hoa = hoaResult.get();
        return hoa.isBoardMemberForTenPlusYears(username);
    }

    /**
     * Checks if the given user is eligible to view the noticeboard of the given hoa.
     *
     * @param hoaId the id of the HOA whose notice board is requested
     * @param username the user that requested the noticeboard
     * @return boolean whether this user is allowed to view the noticeboard of this HOA
     */
    public boolean allowedToGetNoticeboard(long hoaId, String username) {
        Optional<HomeOwnersAssociation> hoaResult = this.hoaRepository.findById(hoaId);
        if (hoaResult.isEmpty()) {
            return false;
        }
        var hoa = hoaResult.get();
        return hoa.getMembers()
                .stream()
                .anyMatch(m -> m.memberId.getMemberIdString().equals(username));
    }
}
