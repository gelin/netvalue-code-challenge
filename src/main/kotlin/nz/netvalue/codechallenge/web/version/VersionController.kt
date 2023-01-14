package nz.netvalue.codechallenge.web.version

import nz.netvalue.codechallenge.core.version.VersionModel
import nz.netvalue.codechallenge.core.version.VersionRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller to return known version.
 */
@RestController
class VersionController(
    private val repository: VersionRepository
) {

    @GetMapping("/version")
    fun getVersion(): VersionView {
        return repository.getVersion().toView()
    }

}

/**
 * Version visible via REST API.
 * Is returned by [VersionController].
 */
data class VersionView(
    val application: String,
    val database: String
)

/**
 * Converts [VersionModel] to [VersionView].
 */
fun VersionModel.toView(): VersionView = VersionView(
    application = this.application,
    database = this.database,
)
