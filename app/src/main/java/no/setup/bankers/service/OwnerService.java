package no.setup.bankers.service;

import java.sql.Connection;

import no.setup.bankers.domain.Owner;
import no.setup.bankers.persistence.AccountRepo;
import no.setup.bankers.persistence.OwnerRepo;
import no.setup.bankers.persistence.TransactionRepo;

public class OwnerService implements IOwnerService {

    private final AccountRepo accountRepo;
    private final OwnerRepo ownerRepo;

    public OwnerService(
        AccountRepo accountRepo,
        OwnerRepo ownerRepo
    ) {
        this.accountRepo = accountRepo;
        this.ownerRepo = ownerRepo;
    }

    @Override
    public int createOwner(
        Connection c,
        String firstname, 
        String surname, 
        String email, 
        String phonenumber
    ) {
        return ownerRepo.create(
            c, 
            firstname, 
            surname, 
            email, 
            phonenumber
        );
    }

    @Override
    public int getOwnerIdFromEmail(Connection c, String email) {
        var res = ownerRepo.findByEmail(c, email);
        if (!res.isPresent()) return 0;
        
        return res.get().id();
    }
    
}
