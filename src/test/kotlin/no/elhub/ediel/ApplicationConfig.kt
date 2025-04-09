package no.elhub.ediel

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

object ApplicationConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(DatabaseSetup)
}
