package team.b2.bingojango.global.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@PropertySource("classpath:application.yml")
@Configuration
class AwsConfig {
    @Value("\${accessKey}")
    private val accessKey: String? = null

    @Value("\${secretKey}")
    private val secretKey: String? = null

    @Value("\${region}")
    private val region: String? = null

    @Bean
    fun amazonS3Client(): AmazonS3Client {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .build() as AmazonS3Client
    }
}