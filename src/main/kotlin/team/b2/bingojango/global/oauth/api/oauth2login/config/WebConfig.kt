package team.b2.bingojango.global.oauth.api.oauth2login.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import team.b2.bingojango.global.oauth.api.oauth2login.converter.OAuth2ProviderConverter

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(OAuth2ProviderConverter())
    }
}