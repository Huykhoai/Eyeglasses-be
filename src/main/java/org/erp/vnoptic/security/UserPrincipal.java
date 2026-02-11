package org.erp.vnoptic.security;

import lombok.Getter;
import org.erp.vnoptic.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final Long employeeId;
    private final Collection<? extends GrantedAuthority> authorities;
    private final List<Integer> roleIds;
    private final Account account;

    public UserPrincipal(Account account, List<String> roles, List<Integer> roleIds) {
        this.account = account;
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.email = account.getEmail();
        this.employeeId = account.getEmployee() != null ? account.getEmployee().getId() : null;
        this.authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        this.roleIds = roleIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.FALSE.equals(account.getDisContinue());
    }
}
