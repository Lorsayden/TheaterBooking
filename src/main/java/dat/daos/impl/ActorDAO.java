package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.ActorDTO;
import dat.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ActorDAO implements IDAO<ActorDTO, Integer> {

    private static ActorDAO instance;
    private static EntityManagerFactory emf;

    public static ActorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ActorDAO();
        }
        return instance;
    }

    @Override
    public ActorDTO read(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Actor actor = em.find(Actor.class, id);
            return new ActorDTO(actor);
        }
    }

    @Override
    public List<ActorDTO> readAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<ActorDTO> query = em.createQuery("SELECT new dat.dtos.ActorDTO(t) from Actor t", ActorDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public ActorDTO create(ActorDTO actorDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Actor actor = new Actor(actorDTO);
            em.persist(actor);
            em.getTransaction().commit();
            return new ActorDTO(actor);
        }
    }

    @Override
    public ActorDTO update(Integer id, ActorDTO actorDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, id);
            actor.setFirstName(actorDTO.getFirstName());
            actor.setLastName(actorDTO.getLastName());
            actor.setEmail(actorDTO.getEmail());
            actor.setPhone(actorDTO.getPhone());
            actor.setYearsOfExperience(actorDTO.getYearsOfExperience());

            Actor mergedActor = em.merge(actor);
            em.getTransaction().commit();
            return mergedActor != null ? new ActorDTO(mergedActor) : null;
        }
    }

    @Override
    public void delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, id);
            if(actor != null){
                em.remove(actor);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, id);
            return actor != null;
        }
    }
}
