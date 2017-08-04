package org.fablat.auth.config;

import org.fablat.auth.entities.Fabber;
import org.fablat.auth.model.dao.FabberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private FabberDAO fabberDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Fabber user = fabberDAO.findByEmail(username);
		if (user == null) {
			return null;
		}

		return new AppUserDetails(user);
	}
	
}
