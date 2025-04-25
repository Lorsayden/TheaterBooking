package dat.routes;

import dat.controllers.impl.PerformanceController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final PerformanceController performanceController = new PerformanceController();

    public EndpointGroup getRoutes() {
        return () -> {
            get("/performances/{id}/props", performanceController::getPropsForPerformance);
            get("/performances/{id}/props/total-cost", performanceController::getPropCostEstimate);
            get("/performances/{id}/with-props", performanceController::readWithProps);

            get("/performances/actor-revenue", performanceController::getActorTotalRevenues);
            get("/performances", performanceController::readAll);
            get("/performances/{id}", performanceController::read);
            post("/performances", performanceController::create);
            put("/performances/{id}", performanceController::update);
            delete("/performances/{id}", performanceController::delete);
            put("/performances/{performance_id}/actors/{actor_id}", performanceController::addActor);
            get("/performances/actor/{actor_id}", performanceController::getPerformancesByActor);
            get("/performances/genre/{genre}", performanceController::filterByGenre);

            post("/performances/populate", performanceController::populate);
        };
    }
}
