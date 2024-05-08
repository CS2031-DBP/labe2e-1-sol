package org.e2e.labe2e01.passenger.infrastructure;

import jakarta.transaction.Transactional;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.user.infrastructure.BaseUserRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface PassengerRepository extends BaseUserRepository<Passenger> {

}