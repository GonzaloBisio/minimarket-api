package group.minimarketapi.config;

import group.minimarketapi.application.security.UserPrincipal;
import group.minimarketapi.domain.model.admin.SystemUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<SystemUser> {

    @Override
    public Optional<SystemUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return Optional.empty();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


        SystemUser systemUserProxy = new SystemUser();
        systemUserProxy.setId(userPrincipal.getId());

        return Optional.of(systemUserProxy);
    }
}