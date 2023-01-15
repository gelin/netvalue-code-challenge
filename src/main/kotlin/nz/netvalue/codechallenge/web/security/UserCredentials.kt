package nz.netvalue.codechallenge.web.security

interface UserCredentials {
    val username: String
    val roles: Set<String>
}
