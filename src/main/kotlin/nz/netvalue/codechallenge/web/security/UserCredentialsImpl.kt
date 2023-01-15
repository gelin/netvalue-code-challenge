package nz.netvalue.codechallenge.web.security

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class UserCredentialsImpl(
    override val username: String,
    override val roles: Set<String>
): UserCredentials
