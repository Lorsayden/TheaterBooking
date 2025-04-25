package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.IPerformanceActorDAO;
import dat.dtos.PerformanceDTO;
import dat.entities.Actor;
import dat.entities.Performance;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerformanceDAO implements IDAO<PerformanceDTO, Integer>, IPerformanceActorDAO {

    private static PerformanceDAO instance;
    private static EntityManagerFactory emf;

    public static PerformanceDAO getInstance(EntityManagerFactory _emf){
        if(instance == null){
            emf = _emf;
            instance = new PerformanceDAO();
        }
        return instance;
    }

    @Override
    public PerformanceDTO read(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Performance performance = em.find(Performance.class, id);
            if (performance == null) {
                throw new ApiException(404, "Performance with id " + id + " not found");
            }
            return new PerformanceDTO(performance, true);
        }
    }

    @Override
    public List<PerformanceDTO> readAll() {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            TypedQuery<PerformanceDTO> query = em.createQuery("SELECT new dat.dtos.PerformanceDTO(p) from Performance p", PerformanceDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public PerformanceDTO create(PerformanceDTO PerformanceDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Performance performance = new Performance(PerformanceDTO);
            em.persist(performance);
            em.getTransaction().commit();
            return new PerformanceDTO(performance);

        }
    }

    @Override
    public PerformanceDTO update(Integer id, PerformanceDTO performanceDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            Performance performance = em.find(Performance.class, id);
            if (performance == null) {
                throw new ApiException(404, "Performance with id " + id + " not found");
            }

            performance.setStartTime(performanceDTO.getStartTime());
            performance.setEndTime(performanceDTO.getEndTime());
            performance.setTitle(performanceDTO.getTitle());
            performance.setTicketPrice(performanceDTO.getTicketPrice());
            performance.setLatitude(performanceDTO.getLatitude());
            performance.setLongitude(performanceDTO.getLongitude());
            performance.setGenre(performanceDTO.getGenre());
            Performance mergedPerformance = em.merge(performance);
            em.getTransaction().commit();
            return mergedPerformance != null ? new PerformanceDTO(mergedPerformance) : null;
        }
    }

    @Override
    public void delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            Performance performance = em.find(Performance.class, id);
            if (performance == null) {
                throw new ApiException(404, "Performance with id " + id + " not found");
            }

            em.remove(performance);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            return em.find(Performance.class, id) != null;
        }
    }

    @Override
    public void addActorToPerformance(int performanceId, int actorId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Performance performance = em.find(Performance.class, performanceId);
            Actor actor = em.find(Actor.class, actorId);

            if (performance == null) {
                throw new ApiException(404, "Performance not found with id " + performanceId);
            }
            if (actor == null) {
                throw new ApiException(404, "Actor not found with id " + actorId);
            }

            performance.setActor(actor);
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<PerformanceDTO> getPerformancesByActor(int actorId) {
        try (EntityManager em = emf.createEntityManager()) {
            Actor actor = em.find(Actor.class, actorId);
            if (actor == null) {
                throw new ApiException(404, "Actor with id " + actorId + " not found");
            }

            TypedQuery<PerformanceDTO> query = em.createQuery(
                    "SELECT new dat.dtos.PerformanceDTO(p) FROM Performance p WHERE p.actor.id = :actor_id",
                    PerformanceDTO.class
            );
            query.setParameter("actor_id", actorId);

            List<PerformanceDTO> results = query.getResultList();
            if (results.isEmpty()) {
                throw new ApiException(404, "No performances found for actor with id " + actorId);
            }

            return new HashSet<>(results);
        }
    }

    public Set<PerformanceDTO> getPerformancesByActorAllowEmpty(int actorId) {
        try (EntityManager em = emf.createEntityManager()) {
            Actor actor = em.find(Actor.class, actorId);
            if (actor == null) {
                throw new ApiException(404, "Actor with id " + actorId + " not found");
            }

            TypedQuery<PerformanceDTO> query = em.createQuery(
                    "SELECT new dat.dtos.PerformanceDTO(p) FROM Performance p WHERE p.actor.id = :actor_id",
                    PerformanceDTO.class
            );
            query.setParameter("actor_id", actorId);
            return new HashSet<>(query.getResultList());
        }
    }

    public Set<PerformanceDTO> getPerformancesByGenre(String genreString) {
        try (EntityManager em = emf.createEntityManager()) {
            Performance.Genre genre;
            try {
                genre = Performance.Genre.valueOf(genreString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException(400, "Invalid genre. Must be one of: DRAMA, COMEDY, MUSICAL");
            }

            TypedQuery<PerformanceDTO> query = em.createQuery(
                    "SELECT new dat.dtos.PerformanceDTO(p) FROM Performance p WHERE p.genre = :genre",
                    PerformanceDTO.class
            );
            query.setParameter("genre", genre);

            List<PerformanceDTO> results = query.getResultList();
            if (results.isEmpty()) {
                throw new ApiException(404, "No performances found for genre: " + genre.name());
            }

            return new HashSet<>(results);
        }
    }
}
