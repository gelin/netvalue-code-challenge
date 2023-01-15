package nz.netvalue.codechallenge.core.user

/**
 * User model exposed by [FindUserRepository].
 */
data class UserModel(
    val id: String,
    val name: String,
    val roles: Set<String>
)
