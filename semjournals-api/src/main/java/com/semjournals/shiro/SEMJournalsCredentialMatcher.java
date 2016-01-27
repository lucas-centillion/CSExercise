package com.semjournals.shiro;

import com.semjournals.encryption.HashUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * CredentialMatcher used to match the given password to the one stored on the database.
 * It uses the HashUtils class to encrypt the password using the stored salt and returns true if the resulting
 * encrypted password matches the one stored on the database.
 */
public class SEMJournalsCredentialMatcher extends SimpleCredentialsMatcher {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleCredentialsMatcher.class);

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		SimpleAuthenticationInfo simpleInfo = (SimpleAuthenticationInfo) info;
		Object tokenCredentials = getCredentials(token);
		Object accountCredentials = getCredentials(info);

		try {
			String tokenPassword = new String((char[]) tokenCredentials);
			String encryptedTokenPassword = HashUtil.encrypt(tokenPassword, simpleInfo.getCredentialsSalt().getBytes());

			return equals(encryptedTokenPassword, accountCredentials);
		} catch (UnsupportedEncodingException e) {
			final String message = "There was an error while hashing the password [" + token.getPrincipal() + "]";
			LOG.error(message, e);
			throw new AuthenticationException(message, e);
		}
	}
}