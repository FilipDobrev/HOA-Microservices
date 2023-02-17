package nl.tudelft.sem.group23a.hoa.domain.hoa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with hoa boards.
 */
@Service
@AllArgsConstructor
public class HoaBoardService {

    private final transient HoaRepository hoaRepository;

    /**
     * Sets the hoa board.
     *
     * @param hoaId the hoa id
     * @param boardMembers the usernames of the board members
     */
    public void setBoard(Long hoaId, List<String> boardMembers) {
        Optional<HomeOwnersAssociation> hoaResult = hoaRepository.findById(hoaId);
        if (hoaResult.isEmpty()) {
            return;
        }

        HomeOwnersAssociation hoa = hoaResult.get();
        hoa.clearBoardMembers();

        for (String member : boardMembers) {
            hoa.addBoardMember(member);
        }

        hoaRepository.save(hoa);
    }

    /**
     * Returns the hoa board.
     *
     * @param hoaId the hoa id
     * @return list of board members
     */
    public List<String> getBoard(Long hoaId) {
        Optional<HomeOwnersAssociation> hoaResult = hoaRepository.findById(hoaId);
        if (hoaResult.isEmpty()) {
            return List.of();
        }

        HomeOwnersAssociation hoa = hoaResult.get();
        return hoa.getBoardMembers()
                .stream()
                .map(m -> m.memberId.getMemberIdString())
                .collect(Collectors.toList());
    }
}
