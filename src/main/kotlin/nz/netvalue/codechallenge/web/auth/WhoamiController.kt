package nz.netvalue.codechallenge.web.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Shows the current logged user data.
 */
@RestController
class WhoamiController {

    @GetMapping("/whoami")
    fun getWhoami(userDetails: UserDetails?): WhoamiView {
        return WhoamiView(
            userDetails?.username ?: "unauthorized"
        )
    }

}

data class WhoamiView(
    val name: String
)
