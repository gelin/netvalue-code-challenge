package nz.netvalue.codechallenge.core.user

/**
 * Finds user by their name.
 * Used for authorization.
 */
interface FindUserRepository {
    fun findUserByName(name: String): UserModel?
}
