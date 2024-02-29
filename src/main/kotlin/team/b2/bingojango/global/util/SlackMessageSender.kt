package team.b2.bingojango.global.util

import com.slack.api.Slack
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SlackMessageSender(
    @Value("\${slack.bot-token}")
    private val slackToken: String,
    @Value("\${slack.channel}")
    private val channel: String
) {
    fun sendSlackMessage(message: String) {
        val client = Slack.getInstance().methods()
        client.chatPostMessage {
            it.token(slackToken)
                .channel(channel)
                .text(message)
        }
    }
}