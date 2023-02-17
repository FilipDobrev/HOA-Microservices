package nl.tudelft.sem.group23a.activity.services;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Application;
import nl.tudelft.sem.group23a.activity.domain.activities.ApplicationRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Description;
import nl.tudelft.sem.group23a.activity.domain.activities.Election;
import nl.tudelft.sem.group23a.activity.domain.activities.ElectionRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.HoaId;
import nl.tudelft.sem.group23a.activity.integrations.hoa.HoaBoardService;
import nl.tudelft.sem.group23a.commons.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for election procedure handling.
 */
@Service
@RequiredArgsConstructor
public class ElectionProcedureService {

    private static final Logger logger = LoggerFactory.getLogger(ElectionProcedureService.class);

    public static final Integer APPLICATION_LENGTH_IN_MINUTES = 1;

    public static final Integer ELECTION_LENGTH_IN_MINUTES = 1;

    private final transient ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final transient ApplicationRepository applicationRepository;
    private final transient ElectionRepository electionRepository;
    private final transient ActivityRepository activityRepository;
    private final transient HoaBoardService hoaBoardService;

    /**
     * Handles the application procedure of an election for a specific hoa.
     * Schedules the ending of the application period and start the election itself.
     *
     * @param hoaId the id of the hoa
     */
    public void handleApplicationProcedure(long hoaId) {
        logger.info(String.format("Application procedure for hoa with id = %d is starting", hoaId));
        scheduler.schedule(() -> {
            Optional<Application> applicationResult = this.applicationRepository
                    .findFirstByHoaIdOrderByIdDesc(new HoaId(hoaId));

            if (applicationResult.isEmpty()) {
                throw new IllegalStateException("Application activity is missing");
            }

            Application application = applicationResult.get();
            List<String> applicants = application.getVotingResult();

            Description description = new Description(String.format("Elections for hoa with id = %d", hoaId));

            Election election = new Election(application.getHoaId(), description, new HashSet<>(applicants));
            logger.info("Application procedure for hoa with id = {} finished. Election applicants: {}",
                    hoaId, String.join(", ", applicants));
            this.activityRepository.save(election);
        }, APPLICATION_LENGTH_IN_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Handles the election procedure of an election for a specific hoa.
     * Schedules the ending of the election period and sends a request to change the board members.
     *
     * @param hoaId the id of the hoa
     */
    public void handleElectionProcedure(Long hoaId) {
        logger.info(String.format("Election procedure for hoa with id = %d is starting", hoaId));
        scheduler.schedule(() -> {
            Optional<Election> electionResult = electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(hoaId));

            if (electionResult.isEmpty()) {
                throw new IllegalStateException("Election activity is missing");
            }
            Election application = electionResult.get();
            List<String> boardMembers = application.getVotingResult();
            logger.info("Election procedure for hoa with id = {} finished. Board members: {}",
                    hoaId, String.join(", ", boardMembers));
            this.hoaBoardService.updateBoardMembers(hoaId, boardMembers);
        }, ELECTION_LENGTH_IN_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Updates an application vote.
     *
     * @param hoaId the id of the hoa
     * @param choice the application choice
     * @param username the user who made the application
     * @return whether the operation was successful
     */
    public Result applyForElection(long hoaId, String choice, String username) {
        Optional<Application> activityResult = applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(hoaId));

        if (activityResult.isEmpty()) {
            return Result.unsuccessful("There is no application activity currently for the specific hoa.");
        }

        Application activity = activityResult.get();
        activity.vote(choice, username);
        activityRepository.save(activity);

        return Result.successful();
    }

    /**
     * Updates an election vote.
     *
     * @param hoaId the id of the hoa
     * @param choice the election choice
     * @param username the user who voted
     * @return whether the operation was successful
     */
    public Result voteForElection(long hoaId, String choice, String username) {
        Optional<Election> electionResult = electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(hoaId));

        if (electionResult.isEmpty()) {
            return Result.unsuccessful("There is no election activity currently for the specific hoa.");
        }

        Election election = electionResult.get();
        election.vote(choice, username);
        activityRepository.save(election);

        return Result.successful();
    }

    /**
     * Starts the application process by creating an application activity.
     *
     * @param hoaId the id of the hoa
     */
    public void startElectionProcess(Long hoaId) {
        Application application = new Application(new HoaId(hoaId), new Description("Apply for the hoa elections"));
        this.activityRepository.save(application);
    }

    /**
     * Returns whether there is a currently running election for a specific hoa.
     *
     * @param hoaId the id of the hoa
     * @return whether there is an election procedure
     */
    public boolean hasCurrentlyRunningElection(Long hoaId) {
        Optional<Application> applicationResult = this.applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(hoaId));
        if (applicationResult.isEmpty()) {
            return false;
        }

        Application application = applicationResult.get();
        boolean applicationExpired = application.getTimestamp().before(new GregorianCalendar());

        if (!applicationExpired) {
            return true;
        }

        Optional<Election> electionResult = this.electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(hoaId));
        if (electionResult.isEmpty()) {
            return false;
        }

        Election election = electionResult.get();
        return !election.getTimestamp().before(new GregorianCalendar());
    }

    /**
     * Checks whether the given user is already running in an election.
     *
     * @param username the user for whom we need to check this.
     * @return a boolean indicating whether they are, in fact, already running.
     */
    public boolean isCurrentlyRunningInElection(String username) {
        return this.electionRepository.existsByChoicesContaining(username);
    }
}
