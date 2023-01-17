package nz.netvalue.codechallenge.web.whoami

import nz.netvalue.codechallenge.web.security.UserCredentials
import nz.netvalue.codechallenge.web.view.ResponseView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Shows the current logged user data.
 */
@RestController
@RequestMapping("/whoami")
class WhoamiController {

    @GetMapping
    fun getWhoami(user: UserCredentials?): ResponseView<WhoamiView> {
        return ResponseView(
            result = WhoamiView(
                name = user?.username ?: "unauthenticated",
                roles = user?.roles ?: setOf()
            )
        )
    }

}

data class WhoamiView(
    val name: String,
    val roles: Set<String>
)
