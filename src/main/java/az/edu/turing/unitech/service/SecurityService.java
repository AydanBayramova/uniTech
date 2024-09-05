//package az.edu.turing.unitech.service;
//
//import az.edu.turing.unitech.domain.entity.AccountEntity;
//import az.edu.turing.unitech.domain.repository.AccountRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class SecurityService {
//
//    private final AccountRepository accountRepository;
//
//    public SecurityService(AccountRepository accountRepository) {
//        this.accountRepository = accountRepository;
//    }
//
//    public boolean currentUserHasAdminRole() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || authentication.getAuthorities() == null) {
//            return false;
//        }
//
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            if (authority.getAuthority().equals("ROLE_ADMIN")) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
