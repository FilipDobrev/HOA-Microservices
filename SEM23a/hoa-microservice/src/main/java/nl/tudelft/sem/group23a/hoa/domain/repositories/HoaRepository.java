package nl.tudelft.sem.group23a.hoa.domain.repositories;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HomeOwnersAssociation;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoaRepository extends JpaRepository<HomeOwnersAssociation, Long> {

    @Override
    <H extends HomeOwnersAssociation> H save(H hoa);

    <H extends HomeOwnersAssociation> Optional<H> findById(long id);

    <H extends HomeOwnersAssociation> Optional<H> findByIdAndMembersMemberId(long id, MemberId memberId);

    <H extends HomeOwnersAssociation> Optional<H> findByIdAndBoardMembersMemberId(long id, MemberId memberId);

    <H extends HomeOwnersAssociation> List<H> findByBoardMembersMemberId(MemberId memberId);

    @Override
    void delete(HomeOwnersAssociation hoa);
}
