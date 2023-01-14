package nz.netvalue.codechallenge.core.user

/**
 * User model exposed by [FindUserRepository].
 * [UserModel.password] is a stored hash of the password, it's sensitive information and must not be exported.
 */
data class UserModel(
    val id: String,
    val name: String,
    val password: String
)
