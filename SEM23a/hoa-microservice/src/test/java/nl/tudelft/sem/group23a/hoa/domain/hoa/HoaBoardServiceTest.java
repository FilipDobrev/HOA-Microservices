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
import java.util.stream.Collectors;
import nl.tudelft.sem.group23a.hoa.domain.repositories.HoaRepository;
import org.junit.jupiter.api.Test;

public class HoaBoardServiceTest {

    @Test
    void setBoardClearsPreviousBoardMembers() {
        // arrange
        long hoaId = 4L;

        Member.MemberBuilder memberBuilder = Member.builder()
                .joinTime(new Date())
                .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "23"))
                .yearsOnHoaBoard(0);

        List<Member> members = List.of(
                memberBuilder.memberId(new MemberId("a")).build(),
                memberBuilder.memberId(new MemberId("b")).build()
        );

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .id(hoaId)
                .hoaName(new HoaName("HoaName"))
                .boardMembers(new HashSet<>(members))
                .address(new Address("Czech Republic", "Prague"))
                .members(new HashSet<>(members))
                .build();

        HomeOwnersAssociation spied = spy(hoa);

        for (Member member : members) {
            member.boardMemberOfHoa = spied;
        }

        HoaRepository hoaRepository = mock(HoaRepository.class);
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(spied));

        HoaBoardService hoaBoardService = new HoaBoardService(hoaRepository);

        // act
        hoaBoardService.setBoard(hoaId, List.of());

        // assert
        assertThat(hoaBoardService.getBoard(hoaId)).isEmpty();
        assertThat(members.get(0).getBoardMemberOfHoa()).isNull();
        assertThat(members.get(1).getBoardMemberOfHoa()).isNull();
        verify(hoaRepository, times(1)).save(spied);
        verify(spied, times(1)).clearBoardMembers();
        verify(spied, times(0)).addBoardMember(any());
    }

    @Test
    void setBoardNoHoa() {
        // arrange
        long hoaId = 4L;

        HoaRepository hoaRepository = mock(HoaRepository.class);
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        HoaBoardService hoaBoardService = new HoaBoardService(hoaRepository);

        // act
        hoaBoardService.setBoard(hoaId, List.of());

        // assert
        assertThat(hoaBoardService.getBoard(hoaId)).isEmpty();
        verify(hoaRepository, times(0)).save(any());
    }

    @Test
    void setBoardSetsBoardMembersCorrectly() {
        // arrange
        long hoaId = 4L;

        Member.MemberBuilder memberBuilder = Member.builder()
                .joinTime(new Date())
                .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "23"))
                .yearsOnHoaBoard(0);

        List<Member> members = List.of(
                memberBuilder.memberId(new MemberId("a")).build(),
                memberBuilder.memberId(new MemberId("b")).build()
        );

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .id(hoaId)
                .hoaName(new HoaName("HoaName"))
                .boardMembers(new HashSet<>(List.of(members.get(1))))
                .address(new Address("Czech Republic", "Prague"))
                .members(new HashSet<>(members))
                .build();

        HomeOwnersAssociation spied = spy(hoa);
        members.get(1).boardMemberOfHoa = spied;

        HoaRepository hoaRepository = mock(HoaRepository.class);
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(spied));

        HoaBoardService hoaBoardService = new HoaBoardService(hoaRepository);

        MemberId member = new MemberId("a");
        List<String> memberIdList = List.of(member.getMemberIdString());

        // act
        hoaBoardService.setBoard(hoaId, memberIdList);

        // assert
        assertThat(hoaBoardService.getBoard(hoaId)).hasSize(1);
        assertThat(members.get(0).getBoardMemberOfHoa()).isEqualTo(spied);
        assertThat(members.get(1).getBoardMemberOfHoa()).isNull();
        verify(hoaRepository, times(1)).save(spied);
        verify(spied, times(1)).clearBoardMembers();
        verify(spied, times(1)).addBoardMember(memberIdList.get(0));
        verify(spied, times(1)).addBoardMember(any());
    }

    @Test
    void setBoardOnlyMembersCanBecomeBoardMembers() {
        // arrange
        long hoaId = 4L;

        Member.MemberBuilder memberBuilder = Member.builder()
                .joinTime(new Date())
                .address(new SpecificAddress("Czech Republic", "Prague", "1", "Street", "23"))
                .yearsOnHoaBoard(0);

        List<Member> members = List.of(
                memberBuilder.memberId(new MemberId("a")).build(),
                memberBuilder.memberId(new MemberId("b")).build()
        );

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .id(hoaId)
                .hoaName(new HoaName("HoaName"))
                .boardMembers(new HashSet<>(List.of(members.get(1))))
                .address(new Address("Czech Republic", "Prague"))
                .members(new HashSet<>(List.of(members.get(1))))
                .build();

        HomeOwnersAssociation spied = spy(hoa);
        members.get(1).boardMemberOfHoa = spied;

        HoaRepository hoaRepository = mock(HoaRepository.class);
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(spied));

        HoaBoardService hoaBoardService = new HoaBoardService(hoaRepository);

        // act
        hoaBoardService.setBoard(hoaId, List.of("a"));

        // assert
        assertThat(hoaBoardService.getBoard(hoaId)).isEmpty();
        assertThat(members.get(0).getBoardMemberOfHoa()).isNull();
        assertThat(members.get(1).getBoardMemberOfHoa()).isNull();
        verify(hoaRepository, times(1)).save(spied);
        verify(spied, times(1)).clearBoardMembers();
        verify(spied, times(1)).addBoardMember("a");
        verify(spied, times(1)).addBoardMember(any());
    }

    @Test
    void getBoardReturnsCorrectly() {
        // arrange
        long hoaId = 4L;

        Member.MemberBuilder memberBuilder = Member.builder()
                .joinTime(new Date())
                .address(new SpecificAddress("Czech Republic",
                        "Prague",
                        "1",
                        "Street",
                        "97"))
                .yearsOnHoaBoard(0);

        List<Member> members = List.of(memberBuilder.memberId(new MemberId("a")).build(),
                memberBuilder.memberId(new MemberId("b")).build());

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .id(hoaId)
                .hoaName(new HoaName("HoaName"))
                .boardMembers(new HashSet<>(members))
                .address(new Address("Czech Republic", "Prague"))
                .members(new HashSet<>(members))
                .build();

        for (Member member : members) {
            member.boardMemberOfHoa = hoa;
        }

        HoaRepository hoaRepository = mock(HoaRepository.class);
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.of(hoa));

        HoaBoardService hoaBoardService = new HoaBoardService(hoaRepository);

        List<String> memberIdStringList = members.stream()
                .map(m -> m.memberId.getMemberIdString())
                .collect(Collectors.toList());

        // act
        List<String> boardMembers = hoaBoardService.getBoard(hoaId);

        // assert
        assertThat(boardMembers).hasSameElementsAs(memberIdStringList);
    }

    @Test
    void getBoardNoHoa() {
        // arrange
        long hoaId = 4L;

        HoaRepository hoaRepository = mock(HoaRepository.class);
        when(hoaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        HoaBoardService hoaBoardService = new HoaBoardService(hoaRepository);

        // act and assert
        assertThat(hoaBoardService.getBoard(hoaId)).isEmpty();
    }
}
