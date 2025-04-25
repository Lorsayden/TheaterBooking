package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.controllers.IController;
import dat.daos.impl.ActorDAO;
import dat.daos.impl.PerformanceDAO;
import dat.dtos.ActorDTO;
import dat.dtos.PerformanceDTO;
import dat.dtos.PropDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static dat.utils.PropFetcher.fetchPropsByGenre;

public class PerformanceController implements IController<PerformanceDTO, Integer> {

    private PerformanceDAO dao;
    private EntityManagerFactory emf;

    public PerformanceController(){
        this.emf = HibernateConfig.getEntityManagerFactory();
        this.dao = PerformanceDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();

        PerformanceDTO performanceDTO = dao.read(id);

        ctx.status(200).json(performanceDTO);
    }

    @Override
    public void readAll(Context ctx) {

        List<PerformanceDTO> performanceDTOS = dao.readAll()
                .stream()
                .sorted(Comparator.comparing(PerformanceDTO::getId))
                .toList();

        ctx.res().setStatus(200);
        ctx.json(performanceDTOS);
    }

    @Override
    public void create(Context ctx) {
        PerformanceDTO request = validateEntity(ctx);
        PerformanceDTO created = dao.create(request);
        ctx.status(201).json(created);
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        dao.update(id, validateEntity(ctx));

        ctx.res().setStatus(200);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        dao.delete(id);

        ctx.res().setStatus(204);
    }

    public void addActor(Context ctx){
        int performanceId = ctx.pathParamAsClass("performance_id", Integer.class).get();
        int actorId = ctx.pathParamAsClass("actor_id", Integer.class).get();

        dao.addActorToPerformance(performanceId, actorId);

        ctx.status(204);
    }

    public void getPerformancesByActor(Context ctx) {
        int id = ctx.pathParamAsClass("actor_id", Integer.class).get();
        Set<PerformanceDTO> performances = dao.getPerformancesByActor(id);
        ctx.status(200).json(performances);
    }

    public void filterByGenre(Context ctx) {
        String genre = ctx.pathParam("genre");
        Set<PerformanceDTO> performances = dao.getPerformancesByGenre(genre);
        ctx.status(200).json(performances);
    }

    public void getActorTotalRevenues(Context ctx) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        PerformanceDAO performanceDAO = PerformanceDAO.getInstance(emf);
        ActorDAO actorDAO = ActorDAO.getInstance(emf);

        List<Map<String, Object>> result = new ArrayList<>();

        for (ActorDTO actor : actorDAO.readAll()) {
            float totalRevenue = performanceDAO.getPerformancesByActorAllowEmpty(actor.getId())
                    .stream()
                    .map(PerformanceDTO::getTicketPrice)
                    .reduce(0f, Float::sum);

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("actorId", actor.getId());
            entry.put("totalRevenue", totalRevenue);
            result.add(entry);
        }

        ctx.status(200).json(result);
    }

    public void readWithProps(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        PerformanceDTO performance = dao.read(id);
        List<PropDTO> props = fetchPropsByGenre(performance.getGenre().name().toLowerCase());

        Map<String, Object> response = new HashMap<>();
        response.put("performance", performance);
        response.put("props", props);

        ctx.status(200).json(response);
    }


    public void getPropsForPerformance(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        PerformanceDTO performance = dao.read(id);
        List<PropDTO> props = fetchPropsByGenre(performance.getGenre().name().toLowerCase());
        ctx.status(200).json(props);
    }

    public void getPropCostEstimate(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        PerformanceDTO performance = dao.read(id);
        List<PropDTO> props = fetchPropsByGenre(performance.getGenre().name().toLowerCase());

        int totalCost = props.stream()
                .mapToInt(PropDTO::getCostEstimate)
                .sum();

        ctx.status(200).json(Map.of("performance_id", id, "totalPropCost", totalCost));
    }


    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public PerformanceDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(PerformanceDTO.class)
                .check(s -> s.getStartTime() != null && s.getStartTime().isAfter(LocalDateTime.now()), "Not a valid start time: End time must be later than the current time")
                .check(s -> s.getEndTime() != null && s.getEndTime().isAfter(s.getStartTime()), "Not a valid start time: End time must be after start time")
                .check(s -> s.getTitle() != null && !s.getTitle().isBlank(), "Not a valid title")
                .check(s -> s.getTicketPrice() > 0, "Not a valid price")
                .check(s -> s.getLatitude() > 0, "Not a valid location for Latitude")
                .check(s -> s.getLongitude() > 0, "Not a valid location for Longitude")
                .check(s -> s.getGenre() != null, "Not a valid genre")
                .get();
    }

    public void populate(Context ctx) {
        Populate.populate(HibernateConfig.getEntityManagerFactory());
        ctx.status(200).json("{\"message\": \"Database has been wiped, reset and repopulated\"}");
    }
}
