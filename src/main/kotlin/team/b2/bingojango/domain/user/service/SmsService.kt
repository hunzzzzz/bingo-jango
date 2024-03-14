
import org.springframework.stereotype.Service

@Service
class SmsService(accountSid: String, authToken: String, val twilioPhoneNumber: String) {

   // init {
        //Twilio.init(accountSid, authToken)
    }

    //fun sendSms(to: String, message: String) //{
        //val from = PhoneNumber(twilioPhoneNumber)
        //val toPhoneNumber = PhoneNumber(to)

        //try {
            //val sms = Message.creator(
            //toPhoneNumber,
            //from,
            //message
            //).create()

            //println("SMS sent with SID: ${sms.sid}")
        // } //catch (e: TwilioException) {
            //println("Failed to send SMS: ${e.message}")
            // 실패한 경우에 대한 처리 추가
        // }
    //}
