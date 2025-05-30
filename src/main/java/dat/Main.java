package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;

public class Main {

    public static void main(String[] args) {
        Populate.populate(HibernateConfig.getEntityManagerFactory());
        ApplicationConfig.startServer(7070);
    }
}