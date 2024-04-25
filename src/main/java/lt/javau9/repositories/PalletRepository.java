package lt.javau9.repositories;

import lt.javau9.models.Pallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PalletRepository extends JpaRepository<Pallet, Long> {
}
