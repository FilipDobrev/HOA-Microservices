package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import nl.tudelft.sem.group23a.hoa.domain.repositories.MemberRepository;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoaServiceTest {

    private HoaRepository hoaRepository;
    private MemberRepository memberRepository;
    private HoaMembershipService hoaMembershipService;

    private HoaService hoaService;

    /**
     * Sets up and wires the basic environment needed.
     */
    @BeforeEach
    void setUp() {
        this.hoaRepository = mock(HoaRepository.class);
        this.memberRepository = mock(MemberRepository.class);
        this.hoaMembershipService = mock(HoaMembershipService.class);

        this.hoaService = new HoaService(hoaRepository, memberRepository, hoaMembershipService);
    }

    @Test
    void createHoa() {
        HoaCreationModel hoaCreationModel = new HoaCreationModel("name", "eSwatini", "Mbabane");
        HomeOwnersAssociation h = HomeOwnersAssociation.builder().id(1L).build();
        HomeOwnersAssociation spied = spy(h);
        when(hoaRepository.save(any(HomeOwnersAssociation.class))).thenReturn(spied);

        long id = hoaService.createHoa(hoaCreationModel);

        assertThat(id).isEqualTo(1L);
        // There are 2 calls to save as the first one saves the entity and generates an id for it,
        // while the second one publishes an event with that id.
        verify(hoaRepository, times(2)).save(any());
        verify(spied, times(1)).hoaWasCreated();
    }

    @Test
    void joinHoa() {
        HoaJoinModel dto = new HoaJoinModel("Netherlands", "Delft", "2000", "Mekelweg", "1");
        HomeOwnersAssociation h = HomeOwnersAssociation.builder()
              .id(1L)
              .address(new Address("Netherlands", "Delft"))
              .members(new HashSet<>())
              .build();
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(h));
        when(hoaMembershipService.isMember(any(Long.class), any(String.class))).thenReturn(false);

        HoaService.HoaJoinOutcome outcome = hoaService.joinHoa(1L, dto, "user");

        assertThat(outcome).isEqualTo(HoaService.HoaJoinOutcome.ACCEPTED_TO_HOA);
    }

    @Test
    void joinHoaAlreadyPartOf() {
        HoaJoinModel joinDto = new HoaJoinModel("Netherlands", "Delft", "2000", "Mekelweg", "1a");
        Member.MemberBuilder mb = Member.builder()
              .address(new SpecificAddress("Netherlands", "Delft", "2000", "Mekelweg", "1a"))
              .joinTime(new Date());
        Member member1 = mb.memberId(new MemberId("user2"))
              .build();
        Member member2 = mb.memberId(new MemberId("user"))
              .build();
        Member member3 = mb.memberId(new MemberId("use"))
              .build();
        HomeOwnersAssociation h = HomeOwnersAssociation.builder()
              .id(1L)
              .address(new Address("Netherlands", "Delft"))
              .members(new HashSet<>(List.of(member1, member2, member3)))
              .build();
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(h));
        when(hoaMembershipService.isMember(any(Long.class), any(String.class))).thenReturn(true);

        HoaService.HoaJoinOutcome outcome = hoaService.joinHoa(1L, joinDto, "user");

        assertThat(outcome).isEqualTo(HoaService.HoaJoinOutcome.ALREADY_IN_HOA);
    }

    @Test
    void joinHoaWrongLocality() {
        HoaJoinModel joinDto = new HoaJoinModel("Netherlands", "Delft", "2000", "Mekelweg", "1");
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .id(1L)
              .address(new Address("Netherlands", "Rotterdam"))
              .build();
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(hoa));

        HoaService.HoaJoinOutcome outcome = hoaService.joinHoa(1L, joinDto, "user");

        assertThat(outcome).isEqualTo(HoaService.HoaJoinOutcome.WRONG_LOCALITY_FOR_HOA);
    }

    @Test
    void joinHoaDoesNotExist() {
        HoaJoinModel joinDto = new HoaJoinModel("Netherlands", "Delft", "2000", "Mekelweg", "1b");
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        HoaService.HoaJoinOutcome outcome = hoaService.joinHoa(1L, joinDto, "user");

        assertThat(outcome).isEqualTo(HoaService.HoaJoinOutcome.HOA_DOES_NOT_EXIST);
    }

    @Test
    void leaveHoaSuccessfully() {
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .hoaName(new HoaName("HoaName"))
              .boardMembers(new HashSet<>())
              .address(new Address("Czech Republic", "Prague"))
              .members(new HashSet<>())
              .build();

        Member.MemberBuilder memberBuilder = Member.builder()
              .memberOfHoa(hoa)
              .joinTime(new Date())
              .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "23bis"))
              .yearsOnHoaBoard(0);

        Member member1 = memberBuilder.memberId(new MemberId("user1")).build();
        Member member2 = memberBuilder.memberId(new MemberId("user")).build();
        Member member3 = memberBuilder.memberId(new MemberId("us")).build();

        hoa.addMember(member1);
        hoa.addMember(member2);
        hoa.addMember(member3);

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(hoa));

        boolean outcome = hoaService.leaveHoa(1L, "user1");

        assertThat(outcome).isEqualTo(true);
        verify(memberRepository, times(1)).delete(any(Member.class));
    }

    @Test
    void leaveHoaNotPartOf() {
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .hoaName(new HoaName("HoaName"))
              .boardMembers(new HashSet<>())
              .address(new Address("Czech Republic", "Prague"))
              .members(new HashSet<>())
              .build();

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(hoa));

        Member.MemberBuilder memberBuilder = Member.builder()
              .memberOfHoa(hoa)
              .joinTime(new Date())
              .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "23"))
              .yearsOnHoaBoard(0);

        Member member1 = memberBuilder.memberId(new MemberId("user1")).build();
        Member member2 = memberBuilder.memberId(new MemberId("user")).build();

        hoa.addMember(member1);
        hoa.addMember(member2);

        boolean outcome = hoaService.leaveHoa(1L, "us");

        assertThat(outcome).isEqualTo(false);
    }

    @Test
    void leaveHoaDoesNotExist() {
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        boolean outcome = hoaService.leaveHoa(1L, "MyUsername");

        assertThat(outcome).isEqualTo(false);
    }

    @Test
    void getHoaExists() {
        long hoaId = 5;

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .id(hoaId)
              .build();

        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(hoa));

        assertThat(hoaService.getHoa(hoaId)).isEqualTo(hoa);
    }

    @Test
    void getHoaNoHoa() {
        long hoaId = 5;
        long anotherHoaId = 4;

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .id(hoaId)
              .build();

        when(hoaRepository.findById(hoaId)).thenReturn(Optional.of(hoa));
        when(hoaRepository.findById(anotherHoaId)).thenReturn(Optional.empty());

        assertThat(hoaService.getHoa(anotherHoaId)).isNull();
    }

    @Test
    void hoaWithMembersMemberId() {
        Member m1 = Member
              .builder()
              .id(1)
              .address(new SpecificAddress("Bulgaria", "Varna", "9000", "20-ta", "9"))
              .memberId(new MemberId("2"))
              .yearsOnHoaBoard(2)
              .build();
        Member m2 = Member
              .builder()
              .id(2)
              .address(new SpecificAddress("Bulgaria", "Varna", "9000", "21-ta", "10"))
              .memberId(new MemberId("3"))
              .yearsOnHoaBoard(1)
              .build();

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
              .hoaName(new HoaName("HoaName"))
              .boardMembers(new HashSet<>())
              .address(new Address("Bulgaria", "Varna"))
              .members(Set.of(m1, m2))
              .build();
        when(hoaRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(hoa));

        assertThat(hoaService.memberIds(1L))
              .containsExactlyInAnyOrderElementsOf(List.of("2", "3"));
    }

    // Boundary test
    @Test
    public void emptyHoa() {
        when(hoaRepository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());

        assertThat(hoaService.memberIds(1L)).isEmpty();
    }
}