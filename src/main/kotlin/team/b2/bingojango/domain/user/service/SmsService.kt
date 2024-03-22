import com.twilio.Twilio
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service
class SmsService {
    private val accountSid: String = System.getenv("TWILIO_ACCOUNT_SID")
    private val authToken: String = System.getenv("TWILIO_AUTH_TOKEN")
    private val twilioPhoneNumber: String = System.getenv("TWILIO_PHONE_NUMBER")

    init {
        Twilio.init(accountSid, authToken)
    }

    fun sendSms(to: String, message: String) {
        val from = com.twilio.type.PhoneNumber(twilioPhoneNumber)
        val toPhoneNumber = com.twilio.type.PhoneNumber(to)

        val sms = com.twilio.rest.api.v2010.account.Message.creator(
            toPhoneNumber,
            from,
            message
        ).create()

        println("SMS sent with SID: ${sms.sid}")
    }
}