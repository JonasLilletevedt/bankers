package no.setup.bankers.http;

import io.javalin.Javalin;
import no.setup.bankers.service.AccountService;
import no.setup.bankers.persistence.AccountRepo;
import no.setup.bankers.persistence.TransactionRepo;
import no.setup.bankers.persistence.Db;
import no.setup.bankers.persistence.Sql;

public final class Api {

    public static Javalin start(int port, Db db, AccountService svc) {
        var app = Javalin.create(cfg -> {
            cfg.bundledPlugins.enableCors(cors -> cors.addRule(r -> r.anyHost()));
        });

        app.get("/api/health", ctx -> ctx.result("ok"));

        app.get("/api/accounts/{id}/balance", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            try (var c = db.connect()) {
                int bal = svc.balanceCents(c, id);
                ctx.json(bal);
            }
        });

        app.start(port);

        return app;
    }

    public static void main(String[] args) {
        var db = new Db("bankers.db");
        var sql = new Sql();
        var svc = new AccountService(
            new AccountRepo(sql), 
            new TransactionRepo(sql)
        );
        Api.start(8080, db, svc);
    }
}
