package com.sm.makedelivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/*
	@EnableRedisHttpSession
	기존 서버 세션 저젱소를 사용하지 않고 Redis의 Session Stroage에 Session을 저장합니다.
	springSessionRepositoryFilter라는 이름의 필터를 스프링빈에 생성합니다.
	springSessionRepositoryFilter는 HttpSession을 스프링 세션에 의해 지원되는 커스텀 구현체로 바꿔줍니다.
	이 어노테이션이 붙은 곳에는 레디스가 스프링 세션을 지원합니다.
 */

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10)
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

}
