package nl.tudelft.sem.group23a.hoa.domain.repositories;

import java.util.Optional;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Member;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting member aggregate roots.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Override
    <M extends Member> M save(M member);

    /**
     * Find a member by username and HOA.
     *
     * @param memberId the id of the member
     * @param hoaId the id of the HOA
     * @return an optional of an object from the database
     */
    <M extends Member> Optional<M> findByMemberIdAndMemberOfHoaId(MemberId memberId, long hoaId);
}
