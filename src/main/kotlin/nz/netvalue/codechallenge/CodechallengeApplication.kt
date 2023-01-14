package nz.netvalue.codechallenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class CodechallengeApplication

fun main(args: Array<String>) {
	runApplication<CodechallengeApplication>(*args)
}
