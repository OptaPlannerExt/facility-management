package org.acme.facilitylocation.rest;


// For Keycloak
/* import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;


import org.jboss.resteasy.annotations.cache.NoCache;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.Authenticated;
*/
// End Keycloak

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.facilitylocation.domain.FacilityLocationProblem;
import org.acme.facilitylocation.persistence.FacilityLocationProblemRepository;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.solver.SolverManager;




@Path("/flp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@Authenticated
public class SolverResource {

    /* @Inject
    SecurityIdentity securityIdentity;*/

    private static final long PID = 0L;

    private final AtomicReference<Throwable> solverError = new AtomicReference<>();

    private final FacilityLocationProblemRepository repository;
    private final SolverManager<FacilityLocationProblem, Long> solverManager;
    private final ScoreManager<FacilityLocationProblem> scoreManager;

    public SolverResource(FacilityLocationProblemRepository repository,
			  SolverManager<FacilityLocationProblem, Long> solverManager,
            		  ScoreManager<FacilityLocationProblem> scoreManager) {
        
	this.repository = repository;
        this.solverManager = solverManager;
        this.scoreManager = scoreManager;
    }

    private Status statusFromSolution(FacilityLocationProblem solution) {
	/*System.out.println("Summary Begin");
	System.out.println(scoreManager.explainScore(solution).getSummary());
	System.out.println("Summary End");
	System.out.println("Status Begin");
	System.out.println(solverManager.getSolverStatus(PID));
	System.out.println("Status End");*/
        return new Status(solution, scoreManager.explainScore(solution).getSummary(), solverManager.getSolverStatus(PID));
    }

    @GET
    @Path("status")
    //@RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    //@NoCache
    public Status status() {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> { 
		throw new RuntimeException("Solver failed", throwable);
        });
        return statusFromSolution(repository.solution().orElse(FacilityLocationProblem.empty()));
    }

    @POST
    @Path("solve")
    //@RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    //@NoCache
    public void solve() {
        Optional<FacilityLocationProblem> maybeSolution = repository.solution();
        maybeSolution.ifPresent(facilityLocationProblem -> solverManager.solveAndListen(
                PID,
                id -> facilityLocationProblem,
                repository::update,
                (problemId, throwable) -> solverError.set(throwable)));
    }

    @POST
    @Path("stopSolving")
    //@RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    //@NoCache
    public void stopSolving() {
        solverManager.terminateEarly(PID);
    }
}
