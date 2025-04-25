package dat.config;

import dat.entities.Actor;
import dat.entities.Performance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

public class Populate {

    public static void populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Delete existing data
            em.createQuery("DELETE FROM Performance").executeUpdate();
            em.createQuery("DELETE FROM Actor").executeUpdate();

            // Reset ID sequences (PostgreSQL specific)
            em.createNativeQuery("ALTER SEQUENCE performance_performance_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE actor_actor_id_seq RESTART WITH 1").executeUpdate();

            // Create actors
            Actor alice = createActor("Alice", "Smith", "alice@example.com", 12345678, 5);
            Actor bob = createActor("Bob", "Johnson", "bob@example.com", 87654321, 10);
            Actor clara = createActor("Clara", "Andersen", "clara@example.com", 44442222, 3);
            Actor daniel = createActor("Daniel", "Hansen", "daniel@example.com", 33335555, 7);

            em.persist(alice);
            em.persist(bob);
            em.persist(clara);
            em.persist(daniel);

            // Create performances
            em.persist(createPerformance("The Comedy Show", 1, 2, 100.0f, 55.0, 12.0, Performance.Genre.COMEDY, alice));
            em.persist(createPerformance("Dramatic Night", 2, 3, 110.0f, 56.0, 13.0, Performance.Genre.DRAMA, bob));
            em.persist(createPerformance("Musical Mayhem", 3, 4, 120.0f, 57.0, 14.0, Performance.Genre.MUSICAL, clara));
            em.persist(createPerformance("Laugh Out Loud", 4, 5, 130.0f, 58.0, 15.0, Performance.Genre.COMEDY, daniel));
            em.persist(createPerformance("Deep Drama", 1, 3, 140.0f, 59.0, 16.0, Performance.Genre.DRAMA, bob));
            em.persist(createPerformance("Song and Dance", 2, 4, 150.0f, 60.0, 17.0, Performance.Genre.MUSICAL, clara));
            em.persist(createPerformance("HaHa Fest", 3, 5, 160.0f, 61.0, 18.0, Performance.Genre.COMEDY, alice));

            em.getTransaction().commit();
        }
    }

    private static Actor createActor(String fn, String ln, String email, int phone, int exp) {
        Actor a = new Actor();
        a.setFirstName(fn);
        a.setLastName(ln);
        a.setEmail(email);
        a.setPhone(phone);
        a.setYearsOfExperience(exp);
        return a;
    }

    private static Performance createPerformance(String title, int startOffset, int endOffset, float price, double lat, double lon, Performance.Genre genre, Actor actor) {
        Performance p = new Performance();
        p.setTitle(title);
        p.setStartTime(LocalDateTime.now().plusDays(startOffset));
        p.setEndTime(LocalDateTime.now().plusDays(endOffset));
        p.setTicketPrice(price);
        p.setLatitude(lat);
        p.setLongitude(lon);
        p.setGenre(genre);
        p.setActor(actor);
        return p;
    }
}
