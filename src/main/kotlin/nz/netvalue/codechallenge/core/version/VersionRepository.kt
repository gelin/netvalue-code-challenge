package nz.netvalue.codechallenge.core.version

/**
 * Returns version of the app and the database.
 */
interface VersionRepository {
    fun getVersion(): VersionModel
}

/**
 * Version model returned by [VersionRepository].
 */
data class VersionModel(
    val application: String,
    val database: String
)
