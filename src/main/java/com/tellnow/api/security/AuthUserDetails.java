package com.tellnow.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tellnow.api.domain.TellnowProfile;

/**
 * A lightweight object holding info about the currently logged in user
 */
public class AuthUserDetails implements UserDetails {

	private static final long serialVersionUID = 4387815199033285806L;

	private Long id;
	private final String username;
	private Collection<? extends GrantedAuthority> grantedAuthorities;

	public AuthUserDetails(TellnowProfile profile, Collection<? extends GrantedAuthority> grantedAuthorities) {
		id = profile.getId();
		username = profile.getProfileId();

		this.grantedAuthorities = grantedAuthorities;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return "";
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
		return true;
	}
}
