package team.b2.bingojango.global.oauth.api.oauth2login.converter

import org.springframework.core.convert.converter.Converter
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

class OAuth2ProviderConverter : Converter<String, OAuth2Provider> {

    override fun convert(source: String): OAuth2Provider {
        return runCatching {
            OAuth2Provider.valueOf(source.uppercase())
        }.getOrElse {
            throw IllegalArgumentException()
        }
    }
}