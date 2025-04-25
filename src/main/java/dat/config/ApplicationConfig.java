package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.exceptions.ApiErrorResponse;
import dat.routes.Routes;
import dat.exceptions.ApiException;
import dat.utils.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static int count = 1;

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes");
        config.router.contextPath = "/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());

        config.jsonMapper(new CustomJacksonMapper(jsonMapper));
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        app.after(ApplicationConfig::afterRequest);

        // Better format for errors. Previous errors were not formatted nicely as
        // easily readable JSON
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());

            String type;
            if (e.getStatusCode() >= 500) {
                type = "error";
            } else if (e.getStatusCode() >= 400) {
                type = "warning";
            } else {
                type = "info";
            }

            ctx.json(new ApiErrorResponse(
                    type,
                    e.getMessage(),
                    e.getStatusCode(),
                    ctx.path()
            ));
        });

        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);
        app.start(port);
        return app;
    }

    public static void afterRequest(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        logger.info(" Request {} - {} was handled with status code {}", count++, requestInfo, ctx.status());
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    private static void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("An unhandled exception occurred", e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "error", e.getMessage()));
    }

}
