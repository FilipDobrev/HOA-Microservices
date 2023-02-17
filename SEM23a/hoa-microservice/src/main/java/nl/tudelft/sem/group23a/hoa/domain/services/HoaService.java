package nl.tudelft.sem.group23a.hoa.domain.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Address;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HoaName;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HomeOwnersAssociation;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Member;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;
import nl.tudelft.sem.group23a.hoa.domain.hoa.SpecificAddress;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import nl.tudelft.sem.group23a.hoa.domain.repositories.MemberRepository;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HoaService {

    private final transient HoaRepository hoaRepository;
    private final transient MemberRepository memberRepository;
    private final transient HoaMembershipService hoaMembershipService;

    public enum HoaJoinOutcome {
        HOA_DOES_NOT_EXIST(HttpStatus.NOT_FOUND),
        ALREADY_IN_HOA(HttpStatus.FORBIDDEN),
        ACCEPTED_TO_HOA(HttpStatus.ACCEPTED),
        WRONG_LOCALITY_FOR_HOA(HttpStatus.FORBIDDEN);

        public final HttpStatus status;

        HoaJoinOutcome(HttpStatus status) {
            this.status = status;
        }
    }

    /**
     * Creates a new HOA.
     *
     * @param details the new HOA's details.
     * @return The new HOA's id.
     */
    public long createHoa(HoaCreationModel details) {
        Address address = new Address(details.country, details.city);
        HoaName hoaName = new HoaName(details.name);
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .hoaName(hoaName)
                .address(address)
                .members(new HashSet<>())
                .boardMembers(new HashSet<>())
                .build();

        HomeOwnersAssociation savedHoa = this.hoaRepository.save(hoa);

        savedHoa.hoaWasCreated();
        // second save call to save the domain event with the correct id
        this.hoaRepository.save(savedHoa);

        return savedHoa.getId();
    }

    /**
     * Joins a HOA.
     *
     * @param hoaId the HOA to join.
     * @param joinRequest the user's information when requesting to join the HOA.
     * @param userId the user that wants to join.
     * @return a HoaJoinOutcome, either acceptance or a reason for rejection
     */
    public HoaJoinOutcome joinHoa(Long hoaId, HoaJoinModel joinRequest, String userId) {
        Optional<HomeOwnersAssociation> hoa = this.hoaRepository.findById(hoaId);
        HoaJoinOutcome outcome = canJoin(hoaId, joinRequest, hoa);
        if (outcome != HoaJoinOutcome.ACCEPTED_TO_HOA) {
            return outcome;
        }

        if (hoaMembershipService.isMember(hoaId, userId)) {
            return HoaJoinOutcome.ALREADY_IN_HOA;
        }

        SpecificAddress address = HoaJoinModel.createFromRequest(joinRequest);
        Member member = Member.builder()
                .memberId(new MemberId(userId))
                .address(address)
                .yearsOnHoaBoard(0)
                .memberOfHoa(hoa.get())
                .joinTime(new Date())
                .build();

        hoa.get().addMember(member);
        this.hoaRepository.save(hoa.get());

        return HoaJoinOutcome.ACCEPTED_TO_HOA;
    }

    private HoaJoinOutcome canJoin(Long hoaId, HoaJoinModel joinRequest, Optional<HomeOwnersAssociation> hoa) {

        if (hoa.isEmpty()) {
            return HoaJoinOutcome.HOA_DOES_NOT_EXIST;
        }

        if (!hoa.get().getAddress().equals(new Address(joinRequest.getCountry(), joinRequest.getCity()))) {
            return HoaJoinOutcome.WRONG_LOCALITY_FOR_HOA;
        }
        return HoaJoinOutcome.ACCEPTED_TO_HOA;

    }

    /**
     * Leaves a HOA.
     *
     * @param hoaId the HOA to join.
     * @param userId the user that wants to join.
     * @return whether leaving the HOA was successful.
     */
    public boolean leaveHoa(Long hoaId, String userId) {
        if (this.hoaRepository.findById(hoaId).isEmpty()) {
            return false;
        }
        HomeOwnersAssociation hoa = this.hoaRepository.findById(hoaId).get();

        for (Member member : hoa.getMembers()) {
            if (member.memberId.equals(new MemberId(userId))) {
                hoa.removeMember(member);
                hoa.removeBoardMember(member);

                this.memberRepository.delete(member);

                this.hoaRepository.save(hoa);
                return true;
            }
        }

        return false;
    }

    public HomeOwnersAssociation getHoa(Long hoaId) {
        return hoaRepository.findById(hoaId).orElse(null);
    }

    /**
     * Get the ids of the members of the hoa.
     *
     * @param hoaId the id of the hoa
     * @return a list of the members
     */
    public List<String> memberIds(Long hoaId) {
        Optional<HomeOwnersAssociation> hoa =  hoaRepository.findById(hoaId);

        return hoa.map(homeOwnersAssociation -> homeOwnersAssociation
                .getMembers()
                .stream()
                .map(x -> x.memberId.getMemberIdString())
                .collect(Collectors.toList())).orElseGet(List::of);
    }
}
