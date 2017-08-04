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

public class AppUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Integer id;
	private String email;
	private String firstName;
	private String lastName;
	private Integer idLab;
	private Collection<? extends GrantedAuthority> authorities;

	public AppUserDetails(Fabber user) {
		// mandatory
		this.username = user.getEmail();
		this.password = user.getPassword();
		this.authorities = translate(user.getRoleFabbers());
		// extra
		this.id = user.getIdFabber();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.idLab = user.getLab() != null ? user.getLab().getIdLab() : null;
	}
	
	private Collection<? extends GrantedAuthority> translate(Set<RoleFabber> rolesUser) {
		List<Role> roles = new ArrayList<Role>();
		for (RoleFabber roleFabber : rolesUser) {
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
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Integer getIdLab() {
		return idLab;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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
