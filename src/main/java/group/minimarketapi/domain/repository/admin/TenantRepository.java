package group.minimarketapi.domain.repository.admin;

import group.minimarketapi.domain.model.admin.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    boolean existsBySchemaName(String schemaName);
    boolean existsByCompanyName(String companyName);

    Optional<Tenant> findBySchemaName(String schemaName);
    Optional<Tenant> findByCompanyName(String companyName);
}