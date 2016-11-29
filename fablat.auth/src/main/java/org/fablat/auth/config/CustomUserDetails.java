package org.fablat.auth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.fablat.auth.entities.Fabber;
import org.fablat.auth.entities.Role;
import org.fablat.auth.entities.RoleFabber;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Collection<? extends GrantedAuthority> authorities;
	private String password;
	private String username;

	public CustomUserDetails(Fabber user) {
		this.username = user.getEmail();
		this.password = user.getPassword(); // TODO: change by real password
		this.authorities = translate(user.getRoleFabbers());
	}

	private Collection<? extends GrantedAuthority> translate(Set<RoleFabber> rolesFabber) {

		List<Role> roles = new ArrayList<Role>();

		for (RoleFabber roleFabber : rolesFabber) {
			roles.add(roleFabber.getRole());
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : roles) {
			String name = role.getName().toUpperCase();
			if (!name.startsWith("ROLE_")) {
				name = "ROLE_" + name;
			}
			authorities.add(new SimpleGrantedAuthority(name));
		}
		return authorities;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
}
