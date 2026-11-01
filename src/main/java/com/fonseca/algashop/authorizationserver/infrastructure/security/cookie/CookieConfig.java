package com.fonseca.algashop.authorizationserver.infrastructure.security.cookie;

import com.fonseca.algashop.authorizationserver.infrastructure.security.AlgashopSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {

	@Bean
	public CookieSerializer cookieSerializer(AlgashopSecurityProperties algaShopSecurityProperties) {
		var properties = algaShopSecurityProperties.getCookie();
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();

		serializer.setCookieName("SESSION");
		serializer.setCookiePath("/");
		serializer.setUseHttpOnlyCookie(true);

		serializer.setUseSecureCookie(properties.getSecure());
		serializer.setSameSite(properties.getSameSite());
		serializer.setDomainName(properties.getDomainName());

		return serializer;
	}
}