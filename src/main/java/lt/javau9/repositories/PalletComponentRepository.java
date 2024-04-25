package lt.javau9.repositories;

import lt.javau9.models.PalletComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PalletComponentRepository extends JpaRepository<PalletComponent, Long> {
}
