package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class HomeOwnersAssociationTest {

    // Boundary test
    @Test
    void isMemberForThreePlusYearsTestExactlyThreeYears() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("usr"))
                        .joinTime(cal.getTime())
                        .build())))
                .build();

        assertThat(hoa.isMemberForThreePlusYears("usr")).isTrue();
    }

    // Boundary test
    @Test
    void isMemberForThreePlusYearsTestJustUnderThreeYears() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);
        cal.add(Calendar.DATE, 1);

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("username"))
                        .joinTime(cal.getTime())
                        .build())))
                .build();

        assertThat(hoa.isMemberForThreePlusYears("username")).isFalse();
    }

    // Boundary test
    @Test
    void isMemberForThreePlusYearsTestJustOverThreeYears() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);
        cal.add(Calendar.DATE, -1);

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("user"))
                        .joinTime(cal.getTime())
                        .build())))
                .build();

        assertThat(hoa.isMemberForThreePlusYears("user")).isTrue();
    }

    @Test
    void hasNoMembersForThreePlusYearsTest() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);
        cal.add(Calendar.DATE, 1);

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("username"))
                        .joinTime(cal.getTime())
                        .build())))
                .build();

        assertThat(hoa.hasMembersForThreePlusYears()).isFalse();
    }

    @Test
    void hasMembersForThreePlusYearsTest() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);
        cal.add(Calendar.DATE, -1);

        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("user"))
                        .joinTime(cal.getTime())
                        .build())))
                .build();

        assertThat(hoa.hasMembersForThreePlusYears()).isTrue();
    }

    // Boundary test
    @Test
    void isBoardMemberForTenPlusYearsTestExactlyTenYears() {
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("username"))
                        .yearsOnHoaBoard(10)
                        .build())))
                .build();

        assertThat(hoa.isBoardMemberForTenPlusYears("username")).isTrue();
    }

    // Boundary test
    @Test
    void isBoardMemberForTenPlusYearsTestJustUnderTenYears() {
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("username"))
                        .yearsOnHoaBoard(9)
                        .build())))
                .build();

        assertThat(hoa.isBoardMemberForTenPlusYears("username")).isFalse();
    }

    // Boundary test
    @Test
    void isBoardMemberForTenPlusYearsTestJustOverTenYears() {
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("username"))
                        .yearsOnHoaBoard(11)
                        .build())))
                .build();

        assertThat(hoa.isBoardMemberForTenPlusYears("username")).isTrue();
    }

    @Test
    void isBoardMemberForTenPlusYearsTestUserDoesNotExist() {
        HomeOwnersAssociation hoa = HomeOwnersAssociation.builder()
                .members(new HashSet<>(List.of(Member.builder()
                        .memberId(new MemberId("username"))
                        .yearsOnHoaBoard(10)
                        .build())))
                .build();

        assertThat(hoa.isBoardMemberForTenPlusYears("anotherName")).isFalse();
    }
}