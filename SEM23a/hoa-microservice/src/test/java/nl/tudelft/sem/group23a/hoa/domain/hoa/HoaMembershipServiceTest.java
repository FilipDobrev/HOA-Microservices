package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HoaMembershipServiceTest {

    private HoaRepository hoaRepository;
    private HoaMembershipService hoaMembershipService;

    private HomeOwnersAssociation mockHoa;

    @BeforeEach
    void setUp() {
        this.hoaRepository = mock(HoaRepository.class);

        this.hoaMembershipService = new HoaMembershipService(this.hoaRepository);
    }

    /**
     * gives a set of members used for assertions inside the tests.
     *
     * @return a Hashset of members inside the HOA
     */
    Set<Member> setupMembers() {
        this.mockHoa = mock(HomeOwnersAssociation.class);
        Set<Member> members = new HashSet<>();

        Member.MemberBuilder memberBuilder = Member.builder()
              .memberOfHoa(mockHoa)
              .joinTime(new Date())
              .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "23"))
              .yearsOnHoaBoard(0);

        Member member1 = memberBuilder.memberId(new MemberId("user1")).build();
        Member member2 = memberBuilder.memberId(new MemberId("user2")).build();

        members.add(member1);
        members.add(member2);
        return members;
    }

    @Test
    void allowedNoticeboard() {
        Set<Member> members = setupMembers();
        when(mockHoa.getMembers()).thenReturn(members);
        when(mockHoa.getId()).thenReturn(5L);

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(hoaRepository.findById(mockHoa.getId())).thenReturn(Optional.of(mockHoa));

        boolean outcome = hoaMembershipService.allowedToGetNoticeboard(mockHoa.getId(), "user1");

        assertThat(outcome).isEqualTo(true);
    }

    @Test
    void notAllowedNoticeboard() {
        Set<Member> members = setupMembers();
        when(mockHoa.getMembers()).thenReturn(members);
        when(mockHoa.getId()).thenReturn(5L);

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(mockHoa));
        when(hoaRepository.findById(mockHoa.getId())).thenReturn(Optional.of(mockHoa));

        boolean outcome = hoaMembershipService.allowedToGetNoticeboard(mockHoa.getId(), "user4");

        assertThat(outcome).isEqualTo(false);
    }

    @Test
    void isMemberIsInHoa() {
        long hoaId = 4L;
        String memberUsername = "user1";

        Member member = Member.builder()
              .joinTime(new Date())
              .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "2f"))
              .yearsOnHoaBoard(0)
              .memberId(new MemberId(memberUsername))
              .build();

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .id(hoaId)
              .hoaName(new HoaName("HoaName"))
              .boardMembers(new HashSet<>())
              .address(new Address("Czech Republic", "Prague"))
              .members(new HashSet<>(List.of(member)))
              .build();

        when(hoaRepository.findByIdAndMembersMemberId(any(Long.class), any(MemberId.class))).thenReturn(Optional.of(hoa));

        assertThat(hoaMembershipService.isMember(hoaId, memberUsername)).isTrue();
    }

    @Test
    void isMemberNotInHoa() {
        long hoaId = 4L;
        String memberUsername = "user1";

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .id(hoaId)
              .hoaName(new HoaName("HoaName"))
              .boardMembers(new HashSet<>())
              .address(new Address("Czech Republic", "Prague"))
              .members(new HashSet<>())
              .build();

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(hoa));

        assertThat(hoaMembershipService.isMember(hoaId, memberUsername)).isFalse();
    }

    @Test
    void isMemberNoHoa() {
        long hoaId = 4L;
        String memberUsername = "user";

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThat(hoaMembershipService.isMember(hoaId, memberUsername)).isFalse();
    }

    @Test
    void isBoardMemberForTenPlusYearsTestHoaDoesNotExist() {
        when(hoaRepository.findByIdAndBoardMembersMemberId(any(Long.class), any(MemberId.class)))
              .thenReturn(Optional.empty());

        assertThat(hoaMembershipService.isBoardMemberForTenPlusYears(1L, "username")).isFalse();
    }

    @Test
    void isBoardMemberForTenPlusYearsTestHoaDoesExist() {
        HomeOwnersAssociation hoa = mock(HomeOwnersAssociation.class);
        when(hoa.isBoardMemberForTenPlusYears(anyString())).thenReturn(true);

        when(hoaRepository.findByIdAndBoardMembersMemberId(any(Long.class), any(MemberId.class)))
              .thenReturn(Optional.of(hoa));

        assertThat(hoaMembershipService.isBoardMemberForTenPlusYears(1L, "username")).isTrue();
    }

    @Test
    void isMemberForThreePlusYearsTestHoaDoesNotExist() {
        when(hoaRepository.findByIdAndBoardMembersMemberId(any(Long.class), any(MemberId.class)))
              .thenReturn(Optional.empty());

        assertThat(hoaMembershipService.isMemberForThreePlusYears(1L, "username")).isFalse();
    }

    @Test
    void isMemberForThreePlusYearsTestHoaDoesExist() {
        HomeOwnersAssociation hoa = mock(HomeOwnersAssociation.class);
        when(hoa.isMemberForThreePlusYears(anyString())).thenReturn(true);
        when(hoa.hasMembersForThreePlusYears()).thenReturn(true);

        when(hoaRepository.findByIdAndMembersMemberId(any(Long.class), any(MemberId.class)))
              .thenReturn(Optional.of(hoa));

        assertThat(hoaMembershipService.isMemberForThreePlusYears(1L, "user")).isTrue();
    }

    @Test
    void isMemberForThreePlusYearsTestNoOneIs() {
        HomeOwnersAssociation hoa = mock(HomeOwnersAssociation.class);
        when(hoa.isMemberForThreePlusYears(anyString())).thenReturn(false);
        when(hoa.hasMembersForThreePlusYears()).thenReturn(false);

        when(hoaRepository.findByIdAndMembersMemberId(any(Long.class), any(MemberId.class)))
              .thenReturn(Optional.of(hoa));

        assertThat(hoaMembershipService.isMemberForThreePlusYears(1L, "user")).isTrue();
    }

    @Test
    void isBoardMemberInOtherHoaTestTrue() {
        HomeOwnersAssociation.HomeOwnersAssociationBuilder hoaBuilder = HomeOwnersAssociation.builder();
        List<HomeOwnersAssociation> hoaList = List.of(hoaBuilder.id(1L).build(), hoaBuilder.id(2L).build());

        when(hoaRepository.findByBoardMembersMemberId(any(MemberId.class)))
              .thenReturn(hoaList);

        assertThat(hoaMembershipService.isBoardMemberInOtherHoa(1L, "user")).isTrue();
    }

    @Test
    void isBoardMemberInOtherHoaTestBoardMemberInSameHoa() {
        HomeOwnersAssociation.HomeOwnersAssociationBuilder hoaBuilder = HomeOwnersAssociation.builder();
        List<HomeOwnersAssociation> hoaList = List.of(hoaBuilder.id(1L).build());

        when(hoaRepository.findByBoardMembersMemberId(any(MemberId.class)))
              .thenReturn(hoaList);

        assertThat(hoaMembershipService.isBoardMemberInOtherHoa(1L, "user")).isFalse();
    }

    @Test
    void isBoardMemberInOtherHoaTestBoardMemberNowhere() {
        when(hoaRepository.findByBoardMembersMemberId(any(MemberId.class)))
              .thenReturn(List.of());

        assertThat(hoaMembershipService.isBoardMemberInOtherHoa(1L, "user")).isFalse();
    }
}
