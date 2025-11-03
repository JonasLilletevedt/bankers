package no.setup.bankers.http;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import no.setup.bankers.domain.Account;
import no.setup.bankers.persistence.AccountRepo;
import no.setup.bankers.persistence.Db;
import no.setup.bankers.persistence.OwnerRepo;
import no.setup.bankers.persistence.Sql;
import no.setup.bankers.persistence.TransactionRepo;
import no.setup.bankers.service.AccountService;
import no.setup.bankers.service.OwnerService;
import no.setup.bankers.util.ErrorUtil;


public final class Api {

    public static Javalin start(
        int port, 
        Db db, 
        AccountService asvc, 
        OwnerService osvc
    ) {
        db.migrate();

        var app = Javalin.create(cfg -> {
            cfg.bundledPlugins.enableCors(cors -> cors.addRule(r -> r.anyHost()));
        });
        var json = new ObjectMapper();

        app.get("/api/health", ctx -> ctx.result("ok"));

        app.get("/api/accounts/{accountId}/balance", ctx -> {
            int accountId = Integer.parseInt(ctx.pathParam("accountId"));
            try (var c = db.connect()) {
                int bal = asvc.balanceCents(c, accountId);
                ctx.json(bal);
            }
        });

        app.get("/api/owners/{ownerId}/accounts", ctx -> {
            int ownerId = Integer.parseInt(ctx.pathParam("ownerId"));
            try (var c = db.connect()) {
                List<Account> accounts = asvc.listByOwner(c, ownerId);
                ctx.json(accounts);
            }
        });

        app.post("/api/owner/data", ctx -> {
            try {
                var node = json.readTree(ctx.body());
                var ownerIdNode = node.path("ownerId"); // safer
                if (!ownerIdNode.canConvertToInt()) {
                    ApiResponses.badRequest(ctx, "Missing or invalid 'ownerId' in request body");
                    return;
                }
                int ownerId = ownerIdNode.asInt();

                try (var c = db.connect()) {
                    var data = osvc.getOwnerFromOwnerId(c, ownerId);
                    if (data.isEmpty()) {
                        ApiResponses.notFound(ctx, "No owner found for id=" + ownerId);
                        return;
                    }
                    ApiResponses.ok(ctx, data.get());
                }
            } catch (Sql.DbException e) {
                ErrorUtil.handleSqlError(ctx, e);
            } catch (Exception e) {
                // catch JSON / NPE / whatever
                e.printStackTrace();
                ApiResponses.serverError(ctx, "Unexpected server error: " + e.getMessage());
            }
        });


        app.post("/api/owners", ctx -> {
            try {
                var node = json.readTree(ctx.body());
                String firstname = node.get("firstname").asText();
                String surname = node.get("surname").asText();
                String email = node.get("email").asText();
                String phonenumber = node.get("phonenumber").asText();



                try (var c = db.connect()) {
                    int ownerId = osvc.createOwner(
                        c, 
                        firstname, 
                        surname, 
                        email, 
                        phonenumber
                    );
                    ctx.status(201).json(Map.of("ownerId", ownerId));
                }
            } catch (Sql.DbException e) {
                ErrorUtil.handleSqlError(ctx, e);
            }
        });

        app.post("/api/login/email", ctx -> {
            var node = json.readTree(ctx.body());
            String email = node.get("email").asText(null);

            if (email == null || email.isBlank()) {
                ApiResponses.fieldError(
                    ctx, 
                    "email", 
                    "Missing email"
                );
                return;
            }

            try (var c = db.connect()) {
                int ownerId = osvc.getOwnerIdFromEmail(c, email);
                if (ownerId <= 0) {
                    ApiResponses.notFound(ctx, "No account found for that email");
                    return;
                }
                ApiResponses.ok(ctx, Map.of("ownerId", ownerId));
            } catch (Sql.DbException e) {
                ErrorUtil.handleSqlError(ctx, e);
            }
        });

        app.start(port);

        return app;
    }

    public static void main(String[] args) {
        var db = new Db("bankers.db");
        var sql = new Sql();
        var accRepo = new AccountRepo(sql);
        var tranRepo = new TransactionRepo(sql);
        var ownrRepo = new OwnerRepo(sql);
        var asvc = new AccountService(accRepo, tranRepo);
        var osvc = new OwnerService(accRepo, ownrRepo);
        Api.start(8080, db, asvc, osvc);
    }
}
