package nz.netvalue.codechallenge.web.auth

import nz.netvalue.codechallenge.core.user.FindUserRepository
import nz.netvalue.codechallenge.core.user.UserModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Part of the integration with Spring Security.
 * @see [Spring Security: Authentication with a Database-backed UserDetailsService](https://www.baeldung.com/spring-security-authentication-with-a-database)
 */
@Service
class DatabaseUserDetailsService(
    private val repository: FindUserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserModel = repository.findUserByName(username) ?: throw UsernameNotFoundException(username)
        return DatabaseUserPrincipal(user)
    }
}

/**
 * [UserDetails] extracted from [FindUserRepository].
 */
class DatabaseUserPrincipal(
    private val user: UserModel
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        // TODO
        return listOf()
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.name

    override fun isAccountNonExpired(): Boolean = true  // TODO: implement account expiration?

    override fun isAccountNonLocked(): Boolean = true   // TODO: implement account lock?

    override fun isCredentialsNonExpired(): Boolean = true      // TODO: implement credentials expiration?

    override fun isEnabled(): Boolean = true            // TODO: implement disabling of users?

}
