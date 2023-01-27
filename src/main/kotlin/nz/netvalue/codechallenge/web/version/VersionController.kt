package nz.netvalue.codechallenge.web.version

import io.swagger.v3.oas.annotations.Operation
import nz.netvalue.codechallenge.core.version.VersionModel
import nz.netvalue.codechallenge.core.version.VersionRepository
import nz.netvalue.codechallenge.web.view.ResponseView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller to return known version.
 */
@RestController
@RequestMapping("/version")
class VersionController(
    private val repository: VersionRepository
) {

    @GetMapping(produces = ["application/json"])
    @Operation(operationId = "version",
        description = "Shows the current app and database version",
        tags = ["util"])
    fun getVersion(): ResponseView<VersionView> {
        return ResponseView(result = repository.getVersion().toView())
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
