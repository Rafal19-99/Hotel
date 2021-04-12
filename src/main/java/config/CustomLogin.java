package config;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class CustomLogin extends DatabaseServerLoginModule {    
	@Override
	public boolean validatePassword(String rawInputPassword, String hashedExpectedPassword ) {
		Argon2 argon2 = Argon2Factory.create( Argon2Factory.Argon2Types.ARGON2id);
		return argon2.verify(hashedExpectedPassword, rawInputPassword.toCharArray());
	
	}	
}